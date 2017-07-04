package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import control.H_ResultsPerspective;
import main.Constants;
import main.Trace;
import main.Utilities;

public class ResultsPerspective extends JDialog {

	private static final long serialVersionUID = 1L;	

	private DefaultStyledDocument resultDocument;
	private JTextPane resultsArea;
	private JScrollPane resultsScrollPane;	

	private JComboBox<String> alignedTracesCombobox;
	private JButton okButton;
	private JButton generateAlignedEvLogButton;

	private JLabel resultsLabel;
	private Thread plannerThread;

	private float totalAlignmentTime;
	private float totalAlignmentCost;

	private int traceIdToCheckFrom = 0;
	private int traceIdToCheckTo = 0;
	private int minTracesLength = 0;
	private int maxTracesLength = 0;

	private Hashtable<String, String> duplicatedTracesHashtable = new Hashtable<String, String>();

	private int tracesWithFailureNumber = 0;
	private int alignedTracesAmount = 0;
	private Vector<String> tracesWithFailureVector = new Vector<String>();

	//private double log_fitness;

	protected H_ResultsPerspective _handler;

	public ResultsPerspective() {
		super();
		initComponent();
		initHandler();
		this.setModal(true);
		this.setVisible(true);
	}

	public void initComponent() {		
		Container content = this.getContentPane();

		content.setLayout(new FlowLayout()); 	

		resultDocument = new DefaultStyledDocument();
		resultsArea = new JTextPane(resultDocument);
		resultsArea.setEditable(false);

		resultsScrollPane = new JScrollPane(resultsArea);
		resultsScrollPane.setPreferredSize(new Dimension(490,480));
		resultsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		resultsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);   

		resultsLabel = new JLabel("Alignment Process: ");
		resultsLabel.setPreferredSize(new Dimension(300,25));

		alignedTracesCombobox = new JComboBox<String>();
		alignedTracesCombobox.setPreferredSize(new Dimension(180,25));		
		alignedTracesCombobox.insertItemAt("Event Log", 0);
		alignedTracesCombobox.setSelectedIndex(0);
		alignedTracesCombobox.setEnabled(false);

		okButton = new JButton("OK");
		generateAlignedEvLogButton = new JButton("Get Aligned Log");
		generateAlignedEvLogButton.setEnabled(false);
		generateAlignedEvLogButton.setVisible(false);

		duplicatedTracesHashtable = new Hashtable<String,String>();

		this.add(resultsLabel);	
		this.add(alignedTracesCombobox);	
		this.add(resultsScrollPane);	

		this.add(okButton); 
		this.add(generateAlignedEvLogButton);

		this.invokePlanner();

		this.setTitle("Event Log Alignment");
		this.setSize(500, 600);
		this.setResizable(true);

