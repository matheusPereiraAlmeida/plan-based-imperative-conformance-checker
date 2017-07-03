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
import java.util.Vector;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.Constants;
import main.Trace;
import main.Utilities;
import view.ResultsPerspective;

public class H_ResultsPerspective {

	public ResultsPerspective _view = null;

	public H_ResultsPerspective (ResultsPerspective i_view){
		_view = i_view;
		installListeners();
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
				_view.getPlannerThread().interrupt();    
				_view.dispose();
			}
		});

		_view.getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_view.getPlannerThread().interrupt();           	
				_view.dispose();
			}
		});


		_view.getAlignedTracesComboBox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {

				String selected_aligned_trace_name = (String) _view.getAlignedTracesComboBox().getSelectedItem();

				if (event.getStateChange() == ItemEvent.SELECTED && !(selected_aligned_trace_name.equalsIgnoreCase("Event Log")) ) {						
					_view.resetResultsPane();

					String number_of_selected_trace = selected_aligned_trace_name.replace("Trace#", "");

					try {

						StyleContext context = new StyleContext();
						Style style = context.addStyle("test", null);   

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> EVENT LOG FILE = ", style);
						StyleConstants.setForeground(style, Color.RED);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), Constants.getEventLogFileName(), style);
						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), " (" + selected_aligned_trace_name + ")\n" , style);

						String trace_alignment_cost = new String();  
						String trace_alignment_time = new String();  
						Vector<String> PDDL_alignment_moves_vector = new Vector<String>();

						File alignment_file = null;

						if(_view.getDuplicatedTracesHashtable().containsKey(selected_aligned_trace_name)) {

							String number_of_identical_trace = _view.getDuplicatedTracesHashtable().get(selected_aligned_trace_name).replace("Trace#", "");
							alignment_file = new File("fast-downward/plans_found/alignment_" + number_of_identical_trace);

							StyleConstants.setForeground(style, Color.MAGENTA);
							_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> SKIPPED! IDENTICAL TO ", style);
							_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "Trace#" + number_of_identical_trace + "\n", style);


						}
						else {
							alignment_file = new File("fast-downward/plans_found/alignment_" + number_of_selected_trace);
						}

						BufferedReader br2;

						br2 = new BufferedReader(new FileReader(alignment_file));
						String line;

						while ((line = br2.readLine()) != null) {


							if(line.startsWith("; cost = ")) {

								trace_alignment_cost = line.replace("; cost = ", "");
								trace_alignment_cost = trace_alignment_cost.replace(" (general cost)", "");

							}
							else if(line.startsWith("; searchtime = "))  {

								trace_alignment_time = line.replace("; searchtime = ", "");
							}
							else {

								PDDL_alignment_moves_vector.addElement(line);
							}
						}
						br2.close();

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> SEARCH ALGORITHM = ", style);
						StyleConstants.setForeground(style, Color.BLUE);

						if(Constants.getPlannerPerspective().getOptimalRadioButton().isSelected())
							_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "Blind A* (Cost-Optimal) \n", style);
						else
							_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "Lazy Greedy (Suboptimal) \n", style);

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> ALIGNMENT TIME = ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), trace_alignment_time + " ms.\n", style); 

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> ALIGNMENT COST = ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), trace_alignment_cost + "\n", style); 

						StyleConstants.setBold(style, true);
						StyleConstants.setForeground(style, Color.decode("#009933"));
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "\n--- ALIGNED TRACE ---\n\n", style);

						StyleConstants.setBold(style, false);

						for(int k=0;k<PDDL_alignment_moves_vector.size();k++) {							

							String align_move = PDDL_alignment_moves_vector.elementAt(k);

							if(align_move.startsWith("(movesync#")) {

								align_move = align_move.replace("(movesync#", "");
								align_move = align_move.substring(0,align_move.lastIndexOf("#"));

								StyleConstants.setStrikeThrough (style,false);
								StyleConstants.setForeground(style, Color.BLACK);
								_view.getResultDocument().insertString(_view.getResultDocument().getLength(), align_move + "\n", style);

							}
							else if(align_move.startsWith("(moveinthemodel#")) {

								align_move = align_move.replace("(moveinthemodel#", "");
								align_move = align_move.substring(0,align_move.lastIndexOf(" )"));

								StyleConstants.setStrikeThrough (style,false);
								StyleConstants.setForeground(style, Color.BLUE);
								_view.getResultDocument().insertString(_view.getResultDocument().getLength(), align_move , style);

								if(Constants.getPlannerPerspective().getCostCheckBox().isSelected()) {
									StyleConstants.setItalic(style,true);
									StyleConstants.setForeground(style, Color.BLACK);
									_view.getResultDocument().insertString(_view.getResultDocument().getLength(), " [cost " + Utilities.getCostOfActivity(align_move, "move_in_the_model") + "]", style);
									StyleConstants.setItalic(style,false);
								}

								_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "\n", style);

							}
							else if(align_move.startsWith("(moveinthelog#")) {

								align_move = align_move.replace("(moveinthelog#", "");
								align_move = align_move.substring(0,align_move.indexOf("#"));				  	   	    		

								StyleConstants.setStrikeThrough (style,true);
								StyleConstants.setForeground(style, Color.RED);
								_view.getResultDocument().insertString(_view.getResultDocument().getLength(), align_move, style);

								if(Constants.getPlannerPerspective().getCostCheckBox().isSelected()) {
									StyleConstants.setStrikeThrough (style,false);
									StyleConstants.setItalic(style,true);
									StyleConstants.setForeground(style, Color.BLACK);
									_view.getResultDocument().insertString(_view.getResultDocument().getLength(), " [cost " + Utilities.getCostOfActivity(align_move, "move_in_the_log") + "]", style);
									StyleConstants.setItalic(style,false);
								}

								_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "\n", style);
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

					_view.getResultsPane().setCaretPosition(0);
				}	

				else if(event.getStateChange() == ItemEvent.SELECTED && (selected_aligned_trace_name.equalsIgnoreCase("Event Log"))) {

					try {

						_view.resetResultsPane();

						StyleContext context = new StyleContext();
						Style style = context.addStyle("test", null);     

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> EVENT LOG FILE = ", style);
						StyleConstants.setForeground(style, Color.RED);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), Constants.getEventLogFileName() + "\n", style);

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> SEARCH ALGORITHM = ", style);
						StyleConstants.setForeground(style, Color.BLUE);

						if(Constants.getPlannerPerspective().getOptimalRadioButton().isSelected())
							_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "Blind A* (Cost-Optimal) \n", style);
						else
							_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "Lazy Greedy (Suboptimal) \n", style);

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> SIZE OF THE EVENT LOG = ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), Constants.getAllTracesVector().size() + "\n", style);

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> ANALYZE FROM ", style);	            
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "trace#" + _view.getTraceIdToCheckFrom(), style);	      
						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), " TO ", style);	
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "trace#" + _view.getTraceIdToCheckTo() + "\n", style);	      

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> DISCARD DUPLICATED TRACES = ", style);	      

						if(Constants.isDiscardDuplicatedTraces()) {
							StyleConstants.setForeground(style, Color.RED);
							_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "YES \n", style);	         
						}
						else {
							StyleConstants.setForeground(style, Color.BLUE);
							_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "NO \n", style); 
						}

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> ANALYZE TRACES IN THE LOG HAVING LENGHT BETWEEN ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), _view.getMinTracesLength() + "", style);	      
						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), " AND ", style);	
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), _view.getMaxTracesLength() + "\n\n", style);	      


						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> RECAP OF THE ALIGNMENT\n\n", style);	      

						for(int k=_view.getTraceIdToCheckFrom()-1;k<_view.getTraceIdToCheckTo();k++) {

							Trace trace = Constants.getAllTracesVector().elementAt(k);

							if(trace.getTraceLength() >= _view.getMinTracesLength() && trace.getTraceLength() <= _view.getMaxTracesLength())  {		

								StyleConstants.setForeground(style, Color.BLACK);
								_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> ALIGNING ", style);
								StyleConstants.setForeground(style, Color.BLUE);
								_view.getResultDocument().insertString(_view.getResultDocument().getLength(), trace.getTraceName() + " ... ", style);	 


								if(Constants.isDiscardDuplicatedTraces() && !Constants.getAllTracesHashtable().containsValue(trace.getTraceName()) && Constants.getAllTracesHashtable().containsKey(trace.getTrace_textual_content().toString()))  {  	            


									StyleConstants.setForeground(style, Color.RED);   	        
									_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "SKIPPED: equivalent to " + Constants.getAllTracesHashtable().get(trace.getTrace_textual_content().toString()) + "\n", style);	    	            
								}
								else {

									String trace_alignment_cost = new String();  
									String trace_alignment_time = new String();  

									File alignment_file = new File("fast-downward/plans_found/alignment_" + trace.getTraceNumber());

									BufferedReader br2 = new BufferedReader(new FileReader(alignment_file)); 
									String line;
									while ((line = br2.readLine()) != null) {

										if(line.startsWith("; cost = ")) {
											trace_alignment_cost = line.replace("; cost = ", "");
											trace_alignment_cost = trace_alignment_cost.replace(" (general cost)", "");
										}
										else if(line.startsWith("; searchtime = "))  {
											trace_alignment_time = line.replace("; searchtime = ", "");
										}
										//System.out.println(line);
									}
									br2.close();	

									StyleConstants.setForeground(style, Color.decode("#009933"));
									_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "ALIGNED IN ", style);	 
									StyleConstants.setForeground(style, Color.BLUE);
									_view.getResultDocument().insertString(_view.getResultDocument().getLength(), trace_alignment_time + " ms.", style);
									StyleConstants.setForeground(style, Color.BLACK);
									_view.getResultDocument().insertString(_view.getResultDocument().getLength(), " WITH COST ", style);
									StyleConstants.setForeground(style, Color.BLUE);
									_view.getResultDocument().insertString(_view.getResultDocument().getLength(), trace_alignment_cost + "\n", style);

								}
							}
						}

						int total_number_of_traces = _view.getTraceIdToCheckTo() - _view.getTraceIdToCheckFrom() + 1;
						int total_number_of_traces_analyzed = total_number_of_traces;

						StyleConstants.setForeground(style, Color.decode("#009933"));
						StyleConstants.setBold(style, true);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "\n--- RESULTS OF THE ALIGNMENT ---\n", style);
						StyleConstants.setBold(style, false);

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "\n>> NUMBER OF TRACES ANALYZED = ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), total_number_of_traces_analyzed + "\n", style); 

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> NUMBER OF TRACES REPAIRED = ", style);
						StyleConstants.setForeground(style, Color.RED);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), _view.getAlignedTracesAmount()+"\n", style); 

						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "\n>> TOTAL ALIGNMENT TIME = ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), _view.getTotalAlignmentTime() + " ms.\n", style); 
						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> AVG ALIGNMENT TIME = ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), _view.getTotalAlignmentTime()/total_number_of_traces_analyzed + " ms.\n", style); 


						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), "\n>> TOTAL ALIGNMENT COST = ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), _view.getTotalAlignmentCost() + "\n", style); 
						StyleConstants.setForeground(style, Color.BLACK);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), ">> AVG ALIGNMENT COST = ", style);
						StyleConstants.setForeground(style, Color.BLUE);
						_view.getResultDocument().insertString(_view.getResultDocument().getLength(), _view.getTotalAlignmentCost()/total_number_of_traces_analyzed + "\n", style);  

					}
					//catch (FileNotFoundException e) {
					//e.printStackTrace();
					//} 
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
}