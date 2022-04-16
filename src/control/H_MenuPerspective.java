package control;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.Pnml;
import main.Constants;
import main.PetrinetTransition;
import main.PnmlImportUtils;
import main.Trace;
import main.XLogReader;
import view.MenuPerspective;

public class H_MenuPerspective {

	private static final String INVISIBLE_TRANSITION_PREFIX = "generatedINV";
	private Pattern decimalNumberRegexPattern = Pattern.compile("\\d+(,\\d{3})*(\\.\\d+)*");
	
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
		
		_view.getCompareResults().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				File[] resultFiles = importFiles();   
				
				comparisonResults(resultFiles);

				//System.out.print(resultFiles);
				
				//CompareResults compareResults = new CompareResults();
				
				
			}

			private void comparisonResults(File[] resultFiles) {
				try {	
					//FileWriter myWriter = new FileWriter("filename.txt");					
					Map<String, Vector<String>> firstResultDictionary = extracted(resultFiles);
					Integer diff = 0;

					for(String alignmentName : firstResultDictionary.keySet()) {
						Matcher traceIdMatcher = decimalNumberRegexPattern.matcher(alignmentName);
						traceIdMatcher.find();
						int traceId = Integer.parseInt(traceIdMatcher.group());

						Trace trace = Constants.getAllTracesVector().elementAt(traceId - 1);

						String correctTrace = trace.getTraceTextualContent().toString().replaceAll(",", "");
						String wrongTrace = firstResultDictionary.get(alignmentName).elementAt(2);

						//String correctTrace2 = correctTrace.replaceAll("activity", "");
						//String wrongTrace2 = wrongTrace.replaceAll("activity", "");

						if(!correctTrace.equals(wrongTrace)) {
							diff++;
						}
					}
					System.out.println("diif: "+diff+" equals: "+ (2000-diff));

					//carrega o trace original 
					//						//compara eles 
					//					if(firstResultDictionary.size() != secondResultDictionary.size()) {
					//						//alerta erro
					//					}
					//
					//					myWriter.write("name;costA;costB;timeA;TimeB;CostDifferent" + System.lineSeparator());
					//
					//					for(String key : firstResultDictionary.keySet()) {
					//						Vector<String> element1= firstResultDictionary.get(key);
					//						Vector<String> element2= secondResultDictionary.get(key);
					//
					//						if(element1.elementAt(0).equals("0") && element2.elementAt(0).equals("0")) {
					//							continue;
					//						}
					//
					//						myWriter.write(key+";");
					//						myWriter.write(element1.elementAt(0));
					//						myWriter.write(element2.elementAt(0)+";");
					//						myWriter.write(element1.elementAt(1)+";");
					//						myWriter.write(element2.elementAt(1)+";");
					//						Boolean isDiff = !element1.elementAt(0).equals(element2.elementAt(0));
					//						if(!isDiff) {
					//							diff++;
					//						}
					//						myWriter.write(isDiff+System.lineSeparator());
					//					}
					//					myWriter.write(diff);
					//					myWriter.close();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			private File[] importFiles() {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);

				Component frame = null;

				chooser.showOpenDialog(frame);
				File[] resultFiles = chooser.getSelectedFiles();
				
				JOptionPane.showMessageDialog(null, "files imported correctly!", "Success!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/success_icon.gif"));
				return resultFiles;
			}
			
			private Map<String, Vector<String>> extracted(File[] files) throws FileNotFoundException {
				Map<String, Vector<String>> infLines = new HashMap<String, Vector<String>>();

				for(File file : files) {
					var absolutePath = file.getAbsolutePath();
					Scanner sc = new Scanner(new FileReader(absolutePath));					
					Vector<String> inf = new Vector<String>();

					String fileName = absolutePath.substring(75, absolutePath.length());
					String originalMoves = "";						
					String traceAligned = "";

					while(sc.hasNext()) {
						String line = sc.nextLine();
						if(line.contains("; cost = ")) {
							String value = line.substring(9, line.indexOf(" (general"));
							inf.add(value);
						}else if(line.contains("; searchtime = ")) {
							String value = line.substring(15, line.length());
							inf.add(value);
						}else {
							if(line.contains("movesync")) {
								traceAligned = traceAligned.concat(line.substring(10,19));
							}
							if(line.contains("moveinthemodel") && !line.contains("generatedinv")) {
								String value = "activity"+line.charAt(24);
								traceAligned = traceAligned.concat(value);							
							}
							if(line.contains("moveinthelog")) {
								traceAligned = traceAligned.concat(line.substring(14,23));
							}
							originalMoves = originalMoves.concat(line + System.lineSeparator());
						} 
					}

					inf.add(traceAligned);
					inf.add(originalMoves);
					infLines.put(fileName, inf);
				}
				return infLines;
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
					Constants.setAllTransitionsVector(new Vector<PetrinetTransition>());
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
						Constants.setAllTransitionsVector(new Vector<PetrinetTransition>());

						Constants.getAlphabetPerspective().setComponentEnabled(false);
						Constants.getTracePerspective().setComponentEnabled(true);
						Constants.getPetriNetPerspective().setComponentEnabled(false);
						////////////////////////////////////////////////////////////////

						XLog log = XLogReader.openLog(selectedFile.getAbsolutePath());

						int traceId = 0;

						// Vector used to record the complete alphabet of activities used in the log
						Vector<String> logAlphabetVector = new Vector<String>();

						// Vector used to record the activities of a specific trace of the log
						Vector<String> traceActivitiesVector = new Vector<String>();

						//int sumOfTracesLength=0;

						for(XTrace trace:log){

							traceId++;

							//String traceName = XConceptExtension.instance().extractName(trace);
							//System.out.println("Trace Name : " + traceName);

							Trace t = new Trace("Trace#" + traceId);

							t.setTraceAlphabet(new Vector<String>());

							//UPDATE the JComboBox of the GUI with the loaded traces 
							Constants.getTracePerspective().getTracesComboBox().addItem(t.getTraceName());
							//////////////////////////////////////////////

							//XAttributeMap caseAttributes = trace.getAttributes();
							traceActivitiesVector = new Vector<String>();

							//int traceLength=0;
							for(XEvent event : trace){
								String activityName = XConceptExtension.instance().extractName(event).toLowerCase();
								activityName = getCorrectFormatting(activityName);

								traceActivitiesVector.addElement(activityName);

								if(!t.getTraceAlphabet().contains(activityName))
									t.getTraceAlphabet().addElement(activityName);

								// add activity name to log alphabet (if not already present)
								if(!logAlphabetVector.contains(activityName))
									logAlphabetVector.addElement(activityName);

							}

							// Update the single trace of the log						

							for(int j=0;j<traceActivitiesVector.size();j++) {
								String string = (String) traceActivitiesVector.elementAt(j);
								t.getTraceContentVector().addElement(string);

								t.getTraceTextualContent().append(string);
								if(j<traceActivitiesVector.size()-1)
									t.getTraceTextualContent().append(",");
							}

							Constants.getAllTracesVector().addElement(t);
							/////////////////////////////////////////////////////////////
							//sumOfTracesLength+=traceLength;
						}

						//Update the GUI component with the loaded LOG
						Constants.setLogActivitiesRepositoryVector(logAlphabetVector);
						for(int kix=0; kix < logAlphabetVector.size(); kix++){
							Constants.getAlphabetPerspective().getAlphabetListModel().addElement(logAlphabetVector.elementAt(kix));
							Constants.getTracePerspective().getAlphabetListModel().addElement(logAlphabetVector.elementAt(kix));
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

					// ask user to confirm import if text area is non-empty
					if(!Constants.getPetriNetPerspective().getPetriNetArea().getText().isEmpty()) {
						String[] options = new String[] {"Yes", "No"};
						response = JOptionPane.showOptionDialog(null,
								"Lose the previously defined Petri Net?",
								"Import a Petri Net from a PNML file",
								JOptionPane.DEFAULT_OPTION, 
								JOptionPane.QUESTION_MESSAGE,
								new ImageIcon("images/question_icon.png"),
								options,
								options[0]);
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

							// create Pnml object from .pnml file
							PnmlImportUtils ut = new PnmlImportUtils();
							InputStream input = new FileInputStream(f);
							Pnml pnml = ut.importPnmlFromStream(input);

							// create Petri Net from Pnml object
							Petrinet net = PetrinetFactory.newPetrinet(pnml.getLabel() + " (imported from " + f.getName() + ")");
							Marking marking = new Marking();								  // only needed for Petrinet initialization
							pnml.convertToNet(net, marking, new GraphLayoutConnection(net));  // initialize Petrinet

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

							Constants.setAllTransitionsVector(new Vector<PetrinetTransition>());
							Constants.setAllPlacesVector(new Vector<String>());
							Constants.setPlacesInInitialMarkingVector(new Vector<String>());
							Constants.setPlacesInFinalMarkingVector(new Vector<String>());

							//Feed the vector of places with the places imported from the Petri Net.
							//Determine which places compose the initial and final markings.
							for (Place place : places) {
								String placeName = place.getLabel();
								placeName = getCorrectFormatting(placeName);

								Constants.getAllPlacesVector().addElement(placeName.toLowerCase());

								Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> placeOutEdgesCollection = net.getOutEdges(place);								
								Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> placeInEdgesCollection = net.getInEdges(place);

								if(placeInEdgesCollection.isEmpty())
									Constants.getPlacesInInitialMarkingVector().addElement(placeName);

								if(placeOutEdgesCollection.isEmpty())
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

							
							int generatedTransitionsNum = 0;
							for (Transition transition : transitions) {
								//System.out.println(aTransition.getLabel());

								StyleConstants.setBold(style, true);
								StyleConstants.setForeground(style, Color.BLACK);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "Transition: ", style);
								StyleConstants.setBold(style, false);
								StyleConstants.setForeground(style, Color.BLUE);

								if(transition.getLabel().isEmpty()) {
									StyleConstants.setItalic(style, true);
									Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),"empty label\n", style);
									StyleConstants.setItalic(style, false);
								}
								else
									Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),transition.getLabel() + "\n", style);


								//To get OUTGOING edges from a transition
								Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionOutEdgesCollection = net.getOutEdges(transition);

								//To get INGOING edges to a transition
								Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionInEdgesCollection = net.getInEdges(transition);


								Vector<Place> transitionOutPlacesVector = new Vector<Place>();
								Vector<Place> transitionInPlacesVector = new Vector<Place>();
								Iterator<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionInEdgesIterator = transitionInEdgesCollection.iterator();
								Iterator<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionOutEdgesIterator = transitionOutEdgesCollection.iterator();


								//get the collection of input places
								StyleConstants.setForeground(style, Color.BLACK);
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "- List of input places: <", style);

								while(transitionInEdgesIterator.hasNext()) {
									PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge = transitionInEdgesIterator.next();									
									transitionInPlacesVector.addElement((Place) edge.getSource());

									Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),edge.getSource().getLabel(), style);

									if(transitionInEdgesIterator.hasNext()) {
										Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),",", style);
										//System.out.println(edge.getSource().getLabel());								
									}
								}
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),">\n", style);



								//get the collection of output places
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(), "- List of output places: <", style);

								while(transitionOutEdgesIterator.hasNext()) {
									PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge = transitionOutEdgesIterator.next();
									transitionOutPlacesVector.addElement((Place) edge.getTarget());

									Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),edge.getTarget().getLabel(), style);

									if(transitionOutEdgesIterator.hasNext())
										Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),",", style);


									//System.out.println(edge.getTarget().getLabel());										
								}
								Constants.getPetriNetPerspective().getDocument().insertString(Constants.getPetriNetPerspective().getDocument().getLength(),">\n\n", style);


								String activityName = transition.getLabel();

								if(activityName.isEmpty() || activityName.equalsIgnoreCase("") || activityName.equalsIgnoreCase(" ") || activityName.equalsIgnoreCase("\"")) {
									activityName = new String(INVISIBLE_TRANSITION_PREFIX + generatedTransitionsNum);
									generatedTransitionsNum++;
								}

								activityName = getCorrectFormatting(activityName);

								PetrinetTransition petriNetTransition = new PetrinetTransition(activityName.toLowerCase(), transitionInPlacesVector, transitionOutPlacesVector);
								Constants.getAllTransitionsVector().addElement(petriNetTransition);
							}

							Constants.getPetriNetPerspective().getPetriNetArea().setCaretPosition(0);

							//
							// Check if a transition with a specific label appears multiple times in a Petri Net		
							// If so, create a specific alias for the transition 
							//
							for(int ixc=0;ixc<Constants.getAllTransitionsVector().size();ixc++)  {

								PetrinetTransition pnt = Constants.getAllTransitionsVector().elementAt(ixc);
								int occurrences = 0;

								if(!pnt.isMultiple()) {

									for(int j=ixc+1;j<Constants.getAllTransitionsVector().size();j++)  {

										PetrinetTransition pnt2 = Constants.getAllTransitionsVector().elementAt(j);

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
