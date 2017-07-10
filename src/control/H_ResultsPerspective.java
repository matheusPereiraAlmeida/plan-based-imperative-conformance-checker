package control;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.apache.commons.lang3.SystemUtils;

import main.Constants;
import main.Trace;
import main.Utilities;
import utils.StreamGobbler;
import view.PlannerPerspective;
import view.ResultsPerspective;

public class H_ResultsPerspective {

	public static final String FAST_DOWNWARD_DIR = "fast-downward/";
	public static final String PLANS_FOUND_DIR = FAST_DOWNWARD_DIR + "plans_found/";
	public static final String PDDL_FILES_DIR = FAST_DOWNWARD_DIR + "Conformance_Checking/";
	public static final String PDDL_EXT = ".pddl";
	public static final String PDDL_DOMAIN_FILE_PREFIX = PDDL_FILES_DIR + "domain";
	public static final String PDDL_PROBLEM_FILE_PREFIX = PDDL_FILES_DIR + "problem";
	public static final String PLAN_FILE_PREFIX = PLANS_FOUND_DIR + "alignment_";
	public static final String COST_ENTRY_PREFIX = "; cost = ";
	public static final String SEARCH_TIME_ENTRY_PREFIX = "; searchtime = ";
	public static final String TRACE_NAME_PREFIX = "Trace#";
	public static final String COMMAND_ARG_PLACEHOLDER = "+";

	public ResultsPerspective _view = null;

	private Thread plannerThread;
	private Process plannerManagerProcess;
	private Hashtable<String, String> duplicatedTracesHashtable = new Hashtable<String, String>();
	private Vector<String> tracesWithFailureVector = new Vector<String>();

	private int traceIdToCheckFrom;
	private int traceIdToCheckTo;
	private int minTracesLength;
	private int maxTracesLength;
	private int alignedTracesAmount = 0;
	private float totalAlignmentCost = 0;
	private float totalAlignmentTime = 0;

	private Pattern decimalNumberRegexPattern = Pattern.compile("\\d+(,\\d{3})*(\\.\\d+)*");

	public H_ResultsPerspective (ResultsPerspective i_view){
		_view = i_view;
		initPlannerThread();
		installListeners();		// needs planner thread to be initialized before being executed
		plannerThread.start();  // needs planner thread and listeners to be initialized before being executed
	}

