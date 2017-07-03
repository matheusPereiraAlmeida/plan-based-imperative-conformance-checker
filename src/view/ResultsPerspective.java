package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
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

	private Hashtable<String, String> alignmentRequiredForTracesHashtable = new Hashtable<String, String>();

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

		this.invokePlanner(this);

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

	private void invokePlanner(ResultsPerspective resP) {

		final ResultsPerspective resultsPerspective = resP;

		plannerThread = new Thread(new Runnable() {

			public void run() {
				try {        

					alignedTracesAmount = 0;              	  

					if(Constants.getPlannerPerspective().getNumberOfTracesCheckBox().isSelected()) {
						traceIdToCheckFrom = Constants.getPlannerPerspective().getTraceIdComboBoxFROM().getSelectedIndex();
						traceIdToCheckTo = Constants.getPlannerPerspective().getTraceIdComboBoxTO().getSelectedIndex();
					}			
					else {
						traceIdToCheckFrom = 1;
						traceIdToCheckTo = Constants.getAllTracesVector().size();
					}

					if(Constants.getPlannerPerspective().getLenghtOfTracesCheckBox().isSelected()) {
						minTracesLength = new Integer(Constants.getPlannerPerspective().getLenghtOfTracesComboBoxFROM().getSelectedItem().toString());
						maxTracesLength = new Integer(Constants.getPlannerPerspective().getLenghtOfTracesComboBoxTO().getSelectedItem().toString());
					}			
					else {
						minTracesLength = Constants.getMinimumLengthOfATrace();
						maxTracesLength = Constants.getMaximumLengthOfATrace();
					}

					StyleContext context = new StyleContext();
					Style style = context.addStyle("test", null);     

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

					totalAlignmentCost = 0;
					totalAlignmentTime = 0;	            

					File plans_found_folder = new File("fast-downward/plans_found");
					File conformance_checking_folder = new File("fast-downward/Conformance_Checking");
					File output_file = new File("fast-downward/sas_plan");
					Utilities.deleteFolderContents(plans_found_folder);
					Utilities.deleteFolderContents(conformance_checking_folder);

					
					// create a pointer to external planner script (according to user heuristic selection)
					File runScript = null;
					if(Constants.getPlannerPerspective().getOptimalRadioButton().isSelected())  {
						runScript = new File("run_FD_optimal_blind_Astar");
					}
					else if(Constants.getPlannerPerspective().getLazyGreedyRadioButton().isSelected())  {
						runScript = new File("run_FD_suboptimal_hmax");
					}

					
					int traceIndex = 1;
					for(int traceId = traceIdToCheckFrom-1; traceId < traceIdToCheckTo; traceId++) {	 

						boolean failureFound = false;
						Trace trace = Constants.getAllTracesVector().elementAt(traceId);

						if(trace.getTraceLength() >= minTracesLength && trace.getTraceLength() <= maxTracesLength)  {		

							StyleConstants.setForeground(style, Color.BLACK);
							resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> ALIGNING ", style);
							StyleConstants.setForeground(style, Color.BLUE);
							resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), trace.getTraceName() + " ... ", style);	 


							if(Constants.isDiscardDuplicatedTraces()
									&& !Constants.getAllTracesHashtable().containsValue(trace.getTraceName())
									&& Constants.getAllTracesHashtable().containsKey(trace.getTrace_textual_content().toString()))  {  	            

								String other_trace = Constants.getAllTracesHashtable().get(trace.getTrace_textual_content().toString());

								StyleConstants.setForeground(style, Color.RED);   	        
								resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "SKIPPED: equivalent to " + Constants.getAllTracesHashtable().get(trace.getTrace_textual_content().toString()) + "\n", style);	 
								duplicatedTracesHashtable.put(trace.getTraceName(), other_trace);   	   	            
							}
							else {

								// create PDDL encodings (domain & problem) for current trace
								StringBuffer sb_domain = Utilities.createPropositionalDomain(trace);
								StringBuffer sb_problem = Utilities.createPropositionalProblem(trace);
								Utilities.writeFile("fast-downward/Conformance_Checking/domain" + trace.getTraceNumber() + ".pddl", sb_domain);
								Utilities.writeFile("fast-downward/Conformance_Checking/problem" + trace.getTraceNumber() + ".pddl", sb_problem);	 

								
								// execute external planner script and wait for results
								String cmd[] = new String[] {runScript.getCanonicalPath(), trace.getTraceNumber()};
								Process pr = Runtime.getRuntime().exec(cmd);
								pr.waitFor();

								
								// check whether planner script failed unexpectedly
								BufferedReader br = new BufferedReader(new FileReader(output_file));     
								if (br.readLine() == null) {
									StyleConstants.setForeground(style, Color.RED);
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "ATTENTION: A TRANSLATION ERROR HAS BEEN OBSERVED!\n", style);	 
									tracesWithFailureNumber++;
									tracesWithFailureVector.addElement(trace.getTraceName());
									failureFound = true;
								}
								br.close();

								if(!failureFound) {

									String trace_alignment_cost = new String();  
									String trace_alignment_time = new String();  

									File alignment_file = new File("fast-downward/plans_found/alignment_" + trace.getTraceNumber());

									BufferedReader br2 = new BufferedReader(new FileReader(alignment_file)); 
									String line;
									while ((line = br2.readLine()) != null) {

										if(line.startsWith("; cost = ")) {
											trace_alignment_cost = line.replace("; cost = ", "");
											trace_alignment_cost = trace_alignment_cost.replace(" (general cost)", "");
											if(Integer.parseInt(trace_alignment_cost) > 0)
												alignedTracesAmount++;

										}
										else if(line.startsWith("; searchtime = "))  {
											trace_alignment_time = line.replace("; searchtime = ", "");
										}

										//System.out.println(line);
									}
									br2.close();	

									StyleConstants.setForeground(style, Color.decode("#009933"));
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "ALIGNED IN ", style);	 
									StyleConstants.setForeground(style, Color.BLUE);
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), trace_alignment_time + " ms.", style);
									StyleConstants.setForeground(style, Color.BLACK);
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), " WITH COST ", style);
									StyleConstants.setForeground(style, Color.BLUE);
									resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), trace_alignment_cost + "\n", style);

									totalAlignmentTime += Float.parseFloat(trace_alignment_time);
									totalAlignmentCost += Float.parseFloat(trace_alignment_cost);

								}

								/*
             	     			resultsPerspective.createResults(trace,"Fast-Downward");            	     
								 */
							}


							resultsPerspective.getAlignedTracesComboBox().insertItemAt(trace.getTraceName(), traceIndex);
							traceIndex += 1;

						}

					}

					resultsPerspective.getAlignedTracesComboBox().setEnabled(true);  

					int total_number_of_traces = traceIdToCheckTo - traceIdToCheckFrom + 1;
					int total_number_of_traces_analyzed = total_number_of_traces - tracesWithFailureNumber;

					StyleConstants.setForeground(style, Color.decode("#009933"));
					StyleConstants.setBold(style, true);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "\n--- RESULTS OF THE ALIGNMENT ---\n", style);
					StyleConstants.setBold(style, false);

					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "\n>> NUMBER OF TRACES ANALYZED = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), total_number_of_traces_analyzed + "\n", style); 

					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> NUMBER OF TRACES REPAIRED = ", style);
					StyleConstants.setForeground(style, Color.RED);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), alignedTracesAmount+"\n", style); 


					//StyleConstants.setForeground(style, Color.BLACK);
					//resultsPerspective.getResultDocument().insertString(rp.getResultDocument().getLength(), ">> NUMBER OF TRACES NOT ANALYZED (due to a planner failure) = ", style);
					//StyleConstants.setForeground(style, Color.RED);
					//resultsPerspective.getResultDocument().insertString(rp.getResultDocument().getLength(), number_of_traces_with_failure + "\n", style); 
					//if(number_of_traces_with_failure>0) 
					// resultsPerspective.getResultsArea().append("\nLIST OF TRACES NOT ANALYZED (due a planner failure) : " + traces_with_failure_vector + "\n");

					//log_fitness = total_number_of_traces_analyzed - number_of_traces_aligned;
					//log_fitness = log_fitness / total_number_of_traces_analyzed;

					//StyleConstants.setForeground(style, Color.BLACK);
					//resultsPerspective.getResultDocument().insertString(rp.getResultDocument().getLength(), "\n>> LOG FITNESS = ", style);
					//StyleConstants.setForeground(style, Color.BLUE);
					//resultsPerspective.getResultDocument().insertString(rp.getResultDocument().getLength(),  log_fitness + "\n", style);

					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "\n>> TOTAL ALIGNMENT TIME = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAlignmentTime + " ms.\n", style); 
					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> AVG ALIGNMENT TIME = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAlignmentTime/total_number_of_traces_analyzed + " ms.\n", style); 


					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), "\n>> TOTAL ALIGNMENT COST = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAlignmentCost + "\n", style); 
					StyleConstants.setForeground(style, Color.BLACK);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), ">> AVG ALIGNMENT COST = ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					resultsPerspective.getResultDocument().insertString(resultsPerspective.getResultDocument().getLength(), totalAlignmentCost/total_number_of_traces_analyzed + "\n", style);  

					resultsPerspective.generateAlignedEvLogButton.setEnabled(true);

				}
				catch(Exception e)
				{e.printStackTrace();
				}

			}
		});
		plannerThread.start();
	}


	public void createResults(Trace trace, String planner_name) {

		//
		//Create the results to be shown by the planner
		//
		if(planner_name.equalsIgnoreCase("fast-downward"))
			new File("fast-downward/src/sas_plan");
		else if(planner_name.equalsIgnoreCase("LPG"))
			new File("LPG/solution");

		Vector<String> plan_vector = new Vector<String>();

		if(planner_name.equalsIgnoreCase("fast-downward")) {
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader("fast-downward/src/sas_plan"));
				String line = reader.readLine();
				while(line!=null) {
					if(line!=null && line.contains("(") && line.contains(")")){
						plan_vector.addElement(line);
					}
					line = reader.readLine();
				}
				reader.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		/*
        else if(planner_name.equalsIgnoreCase("LPG")) {
    	       try
     	    	 {
  			 BufferedReader reader = new BufferedReader(new FileReader("LPG/solution"));
  			 String line = reader.readLine();
  			 while(line!=null) {
  				 if(line!=null && line.contains("(") && line.contains(")")){
  			    	 plan_vector.addElement(line.substring(line.indexOf("("),line.indexOf(")") + 1).toLowerCase());
  			     }
  			     line = reader.readLine();
  			 }
     	    	}
             catch(Exception e)
     	    {e.printStackTrace();
     	    }
          }
		 */
		/////////////////////////////////////////

		StringBuffer logBuffer = new StringBuffer();
		logBuffer.append(">>>> ORIGINAL TRACE: " + trace.getTraceContentVector()+"\n");

		//Vector<String> intermediate_trace_vector = new Vector<String>(trace.getTraceContentVector()); 

		//double trace_fitness = 0.0;
		//int trace_alignment_cost = 0;
		//int trace_reference_alignment_cost = 0;

		int number_of_alignments = 0;

		for (int index=0;index<plan_vector.size();index++) {

			int indice = index+1;

			if(index==0) {
				//	resultsArea.append("###############################\n");
				logBuffer.append("###############################\n");
			}		
			String planning_action = (String) plan_vector.elementAt(index);

			String[] split = planning_action.split("\\(");
			String[] split1 = split[1].split("\\)");

			String[] complete_action = split1[0].split(" ");

			String action = complete_action[0];

			if(action.contains("movelog")) {
				//resultsArea.append(">>>> PLANNING ACTION # " + indice + ": " + action + "\n");
				logBuffer.append(">>>> PLANNING ACTION # " + indice + ": " + action + "\n");
			}
			else {
				//	resultsArea.append(">>>> PLANNING ACTION # " + indice + ": " + action + "\n");
				logBuffer.append(">>>> PLANNING ACTION # " + indice + ": " + action + "\n");	
			}

			if(action.contains("moveinthemodel-") || action.contains("moveinthelog")) {
				number_of_alignments++;
			}
		}

		if(number_of_alignments>0) {

			if(!alignmentRequiredForTracesHashtable.containsKey(trace.getTraceName())) {
				alignmentRequiredForTracesHashtable.put(trace.getTraceName(),"true");
				alignedTracesAmount++;
			}
			else {
				if(alignmentRequiredForTracesHashtable.get(trace.getTraceName()).equalsIgnoreCase("false")) {
					alignmentRequiredForTracesHashtable.put(trace.getTraceName(),"true");
					alignedTracesAmount++;
				}
			}
		}

		logBuffer.append("###############################\n\n");

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

//	private void scriviFile(String nomeFile, StringBuffer buffer) {
//
//		File file = null;
//		FileWriter fw = null;
//
//		try {
//			file = new File(nomeFile);
//			file.setExecutable(true);
//
//			fw = new FileWriter(file);
//			fw.write(buffer.toString());
//			fw.close();
//
//			//fw.flush();
//			//fw.close();
//		}
//		catch(IOException e) {
//			e.printStackTrace();
//		}
//	}

}