		int width = this.getWidth();
		int height = this.getHeight();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width / 2) - (width / 2), (screenSize.height / 2) - (height / 2), width, height);
	}

	private void initHandler() {		
		_handler = new H_ResultsPerspective(this);
	}

	private void invokePlanner() {

		final ResultsPerspective resultsPerspective = this;

		plannerThread = new Thread(new Runnable() {

			public void run() {
				try {
					alignedTracesAmount = 0;              	  
					totalAlignmentCost = 0;
					totalAlignmentTime = 0;
					
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

					StyleContext context = new StyleContext();
					Style style = context.addStyle("test", null);     

					// display initial settings to user
					showPlannerSettings(style, resultsPerspective);


					File plansFoundFolder = new File("fast-downward/plans_found");
					File conformanceCheckingFolder = new File("fast-downward/Conformance_Checking");
					Utilities.deleteFolderContents(plansFoundFolder);
					Utilities.deleteFolderContents(conformanceCheckingFolder);


					// create a pointer to external planner script (according to user heuristic selection)
					//File runScript = null;					
					String[] command = buildFastDownardCommandArguments();

					int traceIndex = 1;
					for(int traceId = traceIdToCheckFrom-1; traceId < traceIdToCheckTo; traceId++) {	 

						Trace trace = Constants.getAllTracesVector().elementAt(traceId);

						if(trace.getTraceLength() >= minTracesLength && trace.getTraceLength() <= maxTracesLength)  {		

							StyleConstants.setForeground(style, Color.BLACK);
							resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> ALIGNING ", style);
							StyleConstants.setForeground(style, Color.BLUE);
							resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), trace.getTraceName() + " ... ", style);	 


							if(Constants.isDiscardDuplicatedTraces()
									&& !Constants.getAllTracesHashtable().containsValue(trace.getTraceName())
									&& Constants.getAllTracesHashtable().containsKey(trace.getTrace_textual_content().toString()))  {  	            

								String otherTrace = Constants.getAllTracesHashtable().get(trace.getTrace_textual_content().toString());

								StyleConstants.setForeground(style, Color.RED);   	        
								resultsPerspective.getResultDocument().insertString(
										resultsPerspective.getResultDocument().getLength(),
										"SKIPPED: equivalent to " + Constants.getAllTracesHashtable()
											.get(trace.getTrace_textual_content().toString()) + "\n",
										style);	 
								duplicatedTracesHashtable.put(trace.getTraceName(), otherTrace);   	   	            
							}
							else {
								Pattern decimalNumberRegex = Pattern.compile("\\d+(,\\d{3})*(\\.\\d+)*");

								// create PDDL encodings (domain & problem) for current trace
								StringBuffer sbDomain = Utilities.createPropositionalDomain(trace);
								StringBuffer sbProblem = Utilities.createPropositionalProblem(trace);

								String sbDomainFileName = "fast-downward/Conformance_Checking/domain" + trace.getTraceNumber() + ".pddl";
								String sbProblemFileName = "fast-downward/Conformance_Checking/problem" + trace.getTraceNumber() + ".pddl";

								File sbDomainFile = Utilities.writeFile(sbDomainFileName, sbDomain);
								File sbProblemFile = Utilities.writeFile(sbProblemFileName, sbProblem);

								// create output file
								File alignmentFile = new File("fast-downward/plans_found/alignment_" + trace.getTraceNumber());

								// execute external planner script and wait for results
								command[5] = alignmentFile.getCanonicalPath();
								command[6] = sbDomainFile.getCanonicalPath();
								command[7] = sbProblemFile.getCanonicalPath();								
								ProcessBuilder processBuilder = new ProcessBuilder(command);
								Process process = processBuilder.start();

								//System.out.println(Arrays.toString(command));

								// read trace alignment time from process std out
								BufferedReader processStdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
								String traceAlignmentTime = new String();
								String stdOutLine;
								while ((stdOutLine = processStdOutReader.readLine()) != null) {
									//System.out.println(stdOutLine);

									if(stdOutLine.startsWith("Total time: ")) {
										// parse alignment total time
										Matcher m = decimalNumberRegex.matcher(stdOutLine);
										m.find();
										traceAlignmentTime = m.group();
									}
								}
								processStdOutReader.close();

								// wait for the process to return to read the generated outputs
								process.waitFor();

								// check execution results
								BufferedReader processOutputReader = new BufferedReader(new FileReader(alignmentFile));
								String outputLine = processOutputReader.readLine(); 
								if (outputLine == null) {

									// planner script failed unexpectedly
									StyleConstants.setForeground(style, Color.RED);
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "ATTENTION: A TRANSLATION ERROR HAS BEEN OBSERVED!\n", style);	 
									tracesWithFailureNumber++;
									tracesWithFailureVector.addElement(trace.getTraceName());

								} else {		

									// read trace alignment cost from process output file
									String traceAlignmentCost = new String();  
									while (outputLine != null) {

										// parse alignment cost
										if(outputLine.startsWith("; cost = ")) {

											// parse alignment total cost
											Matcher m = decimalNumberRegex.matcher(outputLine);
											m.find();
											traceAlignmentCost = m.group();

											if(Integer.parseInt(traceAlignmentCost) > 0)
												alignedTracesAmount++;
										}

										outputLine = processOutputReader.readLine();
									}									

									// append alignment time to result file
									String timeEntry = "; searchtime = " + traceAlignmentTime;
									Files.write(Paths.get(alignmentFile.getCanonicalPath()), timeEntry.getBytes(), StandardOpenOption.APPEND);

									// update UI with trace-related stats
									StyleConstants.setForeground(style, Color.decode("#009933"));
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "ALIGNED IN ", style);	 
									StyleConstants.setForeground(style, Color.BLUE);
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), traceAlignmentTime + " s.", style);
									StyleConstants.setForeground(style, Color.BLACK);
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), " WITH COST ", style);
									StyleConstants.setForeground(style, Color.BLUE);
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), traceAlignmentCost + "\n", style);

									// update total counters
									totalAlignmentCost += Float.parseFloat(traceAlignmentCost);
									totalAlignmentTime += Float.parseFloat(traceAlignmentTime);
								}
								processOutputReader.close();

							}
							resultsPerspective.getAlignedTracesComboBox().insertItemAt(trace.getTraceName(), traceIndex);
							traceIndex += 1;
						}
					}

					resultsPerspective.getAlignedTracesComboBox().setEnabled(true);  

					int totalTracesNumber = traceIdToCheckTo - traceIdToCheckFrom + 1;
					int totalAnalyzedTracesNumber = totalTracesNumber - tracesWithFailureNumber;

					StyleConstants.setForeground(style, Color.decode("#009933"));
					StyleConstants.setBold(style, true);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "\n--- RESULTS OF THE ALIGNMENT ---\n", style);
					StyleConstants.setBold(style, false);

					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "\n>> NUMBER OF TRACES ANALYZED = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAnalyzedTracesNumber + "\n", style); 

					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> NUMBER OF TRACES REPAIRED = ", style);
					StyleConstants.setForeground(style, Color.RED);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), alignedTracesAmount+"\n", style); 

					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "\n>> TOTAL ALIGNMENT TIME = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAlignmentTime + " s.\n", style); 
					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> AVG ALIGNMENT TIME = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAlignmentTime/totalAnalyzedTracesNumber + " s.\n", style); 


					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "\n>> TOTAL ALIGNMENT COST = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAlignmentCost + "\n", style); 
					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> AVG ALIGNMENT COST = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAlignmentCost/totalAnalyzedTracesNumber + "\n", style);  

					resultsPerspective.generateAlignedEvLogButton.setEnabled(true);

				}
				catch(Exception e){
					e.printStackTrace();
				}

			}
		});
		plannerThread.start();
	}


	/**
	 * Build the arguments list needed to launch Fast-Downard planner, tuned according to user selections. 
	 * Notice that, by default, the domain and problem files are not indicate and should be defined before running 
	 * the command.
	 * 
	 * @return an array of Strings containing the arguments.
	 * @throws IOException 
	 */
	public String[] buildFastDownardCommandArguments() throws IOException {
		ArrayList<String> commandComponents = new ArrayList<>();
		commandComponents.add("python");

		File fdScript = new File("fast-downward/fast-downward.py");
		commandComponents.add(fdScript.getCanonicalPath());

		// Fast-Downard is assumed to be built in advance both for 32 and 64 bits OS (being them Windows or Unix-like).
		commandComponents.add("--build");
		if (Utilities.is64bitsOS())
			commandComponents.add("release64");
		else
			commandComponents.add("release32");

		commandComponents.add("--plan-file");
		commandComponents.add("");  // output file placeholder

		commandComponents.add("");  // domain file placeholder
		commandComponents.add("");  // problem file placeholder

		// insert heuristic and search strategy according to user selection
		if(Constants.getPlannerPerspective().getOptimalRadioButton().isSelected()) {
			commandComponents.add("--heuristic");
			commandComponents.add("\"hcea=cea()\"");
			commandComponents.add("--search");
			commandComponents.add("\"astar(blind())\"");
		}
		else if(Constants.getPlannerPerspective().getLazyGreedyRadioButton().isSelected()) {
			commandComponents.add("--heuristic");
			commandComponents.add("\"hhmax=hmax()\"");
			commandComponents.add("--search");
			commandComponents.add("\"lazy_greedy([hhmax], preferred=[hhmax])\"");
		}

		String[] commandArguments = commandComponents.toArray(new String[0]);
		return commandArguments;
	}


	/**
	 * Display the chosen settings for planner execution to the user.
	 * 
	 * @param style
	 * @param resultsPerspective
	 * @throws BadLocationException
	 */
	private void showPlannerSettings(Style style, ResultsPerspective resultsPerspective) throws BadLocationException {

		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> EVENT LOG FILE = ", style);
		StyleConstants.setForeground(style, Color.RED);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), Constants.getEventLogFileName() + "\n", style);

		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> SEARCH ALGORITHM = ", style);
		StyleConstants.setForeground(style, Color.BLUE);

		if(Constants.getPlannerPerspective().getOptimalRadioButton().isSelected())
			resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "Blind A* (Cost-Optimal) \n", style);
		else
			resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "Lazy Greedy (Suboptimal) \n", style);

		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> SIZE OF THE EVENT LOG = ", style);
		StyleConstants.setForeground(style, Color.BLUE);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), Constants.getAllTracesVector().size() + "\n", style);

		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> ANALYZE FROM ", style);	            
		StyleConstants.setForeground(style, Color.BLUE);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "trace#" + traceIdToCheckFrom, style);	      
		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), " TO ", style);	
		StyleConstants.setForeground(style, Color.BLUE);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "trace#" + traceIdToCheckTo + "\n", style);	      

		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> DISCARD DUPLICATED TRACES = ", style);	      

		if(Constants.isDiscardDuplicatedTraces()) {
			StyleConstants.setForeground(style, Color.RED);
			resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "YES \n", style);	         
		}
		else {
			StyleConstants.setForeground(style, Color.BLUE);
			resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "NO \n", style); 
		}

		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> ANALYZE TRACES IN THE LOG HAVING LENGHT BETWEEN ", style);
		StyleConstants.setForeground(style, Color.BLUE);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), minTracesLength + "", style);	      
		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), " AND ", style);	
		StyleConstants.setForeground(style, Color.BLUE);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), maxTracesLength + "\n\n", style);	      

		StyleConstants.setForeground(style, Color.BLACK);
		resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> ALIGNMENT IN PROGRESS.......\n\n", style);
	}

	
	/* GETTERS & SETTERS */

	public JTextArea getResultsArea() {
		return new JTextArea();
	}

	public JTextPane getResultsPane() {
		return resultsArea;
	}

	public void resetResultsPane() {
		resultsArea.setText("");
	}

	public JButton getOkButton() {
		return okButton;
	}

	public float getTotalAlignmentTime() {
		return totalAlignmentTime;
	}

	public float getTotalAlignmentCost() {
		return totalAlignmentCost;
	}

	public int getTraceIdToCheckFrom() {
		return traceIdToCheckFrom;
	}

	public int getTraceIdToCheckTo() {
		return traceIdToCheckTo;
	}

	public int getMinTracesLength() {
		return minTracesLength;
	}

	public int getMaxTracesLength() {
		return maxTracesLength;
	}

	public int getAlignedTracesAmount() {
		return alignedTracesAmount;
	}

	public DefaultStyledDocument getResultDocument() {
		return resultDocument;
	}
	public Thread getPlannerThread() {
		return plannerThread;
	}

	public void setPlannerThread(Thread planner_thread) {
		this.plannerThread = planner_thread;
	}

	public JComboBox<String> getAlignedTracesComboBox() {
		return alignedTracesCombobox;
	}

	public void setAlignedTracesComboBox(JComboBox<String> aligned_traces_combobox) {
		this.alignedTracesCombobox = aligned_traces_combobox;
	}

	public Hashtable<String, String> getDuplicatedTracesHashtable() {
		return duplicatedTracesHashtable;
	}

	public void setDuplicatedTracesHashtable(Hashtable<String, String> duplicatedTracesHashtable) {
		this.duplicatedTracesHashtable = duplicatedTracesHashtable;
	}

}