	private void installListeners() {

		_view.addWindowListener(new WindowListener() {

			public void windowOpened(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}

			public void windowClosing(WindowEvent e) {
				killSubprocesses();
			}
		});

		_view.getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				killSubprocesses();
			}
		});

		_view.getAlignedTracesCombobox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {

				String selectedAlignedTraceName = (String) _view.getAlignedTracesCombobox().getSelectedItem();
				Style style = _view.getStyle();


				if (event.getStateChange() == ItemEvent.SELECTED && !(selectedAlignedTraceName.equalsIgnoreCase("Event Log")) ) {
					// show trace-specific results

					_view.resetResultsArea();

					String selectedTraceId = selectedAlignedTraceName.replace(TRACE_NAME_PREFIX, "");
					String traceAlignmentCost = new String();  
					String traceAlignmentTime = new String();  
					Vector<String> pddlAlignmentMovesVector = new Vector<String>();

					try {

						File alignmentFile = new File(PLAN_FILE_PREFIX + selectedTraceId);

						// parse alignment file
						BufferedReader alignmentFileReader = new BufferedReader(new FileReader(alignmentFile));
						String alignmentFileLine;
						while ((alignmentFileLine = alignmentFileReader.readLine()) != null) {
							if(alignmentFileLine.startsWith(COST_ENTRY_PREFIX)) {
								Matcher matcher = decimalNumberRegexPattern.matcher(alignmentFileLine);
								matcher.find();
								traceAlignmentCost = matcher.group();
							}
							else if(alignmentFileLine.startsWith(SEARCH_TIME_ENTRY_PREFIX))  {
								Matcher matcher = decimalNumberRegexPattern.matcher(alignmentFileLine);
								matcher.find();
								traceAlignmentTime = matcher.group();
							}
							else {
								pddlAlignmentMovesVector.addElement(alignmentFileLine);
							}
						}
						alignmentFileReader.close();


						// display trace overview
						_view.appendToResults(">> EVENT LOG FILE = ", Color.BLACK);
						_view.appendToResults(Constants.getEventLogFileName(), Color.RED);
						_view.appendToResults(" (" + selectedAlignedTraceName + ")\n", Color.BLACK);


						_view.appendToResults(">> SEARCH ALGORITHM = ", Color.BLACK);
						if(Constants.getPlannerPerspective().getOptimalRadioButton().isSelected())
							_view.appendToResults("Blind A* (Cost-Optimal) \n", Color.BLUE);
						else
							_view.appendToResults("Lazy Greedy (Suboptimal) \n", Color.BLUE);

						_view.appendToResults(">> ALIGNMENT TIME = ", Color.BLACK);
						_view.appendToResults(traceAlignmentTime + Constants.TIME_UNIT + "\n", Color.BLUE);
						_view.appendToResults(">> ALIGNMENT COST = ", Color.BLACK);
						_view.appendToResults(traceAlignmentCost + "\n", Color.BLUE);

						StyleConstants.setBold(style, true);
						_view.appendToResults("\n--- ALIGNED TRACE ---\n\n", Color.decode("#009933"));
						StyleConstants.setBold(style, false);


						// display alignment moves
						for(String alignmentMove : pddlAlignmentMovesVector) {

							if(alignmentMove.startsWith("(movesync#")) {

								alignmentMove = alignmentMove.replace("(movesync#", "");
								alignmentMove = alignmentMove.substring(0, alignmentMove.lastIndexOf("#"));

								StyleConstants.setStrikeThrough (style, false);
								_view.appendToResults(alignmentMove + "\n", Color.BLACK);

							}
							else if(alignmentMove.startsWith("(moveinthemodel#")) {

								alignmentMove = alignmentMove.replace("(moveinthemodel#", "");
								alignmentMove = alignmentMove.substring(0,alignmentMove.lastIndexOf(" )"));

								StyleConstants.setStrikeThrough (style, false);
								_view.appendToResults(alignmentMove, Color.BLUE);

								if(Constants.getPlannerPerspective().getCostCheckBox().isSelected()) {
									StyleConstants.setItalic(style, true);
									_view.appendToResults(
											" [cost " + Utilities.getCostOfActivity(alignmentMove, "move_in_the_model") + "]",
											Color.BLACK);
									StyleConstants.setItalic(style, false);
								}
								_view.appendToResults("\n", Color.BLACK);

							}
							else if(alignmentMove.startsWith("(moveinthelog#")) {

								alignmentMove = alignmentMove.replace("(moveinthelog#", "");
								alignmentMove = alignmentMove.substring(0,alignmentMove.indexOf("#"));				  	   	    		

								StyleConstants.setStrikeThrough (style,true);
								_view.appendToResults(alignmentMove, Color.RED);

								if(Constants.getPlannerPerspective().getCostCheckBox().isSelected()) {
									StyleConstants.setStrikeThrough (style, false);
									StyleConstants.setItalic(style, true);
									_view.appendToResults(
											" [cost " + Utilities.getCostOfActivity(alignmentMove, "move_in_the_log") + "]",
											Color.BLACK);
									StyleConstants.setItalic(style, false);
								}
								_view.appendToResults("\n", Color.BLACK);
							}
						}

					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
					} 
					catch (BadLocationException be) {
						be.printStackTrace();
					} 
					catch (IOException io) {
						io.printStackTrace();
					} 

					_view.getResultsArea().setCaretPosition(0);


				} else if (event.getStateChange() == ItemEvent.SELECTED && (selectedAlignedTraceName.equalsIgnoreCase("Event Log"))) {
					// show an overview of the results

					try {

						_view.resetResultsArea();
						_view.showPlannerSettings();

						_view.appendToResults(">> RECAP OF THE ALIGNMENT\n\n", Color.BLACK);


						for(int k = traceIdToCheckFrom - 1; k < traceIdToCheckTo; k++) {

							Trace trace = Constants.getAllTracesVector().elementAt(k);

							if(trace.getTraceLength() >= minTracesLength && trace.getTraceLength() <= maxTracesLength)  {		

								_view.appendToResults(">> ", Color.BLACK);
								_view.appendToResults(trace.getTraceName() + " ", Color.BLUE);


								if(Constants.isDiscardDuplicatedTraces()
										&& !Constants.getAllTracesHashtable().containsValue(trace.getTraceName())
										&& Constants.getAllTracesHashtable().containsKey(trace.getTraceTextualContent().toString()))  {  	            

									_view.appendToResults("SKIPPED: equivalent to "
											+ Constants.getAllTracesHashtable()
											.get(trace.getTraceTextualContent().toString()), Color.RED);

								} else {

									String traceAlignmentCost = new String();  
									String traceAlignmentTime = new String();  
									File alignmentFile = new File(PLAN_FILE_PREFIX + trace.getTraceNumber());

									// parse alignment file
									BufferedReader alignmentFileReader = new BufferedReader(new FileReader(alignmentFile)); 
									String alignmentFileLine;
									while ((alignmentFileLine = alignmentFileReader.readLine()) != null) {
										if(alignmentFileLine.startsWith(COST_ENTRY_PREFIX)) {
											Matcher matcher = decimalNumberRegexPattern.matcher(alignmentFileLine);
											matcher.find();
											traceAlignmentCost = matcher.group();
										}
										else if(alignmentFileLine.startsWith(SEARCH_TIME_ENTRY_PREFIX))  {
											Matcher matcher = decimalNumberRegexPattern.matcher(alignmentFileLine);
											matcher.find();
											traceAlignmentTime = matcher.group();
										}
									}
									alignmentFileReader.close();	

									_view.appendToResults("ALIGNED IN ", Color.decode("#009933"));
									_view.appendToResults(traceAlignmentTime + Constants.TIME_UNIT, Color.BLUE);
									_view.appendToResults(" WITH COST ", Color.BLACK);
									_view.appendToResults(traceAlignmentCost + "\n", Color.BLUE);

								}
							}
						}

						_view.showPlannerResultsOverview();

					}
					catch (BadLocationException be) {
						be.printStackTrace();
					} 
					catch (IOException io) {
						io.printStackTrace();
					} 
				}
			}
		});
	}


	/**
	 * Shut down all active computations.
	 * 
	 */
	private void killSubprocesses() {
		plannerThread.interrupt();
		plannerManagerProcess.destroy();
		_view.dispose();
	}


	/**
	 * Build the arguments list needed to launch Fast-Downward planner, tuned according to user selections. 
	 * Notice that, by default, the domain and problem files are not indicate and should be defined before running 
	 * the command.
	 * 
	 * @return an array of Strings containing the arguments.
	 * @throws IOException 
	 */
	private String[] buildFastDownardCommandArgs() throws IOException {
		ArrayList<String> commandComponents = new ArrayList<>();

		// determine which python interpreter must be used
		String pythonInterpreter = "python";
		if (SystemUtils.IS_OS_WINDOWS) {
			if (Utilities.is64bitsOS()) {
				pythonInterpreter = "python27amd64/" + pythonInterpreter;
			} else {
				pythonInterpreter = "python27/" + pythonInterpreter;
			}
		} 
//		else {
//			pythonInterpreter = "python27/linux-mac/" + pythonInterpreter;
//		}

		/* begin of command args for planner manager */

		commandComponents.add(pythonInterpreter);

		File plannerManagerScript = new File("planner_manager.py");
		commandComponents.add(plannerManagerScript.getCanonicalPath());


		/* begin of command args for Fast-Downward */

		commandComponents.add(pythonInterpreter);

		File fdScript = new File("fast-downward/fast-downward.py");
		commandComponents.add(fdScript.getCanonicalPath());

		// Fast-Downward is assumed to be built in advance both for 32 and 64 bits OS (being them Windows or Unix-like).
		commandComponents.add("--build");
		if (Utilities.is64bitsOS())
			commandComponents.add("release64");
		else
			commandComponents.add("release32");

		commandComponents.add("--plan-file");
		commandComponents.add(COMMAND_ARG_PLACEHOLDER);  // output file

		commandComponents.add(COMMAND_ARG_PLACEHOLDER);  // domain file
		commandComponents.add(COMMAND_ARG_PLACEHOLDER);  // problem file

		// insert heuristic and search strategy according to user selection
		if(Constants.getPlannerPerspective().getOptimalRadioButton().isSelected()) {
			commandComponents.add("--heuristic");
			commandComponents.add("hcea=cea()");
			commandComponents.add("--search");
			commandComponents.add("astar(blind())");
		}
		else if(Constants.getPlannerPerspective().getLazyGreedyRadioButton().isSelected()) {
			commandComponents.add("--heuristic");
			commandComponents.add("hhmax=hmax()");
			commandComponents.add("--search");
			commandComponents.add("lazy_greedy([hhmax], preferred=[hhmax])");
		}

		String[] commandArguments = commandComponents.toArray(new String[0]);
		return commandArguments;
	}

	/**
	 * Initialize the thread for planner execution.
	 * 
	 */
	private void initPlannerThread() {
		plannerThread = new Thread(new Runnable() {

			public void run() {
				try {

					PlannerPerspective plannerPerspective = Constants.getPlannerPerspective();

					// set traces id bounds
					if(plannerPerspective.getNumberOfTracesCheckBox().isSelected()) {
						traceIdToCheckFrom = plannerPerspective.getTraceIdComboBoxFROM().getSelectedIndex();
						traceIdToCheckTo = plannerPerspective.getTraceIdComboBoxTO().getSelectedIndex();
					}			
					else {
						traceIdToCheckFrom = 1;
						traceIdToCheckTo = Constants.getAllTracesVector().size();
					}

					// set traces length bounds
					if(plannerPerspective.getLenghtOfTracesCheckBox().isSelected()) {
						minTracesLength = new Integer(plannerPerspective.getLenghtOfTracesComboBoxFROM().getSelectedItem().toString());
						maxTracesLength = new Integer(plannerPerspective.getLenghtOfTracesComboBoxTO().getSelectedItem().toString());
					}			
					else {
						minTracesLength = Constants.getMinimumLengthOfATrace();
						maxTracesLength = Constants.getMaximumLengthOfATrace();
					}

					// display initial settings to user
					_view.showPlannerSettings();

					// cleanup folders
					File plansFoundDir = new File(PLANS_FOUND_DIR);
					File pddlFilesDir = new File(PDDL_FILES_DIR);
					Utilities.deleteFolderContents(plansFoundDir);
					Utilities.deleteFolderContents(pddlFilesDir);


					/* PLANNER INPUTS BUILDING */

					for(int traceId = traceIdToCheckFrom-1; traceId < traceIdToCheckTo; traceId++) {

						Trace trace = Constants.getAllTracesVector().elementAt(traceId);

						if(trace.getTraceLength() >= minTracesLength && trace.getTraceLength() <= maxTracesLength)  {

							// the trace matches the length bounds

							_view.appendToResults(">> GENERATING PDDL FOR ", Color.BLACK);
							_view.appendToResults(trace.getTraceName() + " ... ", Color.BLUE);

							if(Constants.isDiscardDuplicatedTraces()
									&& !Constants.getAllTracesHashtable().containsValue(trace.getTraceName())
									&& Constants.getAllTracesHashtable().containsKey(trace.getTraceTextualContent().toString()))  {

								// the trace is a duplicate of a previous one

								String otherTrace = Constants.getAllTracesHashtable().get(trace.getTraceTextualContent().toString());
								_view.appendToResults("SKIPPED: equivalent to "
										+ Constants.getAllTracesHashtable()
										.get(trace.getTraceTextualContent().toString()), Color.RED);
								duplicatedTracesHashtable.put(trace.getTraceName(), otherTrace);
							}
							else {

								// the trace is not a duplicate
								// create PDDL encodings (domain & problem) for current trace
								StringBuffer sbDomain = Utilities.createPropositionalDomain(trace);
								StringBuffer sbProblem = Utilities.createPropositionalProblem(trace);
								String sbDomainFileName = PDDL_DOMAIN_FILE_PREFIX + trace.getTraceNumber() + PDDL_EXT;
								String sbProblemFileName = PDDL_PROBLEM_FILE_PREFIX + trace.getTraceNumber() + PDDL_EXT;
								Utilities.writeFile(sbDomainFileName, sbDomain);
								Utilities.writeFile(sbProblemFileName, sbProblem);
							}

							_view.appendToResults("\n", Color.BLACK);
						}
					}



					/* PLANNER INVOCATION */

					_view.appendToResults("\n>> ALIGNMENT IN PROGRESS.......\n\n", Color.BLACK);					

					String[] commandArgs = buildFastDownardCommandArgs();

					// execute external planner script and wait for results
					ProcessBuilder processBuilder = new ProcessBuilder(commandArgs);
					plannerManagerProcess = processBuilder.start();

					//System.out.println(Arrays.toString(commandArgs));

					// read std out & err in separated thread
					StreamGobbler errorGobbler = new StreamGobbler(plannerManagerProcess.getErrorStream(), "ERROR");
					StreamGobbler outputGobbler = new StreamGobbler(plannerManagerProcess.getInputStream(), "OUTPUT");
					errorGobbler.start();
					outputGobbler.start();

					// wait for the process to return to read the generated outputs
					plannerManagerProcess.waitFor();



					/* PLANNER OUTPUTS PROCESSING */

					int traceIndex = 1;
					for(final File alignmentFile : plansFoundDir.listFiles()) {

						// extract traceId
						Matcher traceIdMatcher = decimalNumberRegexPattern.matcher(alignmentFile.getName());
						traceIdMatcher.find();
						int traceId = Integer.parseInt(traceIdMatcher.group());

						Trace trace = Constants.getAllTracesVector().elementAt(traceId - 1);

						// check execution results
						BufferedReader processOutputReader = new BufferedReader(new FileReader(alignmentFile));
						String outputLine = processOutputReader.readLine(); 
						if (outputLine == null) {

							// planner script failed unexpectedly
							_view.appendToResults("ATTENTION: A TRANSLATION ERROR HAS BEEN OBSERVED!\n", Color.RED);

							tracesWithFailureVector.addElement(trace.getTraceName());

						} else {		

							// read trace alignment cost from process output file
							String traceAlignmentCost = new String();
							String traceAlignmentTime = new String();
							while (outputLine != null) {

								// parse alignment cost
								if(outputLine.startsWith(COST_ENTRY_PREFIX)) {

									Matcher matcher = decimalNumberRegexPattern.matcher(outputLine);
									matcher.find();
									traceAlignmentCost = matcher.group();

									if(Integer.parseInt(traceAlignmentCost) > 0)
										alignedTracesAmount++;
								}

								// parse alignment time
								if(outputLine.startsWith(SEARCH_TIME_ENTRY_PREFIX)) {

									Matcher matcher = decimalNumberRegexPattern.matcher(outputLine);
									matcher.find();
									traceAlignmentTime = matcher.group();
								}

								outputLine = processOutputReader.readLine();
							}									

							// update UI with trace-related statistics
							_view.appendToResults(">> ", Color.BLACK);
							_view.appendToResults(trace.getTraceName(), Color.BLUE);
							_view.appendToResults(" ALIGNED IN ", Color.decode("#009933"));							
							_view.appendToResults(traceAlignmentTime + Constants.TIME_UNIT, Color.BLUE);
							_view.appendToResults(" WITH COST ", Color.BLACK);
							_view.appendToResults(traceAlignmentCost + "\n", Color.BLUE);

							// update total counters
							totalAlignmentCost += Float.parseFloat(traceAlignmentCost);
							totalAlignmentTime += Float.parseFloat(traceAlignmentTime);
						}
						processOutputReader.close();

						_view.getAlignedTracesCombobox().insertItemAt(trace.getTraceName(), traceIndex);
						traceIndex += 1;
					}

					_view.showPlannerResultsOverview();

					_view.getAlignedTracesCombobox().setEnabled(true);  
					_view.getGenerateAlignedEvLogButton().setEnabled(true);

				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}


	/* GETTERS & SETTERS */

	public Thread getPlannerThread() {
		return plannerThread;
	}

	public void setPlannerThread(Thread plannerThread) {
		this.plannerThread = plannerThread;
	}

	public Hashtable<String, String> getDuplicatedTracesHashtable() {
		return duplicatedTracesHashtable;
	}

	public void setDuplicatedTracesHashtable(Hashtable<String, String> duplicatedTracesHashtable) {
		this.duplicatedTracesHashtable = duplicatedTracesHashtable;
	}

	public Vector<String> getTracesWithFailureVector() {
		return tracesWithFailureVector;
	}

	public void setTracesWithFailureVector(Vector<String> tracesWithFailureVector) {
		this.tracesWithFailureVector = tracesWithFailureVector;
	}

	public int getTraceIdToCheckFrom() {
		return traceIdToCheckFrom;
	}

	public void setTraceIdToCheckFrom(int traceIdToCheckFrom) {
		this.traceIdToCheckFrom = traceIdToCheckFrom;
	}

	public int getTraceIdToCheckTo() {
		return traceIdToCheckTo;
	}

	public void setTraceIdToCheckTo(int traceIdToCheckTo) {
		this.traceIdToCheckTo = traceIdToCheckTo;
	}

	public int getMinTracesLength() {
		return minTracesLength;
	}

	public void setMinTracesLength(int minTracesLength) {
		this.minTracesLength = minTracesLength;
	}

	public int getMaxTracesLength() {
		return maxTracesLength;
	}

	public void setMaxTracesLength(int maxTracesLength) {
		this.maxTracesLength = maxTracesLength;
	}

	public int getAlignedTracesAmount() {
		return alignedTracesAmount;
	}

	public void setAlignedTracesAmount(int alignedTracesAmount) {
		this.alignedTracesAmount = alignedTracesAmount;
	}

	public float getTotalAlignmentTime() {
		return totalAlignmentTime;
	}

	public void setTotalAlignmentTime(float totalAlignmentTime) {
		this.totalAlignmentTime = totalAlignmentTime;
	}

	public float getTotalAlignmentCost() {
		return totalAlignmentCost;
	}

	public void setTotalAlignmentCost(float totalAlignmentCost) {
		this.totalAlignmentCost = totalAlignmentCost;
	}

	public Process getPlannerManagerProcess() {
		return plannerManagerProcess;
	}

	public void setPlannerManagerProcess(Process plannerManagerProcess) {
		this.plannerManagerProcess = plannerManagerProcess;
	}

}