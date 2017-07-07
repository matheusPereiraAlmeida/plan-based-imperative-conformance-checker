package control;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.Pnml;
import main.Constants;
import main.PetriNetTransition;
import main.PnmlImportUtils;
import main.Trace;
import main.XLogReader;

import view.MenuPerspective;

public class H_MenuPerspective {

	private String generated_invisible_transition = "generatedINV";
	private int number_of_generated_transitions = 0;

	public MenuPerspective _view = null;

	public H_MenuPerspective (MenuPerspective i_view){
		_view = i_view;
		installListeners();
	}

	private void installListeners() {

		_view.getExitMenuItem().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Constants.getDesktop().dispose();

			}
		});

		_view.getNewMenuItem().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				String[] options = new String[] {"Yes", "No"};
				int response = JOptionPane.showOptionDialog(null, "Are you sure you want to lose the data \nof the current event log/Petri Net?", "Create a new event log/Petri Net",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("images/question_icon.png"), options, options[0]);

				if(response==0) {

					Constants.setPetriNetFileName("");
					Constants.setEventLogFileName("Created from scratch");

					Constants.getAlphabetPerspective().resetComponent();
					Constants.setLogActivitiesRepositoryVector(new Vector<String>());

					Constants.getTracePerspective().resetComponent();
					Constants.setAllTracesVector(new Vector<Trace>());

					Constants.getPetriNetPerspective().resetComponent();
					Constants.setAllPlacesVector(new Vector<String>());
					Constants.setAllTransitionsVector(new Vector<PetriNetTransition>());
					Constants.setPlacesInInitialMarkingVector(new Vector<String>());
					Constants.setPlacesInFinalMarkingVector(new Vector<String>());


					Constants.setActivitiesCostVector(new Vector<Vector<String>>());
					Constants.setPetriNetMarkingVector(new Vector<Vector<String>>());

					Constants.getAlphabetPerspective().setComponentEnabled(true);
					Constants.getTracePerspective().setComponentEnabled(false);
					Constants.getPetriNetPerspective().setComponentEnabled(false);					
					Constants.getMenuPerspective().getImportPetriNetMenuItem().setEnabled(false);
				}
			}
		});

		_view.getOpenMenuItem().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();

				FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("Log file (*.xes)", "xes");

				fileChooser.setDialogTitle("Open log file");
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(xmlfilter);

				String workingDirectoryName = System.getProperty("user.dir");
				File workingDirectory = new File(workingDirectoryName + File.separator + "resources" + File.separator + "Log Files");
				fileChooser.setCurrentDirectory(workingDirectory);


				int returnValue = fileChooser.showOpenDialog(Constants.getDesktop());
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					// System.out.println(selectedFile.getName());

					Constants.setEventLogFileName(selectedFile.getName());

					try {

						////////////////////////////////////////////////////////////////
						// RESET the alphabet perspective view
						Constants.getAlphabetPerspective().resetComponent();
						Constants.setLogActivitiesRepositoryVector(new Vector<String>());

						// RESET the trace perspective view
						Constants.getTracePerspective().resetComponent();
						Constants.setAllTracesVector(new Vector<Trace>());

						// RESET the Petri Nets perspective view
						Constants.getPetriNetPerspective().resetComponent();
						Constants.setAllPlacesVector(new Vector<String>());
						Constants.setAllTransitionsVector(new Vector<PetriNetTransition>());

						// RESET the costs of adding/removing tasks in/from the trace
						// --> It is already done during the transition between PetriNetPerspective to PlannerPerspective
						// --> NOT REQUIRED HERE
						//Constants.setActivitiesCostVector(new Vector<Vector<String>>());
						//Constants.setPetriNetMarkingVector(new Vector<Vector<String>>());

						Constants.getAlphabetPerspective().setComponentEnabled(false);
						Constants.getTracePerspective().setComponentEnabled(true);
						Constants.getPetriNetPerspective().setComponentEnabled(false);
						////////////////////////////////////////////////////////////////

						XLog log = XLogReader.openLog(selectedFile.getAbsolutePath());

						int trace_int_id = 0;

						// Vector used to record the complete alphabet of activities used in the log
						Vector<String> loaded_alphabet_vector = new Vector<String>();

						// Vector used to record the activities of a specific trace of the log
						Vector<String> loaded_trace_activities_vector = new Vector<String>();

						//int sumOfTracesLength=0;

						for(XTrace trace:log){

							trace_int_id++;

							//String traceName = XConceptExtension.instance().extractName(trace);
							//System.out.println("Trace Name : " + traceName);

							Trace t = new Trace("Trace#" + trace_int_id);

							t.setTraceAlphabet(new Vector<String>());

							//UPDATE the JComboBox of the GUI with the loaded traces 
							Constants.getTracePerspective().getTracesComboBox().addItem(t.getTraceName());
							//////////////////////////////////////////////

							//XAttributeMap caseAttributes = trace.getAttributes();
							loaded_trace_activities_vector = new Vector<String>();

							//int traceLength=0;

							for(XEvent event : trace){
								String activityName = XConceptExtension.instance().extractName(event).toLowerCase();

								if(activityName.contains(" "))
									activityName = activityName.replaceAll(" ", "");

								if(activityName.contains("/"))
									activityName = activityName.replaceAll("\\/", "");

								if(activityName.contains("("))
									activityName = activityName.replaceAll("\\(", "");

								if(activityName.contains(")"))
									activityName = activityName.replaceAll("\\)", "");

								if(activityName.contains("<"))
									activityName = activityName.replaceAll("\\<", "");

								if(activityName.contains(">"))
									activityName = activityName.replaceAll("\\>", "");

								if(activityName.contains("."))
									activityName = activityName.replaceAll("\\.", "");

								if(activityName.contains(","))
									activityName = activityName.replaceAll("\\,", "_");

								if(activityName.contains("+"))
									activityName = activityName.replaceAll("\\+", "_");

								if(activityName.contains("-"))
									activityName = activityName.replaceAll("\\-", "_");

								//System.out.println("Activity Name : " + activityName);

								//Add to the activity name the type of event that it covers in the life cycle
								//String eventType = XLifecycleExtension.instance().extractTransition(event).toLowerCase();

								//traceLength++;

								loaded_trace_activities_vector.addElement(activityName);

								if(!t.getTraceAlphabet().contains(activityName)) {
									t.getTraceAlphabet().addElement(activityName);
								}

								if(!loaded_alphabet_vector.contains(activityName))
									loaded_alphabet_vector.addElement(activityName);

								/*
								Date timestamp = XTimeExtension.instance().extractTimestamp(event);
								System.out.println("TimeStamp : " + timestamp);

								String eventType = XLifecycleExtension.instance().extractTransition(event);
								XAttributeMap eventAttributes = event.getAttributes();
								for(String key :eventAttributes.keySet()){
									String value = eventAttributes.get(key).toString();
									System.out.println("Value : " + value);
								}
								for(String key :caseAttributes.keySet()){
									String value = caseAttributes.get(key).toString();
									System.out.println("Value : " + value);
								}
								 */

							}

							// Update the single trace of the log						

							for(int j=0;j<loaded_trace_activities_vector.size();j++) {
								String string = (String) loaded_trace_activities_vector.elementAt(j);
								t.getTraceContentVector().addElement(string);

								t.getTraceTextualContent().append(string);
								if(j<loaded_trace_activities_vector.size()-1)
									t.getTraceTextualContent().append(",");
							}

							Constants.getAllTracesVector().addElement(t);
							/////////////////////////////////////////////////////////////
							//sumOfTracesLength+=traceLength;
						}

						//Update the GUI component with the loaded LOG
						Constants.setLogActivitiesRepositoryVector(loaded_alphabet_vector);
						for(int kix=0;kix<loaded_alphabet_vector.size();kix++){
							Constants.getAlphabetPerspective().getAlphabetListModel().addElement(loaded_alphabet_vector.elementAt(kix));
							Constants.getTracePerspective().getAlphabetListModel().addElement(loaded_alphabet_vector.elementAt(kix));
						}

						Constants.getTracePerspective().getTracesComboBox().setSelectedIndex(1);

						Constants.getMenuPerspective().getImportPetriNetMenuItem().setEnabled(false);

						//System.out.println(sumOfTracesLength);
					} 
					catch (Exception exception) {
						exception.printStackTrace();
					}
				}	
			}
		});	

		_view.getImportPetriNetMenuItem().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();

				FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("Petri Net Markup Language (*.pnml)", "pnml");

				fileChooser.setDialogTitle("Import a Petri Net");
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(xmlfilter);

				String workingDirectoryName = System.getProperty("user.dir");
				File workingDirectory = new File(workingDirectoryName + File.separator + "resources" + File.separator + "Petri Nets");
				fileChooser.setCurrentDirectory(workingDirectory);

				int returnValue = fileChooser.showOpenDialog(Constants.getDesktop());

				if (returnValue == JFileChooser.APPROVE_OPTION) {

					int response = 0;

					if(!Constants.getPetriNetPerspective().getPetriNetArea().getText().isEmpty()) {
						String[] options = new String[] {"Yes", "No"};
						response = JOptionPane.showOptionDialog(null, "Lose the previously defined Petri Net?", "Import a Petri Net from a PNML file",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("images/question_icon.png"), options, options[0]);
					}

					if(response==0) {

						// RESET the costs of adding/removing tasks in/from the trace
						// --> It is already done during the transition between PetriNetPerspective to PlannerPerspective
						// --> NOT REQUIRED HERE
						//Constants.setActivitiesCostVector(new Vector<Vector<String>>());
						//Constants.setPetriNetMarkingVector(new Vector<Vector<String>>());

						File f = fileChooser.getSelectedFile();

						Constants.setPetriNetFileName(f.getName());

						try {


							PnmlImportUtils ut = new PnmlImportUtils();									  	

							InputStream input = new FileInputStream(f);
							Pnml pnml = ut.importPnmlFromStream(input, f.getName(), f.length());
							PetrinetGraph net = PetrinetFactory.newInhibitorNet(pnml.getLabel() + " (imported from " + f.getName() + ")");
							Marking marking = new Marking();
							pnml.convertToNet(net,marking ,new GraphLayoutConnection(net));

							Collection<Place> places = net.getPlaces();
							Collection<Transition> transitions = net.getTransitions();

							StyleContext context = new StyleContext();
							Style style = context.addStyle("test", null);     

							try {
								StyleConstants.setForeground(style, Color.BLACK);
								StyleConstants.setBold(style, true);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "******* Details of the Petri Net *******\n\n", style);

								StyleConstants.setForeground(style, Color.BLACK);
								StyleConstants.setBold(style, false);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "-- Petri Net file: ", style);
								StyleConstants.setForeground(style, Color.RED);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), Constants.getPetriNetFileName() + "\n", style);
								StyleConstants.setForeground(style, Color.BLACK);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "-- Number of Transitions: ", style);
								StyleConstants.setForeground(style, Color.BLUE);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), transitions.size() + "\n", style);
								StyleConstants.setForeground(style, Color.BLACK);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "-- Number of Places: ", style);
								StyleConstants.setForeground(style, Color.BLUE);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), places.size() + "\n", style);
							} catch (BadLocationException ex) {
								ex.printStackTrace();
							}

							//System.out.println(places);
							//System.out.println(transitions);

							Iterator<Place> places_iterator = places.iterator();
							Iterator<Transition> transitions_iterator = transitions.iterator();

							Constants.setAllTransitionsVector(new Vector<PetriNetTransition>());
							Constants.setAllPlacesVector(new Vector<String>());

							Constants.setPlacesInInitialMarkingVector(new Vector<String>());
							Constants.setPlacesInFinalMarkingVector(new Vector<String>());

							//Feed the vector of places with the places imported from the Petri Net
							while(places_iterator.hasNext()) {
								Place aPlace = places_iterator.next();

								String placeName = aPlace.getLabel();

								placeName = getCorrectFormatting(placeName);

								Constants.getAllPlacesVector().addElement(placeName.toLowerCase());

								Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesOutPlaceCollection = net.getOutEdges(aPlace);								
								Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesInPlaceCollection = net.getInEdges(aPlace);

								if(edgesInPlaceCollection.isEmpty())
									Constants.getPlacesInInitialMarkingVector().addElement(placeName);

								if(edgesOutPlaceCollection.isEmpty())
									Constants.getPlacesInFinalMarkingVector().addElement(placeName);

							}

							StyleConstants.setForeground(style, Color.BLACK);
							Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "-- Places with no incoming edges: ", style);
							StyleConstants.setForeground(style, Color.BLUE);
							Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), Constants.getPlacesInInitialMarkingVector() + "\n", style);
							StyleConstants.setForeground(style, Color.BLACK);
							Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "-- Places with no outgoing edges: ", style);
							StyleConstants.setForeground(style, Color.BLUE);
							Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), Constants.getPlacesInFinalMarkingVector() + "\n\n", style);

							while(transitions_iterator.hasNext()) {

								Transition aTransition = transitions_iterator.next();

								//System.out.println(aTransition.getLabel());

								StyleConstants.setBold(style, true);
								StyleConstants.setForeground(style, Color.BLACK);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "Transition: ", style);
								StyleConstants.setBold(style, false);
								StyleConstants.setForeground(style, Color.BLUE);

								if(aTransition.getLabel().isEmpty()) {
									StyleConstants.setItalic(style, true);
									Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),"empty label\n", style);
									StyleConstants.setItalic(style, false);
								}
								else
									Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),aTransition.getLabel() + "\n", style);


								//To get OUTGOING edges from a transition
								Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesOutTcollection = net.getOutEdges(aTransition);

								//To get INGOING edges to a transition
								Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesInTcollection = net.getInEdges(aTransition);

								Vector<Place> inputPlacesVector = new Vector<Place>();
								Vector<Place> outputPlacesVector = new Vector<Place>();

								Iterator<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesInTcollectionIterator = edgesInTcollection.iterator();
								Iterator<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesOutTcollectionIterator = edgesOutTcollection.iterator();

								//System.out.println("Input Places of transition " + aTransition.getLabel());


								StyleConstants.setForeground(style, Color.BLACK);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "- List of input places: <", style);

								while(edgesInTcollectionIterator.hasNext()) {
									PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge = edgesInTcollectionIterator.next();									
									inputPlacesVector.addElement((Place) edge.getSource());

									Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),edge.getSource().getLabel(), style);

									if(edgesInTcollectionIterator.hasNext()) {
										Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),",", style);
										//System.out.println(edge.getSource().getLabel());								
									}
								}
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),">\n", style);

								//System.out.println("Output Places of transition " + aTransition.getLabel());

								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "- List of output places: <", style);

								while(edgesOutTcollectionIterator.hasNext()) {
									PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge = edgesOutTcollectionIterator.next();
									outputPlacesVector.addElement((Place) edge.getTarget());

									Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),edge.getTarget().getLabel(), style);

									if(edgesOutTcollectionIterator.hasNext())
										Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),",", style);


									//System.out.println(edge.getTarget().getLabel());										
								}
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),">\n\n", style);


								String activityName = aTransition.getLabel();

								if(activityName.isEmpty() || activityName.equalsIgnoreCase("") || activityName.equalsIgnoreCase(" ") || activityName.equalsIgnoreCase("\"")) {
									activityName = new String(generated_invisible_transition+number_of_generated_transitions);
									number_of_generated_transitions++;
								}

								if(activityName.contains(" "))
									activityName = activityName.replaceAll(" ", "");

								if(activityName.contains("/"))
									activityName = activityName.replaceAll("\\/", "");

								if(activityName.contains("("))
									activityName = activityName.replaceAll("\\(", "");

								if(activityName.contains(")"))
									activityName = activityName.replaceAll("\\)", "");

								if(activityName.contains("<"))
									activityName = activityName.replaceAll("\\<", "");

								if(activityName.contains(">"))
									activityName = activityName.replaceAll("\\>", "");

								if(activityName.contains("."))
									activityName = activityName.replaceAll("\\.", "");

								if(activityName.contains(","))
									activityName = activityName.replaceAll("\\,", "_");

								if(activityName.contains("+"))
									activityName = activityName.replaceAll("\\+", "_");

								if(activityName.contains("-"))
									activityName = activityName.replaceAll("\\-", "_");

								PetriNetTransition trans = new PetriNetTransition(activityName.toLowerCase(), inputPlacesVector, outputPlacesVector);
								Constants.getAllTransitionsVector().addElement(trans);
							}

							Constants.getPetriNetPerspective().getPetriNetArea().setCaretPosition(0);

							//
							// Check if a transition with a specific label appears multiple times in a Petri Net		
							// If so, create a specific alias for the transition 
							//
							for(int ixc=0;ixc<Constants.getAllTransitionsVector().size();ixc++)  {

								PetriNetTransition pnt = Constants.getAllTransitionsVector().elementAt(ixc);
								int occurrences = 0;

								if(!pnt.isMultiple()) {

									for(int j=ixc+1;j<Constants.getAllTransitionsVector().size();j++)  {

										PetriNetTransition pnt2 = Constants.getAllTransitionsVector().elementAt(j);

										if(pnt2.getName().equalsIgnoreCase(pnt.getName())) {
											if(!pnt.isMultiple()) {
												pnt.setMultiple(true);
												pnt.setAlias(pnt.getName() + "0");
											}
											occurrences ++;
											pnt2.setAlias(pnt.getName() + occurrences);
											pnt2.setMultiple(true);
										}

									}
								}
							}
							/////////////////////////////////////////////////////////////////////////////////////

							Constants.getPetriNetPerspective().getNextStepButton().setEnabled(true);

						}
						catch (Exception exception) {
							exception.printStackTrace();
						}
					}
				}
			}
		});

	}

	private String getCorrectFormatting(String string)  {

		if(string.contains(" "))
			string = string.replaceAll(" ", "");

		if(string.contains("/"))
			string = string.replaceAll("\\/", "");

		if(string.contains("("))
			string = string.replaceAll("\\(", "");

		if(string.contains(")"))
			string = string.replaceAll("\\)", "");

		if(string.contains("<"))
			string = string.replaceAll("\\<", "");

		if(string.contains(">"))
			string = string.replaceAll("\\>", "");

		if(string.contains("."))
			string = string.replaceAll("\\.", "");

		if(string.contains(","))
			string = string.replaceAll("\\,", "_");

		if(string.contains("+"))
			string = string.replaceAll("\\+", "_");

		if(string.contains("-"))
			string = string.replaceAll("\\-", "_");

		return string;
	}

}
