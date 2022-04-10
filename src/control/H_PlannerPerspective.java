package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.processmining.models.graphbased.directed.petrinet.elements.Place;

import main.Constants;
import main.PetrinetTransition;
import main.Trace;
import main.Utilities;
import view.PlannerPerspective;
import view.ResultsPerspective;

public class H_PlannerPerspective {

	public PlannerPerspective _view = null;

	public H_PlannerPerspective (PlannerPerspective i_view){
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
				_view.dispose();
				Constants.getPetriNetPerspective().setComponentEnabled(true);
			}
		});

		_view.getPreviousStepButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				_view.dispose();
				Constants.getPetriNetPerspective().setComponentEnabled(true);
			}
		});

		_view.getCostCheckBox().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getCostCheckBox().isSelected()) {
					_view.getAddingCostField().setEnabled(true);
					_view.getRemovalCostField().setEnabled(true);
					_view.getCostComboBox().setEnabled(true);
					_view.getUseTrainingLog().setEnabled(true);
					_view.getUseUpdatedLog().setEnabled(true);
					_view.getResetCosts().setEnabled(true);
				}
				else {
					_view.getAddingCostField().setEnabled(false);
					_view.getRemovalCostField().setEnabled(false);
					_view.getCostComboBox().setEnabled(false);
					_view.getUseTrainingLog().setEnabled(false);
					_view.getUseUpdatedLog().setEnabled(false);
					_view.getResetCosts().setEnabled(false);
				}
			}
		});

		_view.getNumberOfTracesCheckBox().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getNumberOfTracesCheckBox().isSelected()) {
					_view.getTraceIdComboBoxFROM().setEnabled(true);
					_view.getTraceIdComboBoxTO().setEnabled(true);
				}
				else {
					_view.getTraceIdComboBoxFROM().setEnabled(false);
					_view.getTraceIdComboBoxTO().setEnabled(false);
				}
			}
		});

		_view.getLenghtOfTracesCheckBox().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getLenghtOfTracesCheckBox().isSelected()) {
					_view.getLenghtOfTracesComboBoxFROM().setEnabled(true);
					_view.getLenghtOfTracesComboBoxTO().setEnabled(true);
				}
				else {
					_view.getLenghtOfTracesComboBoxFROM().setEnabled(false);
					_view.getLenghtOfTracesComboBoxTO().setEnabled(false);
				}
			}
		});

		_view.getTraceDuplicatedCheckBox().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getTraceDuplicatedCheckBox().isSelected()) {
					Constants.setDiscardDuplicatedTraces(true);
				}
				else {
					Constants.setDiscardDuplicatedTraces(false);
				}
			}
		});

		_view.getRunPlannerButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{

				if(_view.getTraceDuplicatedCheckBox().isSelected()) {
					Constants.setDiscardDuplicatedTraces(true);
				}
				else {
					Constants.setDiscardDuplicatedTraces(false);
				}

				if(!_view.getLazyGreedyRadioButton().isSelected() && !_view.getOptimalRadioButton().isSelected())
					JOptionPane.showMessageDialog(null, "It is required to choose at least a valid \nsearch heuristic to run the planner!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				else if(_view.getNumberOfTracesCheckBox().isSelected() && (_view.getTraceIdComboBoxFROM().getSelectedIndex()==0 || _view.getTraceIdComboBoxTO().getSelectedIndex()==0 || _view.getTraceIdComboBoxFROM().getSelectedIndex() > _view.getTraceIdComboBoxTO().getSelectedIndex())) {
					JOptionPane.showMessageDialog(null, "Please select a valid interval of traces to analyze!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
				}
				else if(_view.getLenghtOfTracesCheckBox().isSelected() && (_view.getLenghtOfTracesComboBoxFROM().getSelectedIndex()==0 || _view.getLenghtOfTracesComboBoxTO().getSelectedIndex()==0 || _view.getLenghtOfTracesComboBoxFROM().getSelectedIndex() > _view.getLenghtOfTracesComboBoxTO().getSelectedIndex())) {
					JOptionPane.showMessageDialog(null, "Please select a valid length of traces to analyze!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
				}
				else {

					if(Constants.getPlannerPerspective().getCostCheckBox().isSelected()) {
						//UPDATE the ADDING/REMOVAL cost associated to the last task updated
						String selected_task_name_for_cost = (String) _view.getCostComboBox().getSelectedItem();

						if (!(selected_task_name_for_cost.equalsIgnoreCase("-- Activity name --")) )
						{
							for(int index=0;index<Constants.getActivitiesCostVector().size();index++) {							
								Vector<String> v = Constants.getActivitiesCostVector().elementAt(index);
								if(v.elementAt(0).equalsIgnoreCase(selected_task_name_for_cost)) {
									v.set(1,_view.getAddingCostField().getText());
									v.set(2,_view.getRemovalCostField().getText());
									break;
								}
							}
						}				
					}

					String selected_place_name_for_marking = (String) _view.getMarkingComboBox().getSelectedItem();

					if (!(selected_place_name_for_marking.equalsIgnoreCase("-- Place name --"))) {
						for(int index=0;index<Constants.getPetriNetMarkingVector().size();index++) {							
							Vector<String> v = Constants.getPetriNetMarkingVector().elementAt(index);
							if(v.elementAt(0).equalsIgnoreCase(selected_place_name_for_marking)) {
								v.set(1,_view.getInitialMarkingField().getText());

								if(_view.getFinalMarkingField().getText().equalsIgnoreCase("") || _view.getFinalMarkingField().getText().contains(" "))
									v.set(2, "n/a");
								else
									v.set(2,_view.getFinalMarkingField().getText());

								break;
							}
						}
					}	

					//
					// Check if there is at least one token in the initial marking and one token in the final marking of the Petri Net 
					//
					boolean has_initial_marking_boolean = false;
					boolean has_final_marking_boolean = false;
					for(int py=0;py<Constants.getPetriNetMarkingVector().size();py++)  {

						Vector<String> place_vector = Constants.getPetriNetMarkingVector().elementAt(py);

						if(!Utilities.isInteger(place_vector.elementAt(1)) || !Utilities.isInteger(place_vector.elementAt(2))) {
							JOptionPane.showMessageDialog(null, "Only integer values are accepted for the initial/final marking of the Petri Net!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
							return;
						}
						else {
							int initial_marking_for_the_place = new Integer(place_vector.elementAt(1));
							int final_marking_for_the_place = new Integer(place_vector.elementAt(2));

							if(initial_marking_for_the_place < 0 || initial_marking_for_the_place > 1 || final_marking_for_the_place < 0 || final_marking_for_the_place > 1) {
								JOptionPane.showMessageDialog(null, "Only 0 or 1 token per place are allowed!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
								return;
							}

							if(initial_marking_for_the_place==1) {
								has_initial_marking_boolean = true;
							}
							else if(final_marking_for_the_place==1) {
								has_final_marking_boolean = true;
							}
						}
					}
					if(!has_initial_marking_boolean) {
						JOptionPane.showMessageDialog(null, "In the initial marking it is required to have at least 1 token in 1 place!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
						return;
					}

					if(!has_final_marking_boolean) {
						JOptionPane.showMessageDialog(null, "In the final marking it is required to have at least 1 token in 1 place!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
						return;
					}
					//////////////////////////////////////////////////////////////

					new ResultsPerspective();
				}
			}
		});

		_view.getCostComboBox().addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				if(event.getStateChange() == ItemEvent.DESELECTED) 
				{

					String previous_selected_task = (String) event.getItem();

					if(!previous_selected_task.equalsIgnoreCase("-- Activity name --")) {

						for(int ix=0;ix<Constants.getActivitiesCostVector().size();ix++) {							
							Vector<String> v = Constants.getActivitiesCostVector().elementAt(ix);
							if(v.elementAt(0).equalsIgnoreCase(previous_selected_task)) {		
								v.set(1, _view.getAddingCostField().getText());
								v.set(2, _view.getRemovalCostField().getText());
								Constants.getActivitiesCostVector().set(ix,v);
								break;
							}
						}
					}					
				}

				String selected_task_name = (String) _view.getCostComboBox().getSelectedItem();

				if (event.getStateChange() == ItemEvent.SELECTED && !(selected_task_name.equalsIgnoreCase("-- Activity name --")) )
				{
					for(int index=0;index<Constants.getActivitiesCostVector().size();index++) {							
						Vector<String> v = Constants.getActivitiesCostVector().elementAt(index);
						if(v.elementAt(0).equalsIgnoreCase(selected_task_name)) {
							_view.getAddingCostField().setText(v.elementAt(1));
							_view.getRemovalCostField().setText(v.elementAt(2));		
							break;
						}
					}
				}				
				else if(event.getStateChange() == ItemEvent.SELECTED && (selected_task_name.equalsIgnoreCase("-- Activity name --"))) {
					_view.getAddingCostField().setText("Model move");
					_view.getRemovalCostField().setText("Log move");
				}

			}
		});

		_view.getMarkingComboBox().addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				if(event.getStateChange() == ItemEvent.DESELECTED) 
				{

					String previous_selected_place = (String) event.getItem();

					if(!previous_selected_place.equalsIgnoreCase("-- Place name --")) {

						for(int ix=0;ix<Constants.getPetriNetMarkingVector().size();ix++) {							
							Vector<String> v = Constants.getPetriNetMarkingVector().elementAt(ix);
							if(v.elementAt(0).equalsIgnoreCase(previous_selected_place)) {		
								v.set(1, _view.getInitialMarkingField().getText());

								if(_view.getFinalMarkingField().getText().equalsIgnoreCase("") || _view.getFinalMarkingField().getText().contains(" "))
									v.set(2, "n/a");
								else
									v.set(2, _view.getFinalMarkingField().getText());

								Constants.getPetriNetMarkingVector().set(ix,v);
								break;
							}
						}
					}					
				}

				String selected_place_name = (String) _view.getMarkingComboBox().getSelectedItem();

				if (event.getStateChange() == ItemEvent.SELECTED && !(selected_place_name.equalsIgnoreCase("-- Place name --")) )
				{
					for(int index=0;index<Constants.getPetriNetMarkingVector().size();index++) {							
						Vector<String> v = Constants.getPetriNetMarkingVector().elementAt(index);
						if(v.elementAt(0).equalsIgnoreCase(selected_place_name)) {
							_view.getInitialMarkingField().setText(v.elementAt(1));
							_view.getFinalMarkingField().setText(v.elementAt(2));		
							break;
						}
					}
				}				
				else if(event.getStateChange() == ItemEvent.SELECTED && (selected_place_name.equalsIgnoreCase("-- Place name --"))) {
					_view.getInitialMarkingField().setText("Initial Marking");
					_view.getFinalMarkingField().setText("Final Marking");
				}

			}
		});

		_view.getCreateDomainAndProblemButton().addActionListener(new ActionListener()  {

			public void actionPerformed(ActionEvent ae)  {            			    	
				//
				// Decide to discard (or not) duplicate traces, in order to avoid unnecessary alignments
				//        	
				if(_view.getTraceDuplicatedCheckBox().isSelected()) {
					Constants.setDiscardDuplicatedTraces(true);
				}
				else {
					Constants.setDiscardDuplicatedTraces(false);
				}

				if(_view.getNumberOfTracesCheckBox().isSelected() && (_view.getTraceIdComboBoxFROM().getSelectedIndex()==0 || _view.getTraceIdComboBoxTO().getSelectedIndex()==0 || _view.getTraceIdComboBoxFROM().getSelectedIndex() > _view.getTraceIdComboBoxTO().getSelectedIndex())) {
					JOptionPane.showMessageDialog(null, "Please select a valid interval of traces to analyze!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
				}
				else if(_view.getLenghtOfTracesCheckBox().isSelected() && (_view.getLenghtOfTracesComboBoxFROM().getSelectedIndex()==0 || _view.getLenghtOfTracesComboBoxTO().getSelectedIndex()==0 || _view.getLenghtOfTracesComboBoxFROM().getSelectedIndex() > _view.getLenghtOfTracesComboBoxTO().getSelectedIndex())) {
					JOptionPane.showMessageDialog(null, "Please select a valid length of traces to analyze!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));  
				}
				else {
					//
					// Remove the existing old files from the folder containing the generated planning domains and problems
					//
					File index = new File("PDDLfiles");            	
					String[]entries = index.list();
					for(String s: entries){
						File currentFile = new File(index.getPath(),s);
						currentFile.delete();
					}

					//////////////////////////////////////////////////////////////////////////

					//
					// Case in which we assign a cost to perform move in the model/log
					//
					if(Constants.getPlannerPerspective().getCostCheckBox().isSelected()) {

						//UPDATE the costs associated to the last activity edited
						String selected_task_name_for_cost = (String) _view.getCostComboBox().getSelectedItem();

						if (!(selected_task_name_for_cost.equalsIgnoreCase("-- Activity name --")) )
						{
							for(int ind=0;ind<Constants.getActivitiesCostVector().size();ind++) {							
								Vector<String> v = Constants.getActivitiesCostVector().elementAt(ind);
								if(v.elementAt(0).equalsIgnoreCase(selected_task_name_for_cost)) {
									v.set(1,_view.getAddingCostField().getText());
									v.set(2,_view.getRemovalCostField().getText());
									break;
								}
							}
						}				
					}

					//////////////////////////////////////////////////////////////////////////

					//UPDATE the marking associated to the last place edited
					String selected_place_name_for_marking = (String) _view.getMarkingComboBox().getSelectedItem();

					if (!(selected_place_name_for_marking.equalsIgnoreCase("-- Place name --"))) {
						for(int ind=0;ind<Constants.getPetriNetMarkingVector().size();ind++) {							
							Vector<String> v = Constants.getPetriNetMarkingVector().elementAt(ind);
							if(v.elementAt(0).equalsIgnoreCase(selected_place_name_for_marking)) {
								v.set(1,_view.getInitialMarkingField().getText());

								if(_view.getFinalMarkingField().getText().equalsIgnoreCase("") || _view.getFinalMarkingField().getText().contains(" "))
									v.set(2, "n/a");
								else
									v.set(2,_view.getFinalMarkingField().getText());	            

								break;
							}
						}
					}

					//
					// Check if there is at least one token in the initial marking and one token in the final marking of the Petri Net 
					//
					boolean has_initial_marking_boolean = false;
					boolean has_final_marking_boolean = false;
					for(int py=0;py<Constants.getPetriNetMarkingVector().size();py++)  {

						Vector<String> place_vector = Constants.getPetriNetMarkingVector().elementAt(py);

						if(!Utilities.isInteger(place_vector.elementAt(1)) || !Utilities.isInteger(place_vector.elementAt(2))) {
							JOptionPane.showMessageDialog(null, "Only integer values are accepted for the initial/final marking of the Petri Net!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
							return;
						}
						else {
							int initial_marking_for_the_place = new Integer(place_vector.elementAt(1));
							int final_marking_for_the_place = new Integer(place_vector.elementAt(2));

							if(initial_marking_for_the_place < 0 || initial_marking_for_the_place > 1 || final_marking_for_the_place < 0 || final_marking_for_the_place > 1) {
								JOptionPane.showMessageDialog(null, "Only 0 or 1 token per place are allowed!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
								return;
							}

							if(initial_marking_for_the_place==1) {
								has_initial_marking_boolean = true;
							}
							else if(final_marking_for_the_place==1) {
								has_final_marking_boolean = true;
							}
						}
					}
					if(!has_initial_marking_boolean) {
						JOptionPane.showMessageDialog(null, "In the initial marking it is required to have at least 1 token in one place!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
						return;
					}

					if(!has_final_marking_boolean) {
						JOptionPane.showMessageDialog(null, "In the final marking it is required to have at least 1 token in one place!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));   
						return;
					}

					//////////////////////////////////////////////////////////////////////////

					int number_of_traces_to_check_from = 0;
					int number_of_traces_to_check_to = 0;

					if(Constants.getPlannerPerspective().getNumberOfTracesCheckBox().isSelected()) {
						number_of_traces_to_check_from = Constants.getPlannerPerspective().getTraceIdComboBoxFROM().getSelectedIndex();
						number_of_traces_to_check_to = Constants.getPlannerPerspective().getTraceIdComboBoxTO().getSelectedIndex();
					}			
					else {
						number_of_traces_to_check_from = 1;
						number_of_traces_to_check_to = Constants.getAllTracesVector().size();
					}

					//////////////////////////////////////////////////////////////////////////

					int length_of_traces_to_check_from = 0;
					int length_of_traces_to_check_to = 0;

					if(Constants.getPlannerPerspective().getLenghtOfTracesCheckBox().isSelected()) {
						length_of_traces_to_check_from = new Integer(Constants.getPlannerPerspective().getLenghtOfTracesComboBoxFROM().getSelectedItem().toString());
						length_of_traces_to_check_to = new Integer(Constants.getPlannerPerspective().getLenghtOfTracesComboBoxTO().getSelectedItem().toString());
					}			
					else {
						length_of_traces_to_check_from = Constants.getMinimumLengthOfATrace();
						length_of_traces_to_check_to = Constants.getMaximumLengthOfATrace();
					}

					//////////////////////////////////////////////////////////////////////////

					for(int k=number_of_traces_to_check_from-1;k<number_of_traces_to_check_to;k++) {

						Trace trace = Constants.getAllTracesVector().elementAt(k);

						if(_view.getTraceDuplicatedCheckBox().isSelected()) { // Remove duplicated traces

							if(Constants.getAllTracesHashtable().containsValue(trace.getTraceName()))  {

								if(trace.getTraceLength() >= length_of_traces_to_check_from && trace.getTraceLength() <= length_of_traces_to_check_to)  {

									StringBuffer sb_domain = Utilities.createPropositionalDomain(trace);
									StringBuffer sb_problem = Utilities.createPropositionalProblem(trace);

									//StringBuffer sb_domain = Utilities.createDomain(trace);
									//StringBuffer sb_problem = Utilities.createProblem(trace);

									int trace_real_number = k + 1;

									Utilities.writeFile("PDDLfiles/domain" + trace_real_number + ".pddl", sb_domain);
									Utilities.writeFile("PDDLfiles/problem" + trace_real_number + ".pddl", sb_problem);	 

								} 
							}           		
						}
						else { // Maintain duplicated traces

							if(trace.getTraceLength() >= length_of_traces_to_check_from && trace.getTraceLength() <= length_of_traces_to_check_to)  {

								StringBuffer sb_domain = Utilities.createPropositionalDomain(trace);
								StringBuffer sb_problem = Utilities.createPropositionalProblem(trace);

								//StringBuffer sb_domain = Utilities.createDomain(trace);
								//StringBuffer sb_problem = Utilities.createProblem(trace);

								int trace_real_number = k + 1;

								Utilities.writeFile("PDDLfiles/domain" + trace_real_number + ".pddl", sb_domain);
								Utilities.writeFile("PDDLfiles/problem" + trace_real_number + ".pddl", sb_problem);

							} 
						}
					}

					JOptionPane.showMessageDialog(null, "The PDDL files have been \ncorrectly encoded and generated!", "Success!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/success_icon.gif"));   

				}
			}

		});

		_view.getUseUpdatedLog().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Map<String, Double> activitiesFrequency = generateActivitiesFrequency();				
				generateModelMoveCosts(activitiesFrequency);
				generateLogMoveCosts(activitiesFrequency);

				JOptionPane.showMessageDialog(null, "The costs have been generated correctly!", "Success!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/success_icon.gif"));   

			}
			private Map<String, Double> generateActivitiesFrequency() {
				Vector<String> atividadesLog = Constants.getLogActivitiesRepositoryVector();
				Map<String, Double> dictionaryLogMoves = new HashMap<String, Double>();

				for(String atividade : atividadesLog)
					dictionaryLogMoves.put(atividade, 1.0);
				dictionaryLogMoves.put("sum", 5.0);

				Vector<Trace> traces = Constants.getAllTracesVector();

				for(Trace trace : traces){
					Vector<String> traceContent = trace.getTraceContentVector();
					Vector<String> traceAlphabet = trace.getTraceAlphabet();

					for(String activity : traceAlphabet) {
						int occurrences = Collections.frequency(traceContent, activity);
						double oldOccurrencesActivity = dictionaryLogMoves.get(activity);
						double oldOccurrencesSum = dictionaryLogMoves.get("sum");

						dictionaryLogMoves.replace(activity, occurrences + oldOccurrencesActivity);
						dictionaryLogMoves.replace("sum", occurrences + oldOccurrencesSum);
					}
				}
				return dictionaryLogMoves;
			}

			private void generateModelMoveCosts(Map<String, Double> activitiesFrequency) {
				Vector<PetrinetTransition> c = Constants.getAllTransitionsVector();				
				Vector<String> allPlaces = Constants.getAllPlacesVector();
				Map<String, Vector<String>> dictionaryModelMoves = new HashMap<String, Vector<String>>();

				//cria um dictinary com <place, <atividades>>	
				for(String place : allPlaces) {
					Vector<String> coco = new Vector<String>();
					for(PetrinetTransition transition : c) {
						Vector<Place> inputPlaces = transition.getInputPlacesVector();
						for(Place inputPlace : inputPlaces) {
							String labelInputPlace = inputPlace.getLabel();
							if(labelInputPlace.equals(place)){
								coco.add(transition.getName());								
							}
						}
					}
					dictionaryModelMoves.put(place, coco);
				}

				//t� dando problema na frequencia do inv
				Map<String, Double> dictionaryModelMoves2 = new HashMap<String, Double>();
				for(String place : allPlaces) {
					Vector<String> atividades = dictionaryModelMoves.get(place);
					int somaTotal = 0;
					for(String atividade : atividades) {
						somaTotal += activitiesFrequency.getOrDefault(atividade, 0.0);						
					}
					for(String atividade : atividades) {
						double custoAtividadeAtual = activitiesFrequency.getOrDefault(atividade, 0.0) / somaTotal;
						dictionaryModelMoves2.put(atividade, custoAtividadeAtual);
					}
				}

				//coloca os custos no vetor de custo

				for(int index=0; index<Constants.getActivitiesCostVector().size(); index++) {
					Vector<String> activityVector = Constants.getActivitiesCostVector().elementAt(index);

					for(PetrinetTransition transition  : c) {
						if(activityVector.firstElement().equals(transition.getName())) {
							int value = (int) Math.floor(dictionaryModelMoves2.get(transition.getName()) * 100);
							activityVector.set(1, String.valueOf(value));
							break;
						}
					}
				}
			}

			private void generateLogMoveCosts(Map<String, Double> dictionaryLogMoves) {
				Vector<String> atividadesLog = Constants.getLogActivitiesRepositoryVector();

				for(String atividade : atividadesLog) {
					double frequency = dictionaryLogMoves.get(atividade) / dictionaryLogMoves.get("sum");
					double realFrequency = (1 - frequency) / (atividadesLog.size());
					dictionaryLogMoves.replace(atividade, realFrequency);
				}

				//cria os custos do log aqui
				for(int index=0; index<Constants.getActivitiesCostVector().size(); index++) {
					Vector<String> activityVector = Constants.getActivitiesCostVector().elementAt(index);

					for(String activityLog : atividadesLog) {
						if(activityVector.firstElement().equals(activityLog)) {
							int value = (int) Math.floor(dictionaryLogMoves.get(activityLog) * 100);
							activityVector.set(2, String.valueOf(value));
							break;
						}
					}
				}
			}
		});

		_view.getResetCosts().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				for(int index=0; index<Constants.getActivitiesCostVector().size(); index++) {
					Vector<String> activityVector = Constants.getActivitiesCostVector().elementAt(index);

					String a = activityVector.elementAt(1); 
					if(!a.equals("0")) {
						activityVector.set(1, "1");
						activityVector.set(2, "1");
					}
				}
								
				JOptionPane.showMessageDialog(null, "The costs have been reseted correctly!", "Success!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/success_icon.gif"));   

			}
		});
	}

}
