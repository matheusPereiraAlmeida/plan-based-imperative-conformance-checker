package control;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.Constants;
import main.PetriNetTransition;
import main.Trace;
import view.PetriNetsPerspective;
import view.PlannerPerspective;

public class H_PetriNetsPerspective {

	private PetriNetsPerspective _view = null;

	public H_PetriNetsPerspective (PetriNetsPerspective i_view) {
		_view = i_view;
		installListeners();
	}

	private void installListeners() {

		_view.getPreviousStepButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Constants.getTracePerspective().setComponentEnabled(true);
				Constants.getPetriNetPerspective().setComponentEnabled(false);
				Constants.getMenuPerspective().getImportPetriNetMenuItem().setEnabled(false);
			}
		});


		_view.getNextStepButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {     

				PlannerPerspective ple = new PlannerPerspective();
				Constants.setPlannerPerspective(ple);

				// 1 -- Reset the costs of moving activities in the model/log
				//   -- Reset the marking vector
				//   -- Reset the vector containing the minimum and maximum length of the traces
				Constants.setActivitiesCostVector(new Vector<Vector<String>>());
				Constants.setPetriNetMarkingVector(new Vector<Vector<String>>());
				Constants.setMinimumLengthOfATrace(0);
				Constants.setMaximumLengthOfATrace(0);

				// 2 -- Reset the vector containing all activities (trace activities and Petri Net transitions)
				Constants.setAllActivitiesVector(new Vector<String>());

				// 3 -- Update the vector with the costs of all activities (in a first stage, just the cost of activities 
				//      occurring in the log) 
				//   -- Update the vector containing all activities	
				for(int i=0;i<Constants.getLogActivitiesRepositoryVector().size();i++) {

					String string = (String) Constants.getLogActivitiesRepositoryVector().elementAt(i);

					Constants.getAllActivitiesVector().addElement(string);

					Vector<String> v = new Vector<String>();
					v.addElement(string);
					v.addElement("1");
					v.addElement("1");
					Constants.getActivitiesCostVector().addElement(v);

				}

				// 4 -- Update the vector with the costs of all activity (in this second stage, with the cost of activities 
				//      corresponding to Petri Net transitions that do not appear in any log trace) 
				//   -- If a transition is KNOWN to be invisible, its costs are set to 0
				//   -- Update the vector containing all activities	
				for(int i=0;i<Constants.getAllTransitionsVector().size();i++) {
					PetriNetTransition ith_trans = Constants.getAllTransitionsVector().elementAt(i);

					if(!Constants.getLogActivitiesRepositoryVector().contains(ith_trans.getName())) {

						Constants.getAllActivitiesVector().addElement(ith_trans.getName());

						Vector<String> v = new Vector<String>();
						v.addElement(ith_trans.getName());


						if(ith_trans.getName().startsWith("generatedinv")) {
							v.addElement("0");
							v.addElement("0");
						}
						else {
							v.addElement("1");
							v.addElement("1");
						}

						Constants.getActivitiesCostVector().addElement(v);

					}
				}		    	

				//
				// 5 -- Update the ComboBox of the costs in the PlannerPerspective area
				//
				for(int kind=0;kind<Constants.getAllActivitiesVector().size();kind++) {
					String task = Constants.getAllActivitiesVector().elementAt(kind);
					ple.getCostComboBox().insertItemAt(task, kind+1);
				}

				// 6 -- Update the vector with the markings of all places 
				//   -- Update the ComboBox of the marking in the PlannerPerspective area

				for(int kind=0;kind<Constants.getAllPlacesVector().size();kind++) {

					String place_name = Constants.getAllPlacesVector().elementAt(kind);

					Vector<String> v = new Vector<String>();
					v.addElement(place_name);

					if(Constants.getPlacesInInitialMarkingVector().contains(place_name)) {
						v.addElement("1");
					}
					else v.addElement("0");

					if(Constants.getPlacesInFinalMarkingVector().contains(place_name)) {
						v.addElement("1");
					}
					else v.addElement("0");

					Constants.getPetriNetMarkingVector().addElement(v);

					ple.getMarkingComboBox().insertItemAt(place_name, kind+1);
				}


				//////////////////////////////////////////////////////////////////////////

				Constants.setAllTracesHashtable(new Hashtable<String,String>());

				StyleContext context = new StyleContext();
				Style style = context.addStyle("test", null);     

				// 7 -- Update the text area containing the traces in the PlannerPerspective panel

				int total_number_of_events = 0;
				Vector<String> ev_log_alphabet_vector = new Vector<String>();

				for(int j=0;j<Constants.getAllTracesVector().size();j++) {

					Trace trace = Constants.getAllTracesVector().elementAt(j);

					//ple.getTraceArea().append("****************\n");
					//ple.getTraceArea().append(trace.getTraceName() + "\n");
					//ple.getTraceArea().append("****************\n");

					/*
         			try {
         				 StyleConstants.setForeground(style, Color.BLUE);
         				 ple.getDocument().insertString(ple.getDocument().getLength(), "****************\n", style);
         				 ple.getDocument().insertString(ple.getDocument().getLength(), trace.getTraceName() + "\n", style);
         				 ple.getDocument().insertString(ple.getDocument().getLength(), "****************\n", style);
         			} catch (BadLocationException e) {
						e.printStackTrace();
					}
					 */

					trace.setTraceAlphabet(new Vector<String>());

					//
					// Update the length of the traces to be analyzed
					//
					int trace_length =  trace.getTraceContentVector().size();        			
					trace.setTraceLength(trace_length);

					total_number_of_events += trace_length;

					//System.out.println("MIN " + Constants.getMinimumLengthOfATrace());
					//System.out.println("MAX " + Constants.getMaximumLengthOfATrace());
					//System.out.println("CURRENT " + trace_length);

					if(j==0)  {
						Constants.setMinimumLengthOfATrace(trace_length);
					}         		
					if(Constants.getMinimumLengthOfATrace() > trace_length) {
						Constants.setMinimumLengthOfATrace(trace_length);
					}
					if(Constants.getMaximumLengthOfATrace() < trace_length) {
						Constants.setMaximumLengthOfATrace(trace_length);
					}

					for(int jind=0;jind<trace.getTraceContentVector().size();jind++) {

						String act = trace.getTraceContentVector().elementAt(jind);

						/*
         				StyleConstants.setForeground(style, Color.RED);
         				try {
         					//ple.getTraceArea().append(act + "\n");
         					ple.getDocument().insertString(ple.getDocument().getLength(), act + "\n", style);
         				} catch (BadLocationException e) {
							e.printStackTrace();
						}
						 */

						if(!ev_log_alphabet_vector.contains(act))
							ev_log_alphabet_vector.addElement(act);


						if(!trace.getTraceAlphabet().contains(act)) {
							trace.getTraceAlphabet().addElement(act);
						}

						if(!Constants.getAllTracesHashtable().containsKey(trace.getTraceTextualContent().toString()))  {
							Constants.getAllTracesHashtable().put(trace.getTraceTextualContent().toString(),trace.getTraceName());
						}
					}

					/*
         			try {
         			//ple.getTraceArea().append("\n");
         				ple.getDocument().insertString(ple.getDocument().getLength(), "\n", style);
            		} catch (BadLocationException e) {
						e.printStackTrace();
					}
					 */

					//////////////////////////////////////////////////////////////////////////////////////////////////

					//System.out.println("************************");
					//System.out.println("TRACE name : " + trace.getTraceName());
					//System.out.println("TRACE content : " + trace.getTraceContentVector());
					//System.out.println("TRACE alphabet : " + trace.getTraceAlphabet());
					//System.out.println("TRACE textual content : " + trace.getTrace_textual_content());

					////////////////////////////////
				}

				try {
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getDocument().insertString(ple.getDocument().getLength(), "Event Log file: ", style);
					StyleConstants.setForeground(style, Color.RED);
					ple.getDocument().insertString(ple.getDocument().getLength(), Constants.getEventLogFileName() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getDocument().insertString(ple.getDocument().getLength(), "Total number of traces: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getDocument().insertString(ple.getDocument().getLength(), Constants.getAllTracesVector().size() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getDocument().insertString(ple.getDocument().getLength(), "Total number of events: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getDocument().insertString(ple.getDocument().getLength(), total_number_of_events + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getDocument().insertString(ple.getDocument().getLength(), "Average trace length: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getDocument().insertString(ple.getDocument().getLength(), (float) total_number_of_events/Constants.getAllTracesVector().size() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getDocument().insertString(ple.getDocument().getLength(), "Max trace length: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getDocument().insertString(ple.getDocument().getLength(), Constants.getMaximumLengthOfATrace() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getDocument().insertString(ple.getDocument().getLength(), "Min trace length: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getDocument().insertString(ple.getDocument().getLength(), Constants.getMinimumLengthOfATrace() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getDocument().insertString(ple.getDocument().getLength(), "Number of activities involved: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getDocument().insertString(ple.getDocument().getLength(), ev_log_alphabet_vector.size() +"", style);

					StyleConstants.setForeground(style, Color.BLACK);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), "-- Petri Net file: ", style);
					StyleConstants.setForeground(style, Color.RED);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), Constants.getPetriNetFileName() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), "-- Number of Transitions: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), Constants.getAllTransitionsVector().size() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), "-- Number of Places: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), Constants.getAllPlacesVector().size() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), "-- Places with no incoming edges: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), Constants.getPlacesInInitialMarkingVector() + "\n", style);
					StyleConstants.setForeground(style, Color.BLACK);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), "-- Places with no outgoing edges: ", style);
					StyleConstants.setForeground(style, Color.BLUE);
					ple.getPetriNetDocument().insertString(ple.getPetriNetDocument().getLength(), Constants.getPlacesInFinalMarkingVector() + "", style);

				} catch (BadLocationException e) {
					e.printStackTrace();
				}

				//System.out.println("TRACE textual content : " + Constants.getAllTracesHashtable());

				ple.getTraceArea().setCaretPosition(0);
				ple.getPetriNetArea().setCaretPosition(0);

				int kix = 1;
				for(int lngtr=Constants.getMinimumLengthOfATrace();lngtr<=Constants.getMaximumLengthOfATrace();lngtr++) {	
					ple.getLenghtOfTracesComboBoxFROM().insertItemAt("" + lngtr, kix);
					ple.getLenghtOfTracesComboBoxTO().insertItemAt("" + lngtr, kix);
					kix++;
				}

				ple.getLenghtOfTracesComboBoxFROM().setSelectedIndex(0);
				ple.getLenghtOfTracesComboBoxTO().setSelectedIndex(0);

				ple.getLenghtOfTracesComboBoxFROM().setEnabled(false);
				ple.getLenghtOfTracesComboBoxTO().setEnabled(false);

				_view.setComponentEnabled(false);

				ple.setModal(true);
				ple.setVisible(true);

			}
		});
	}
}
