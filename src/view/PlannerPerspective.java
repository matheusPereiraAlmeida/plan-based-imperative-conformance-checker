package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultStyledDocument;

import main.Constants;
import control.H_PlannerPerspective;

public class PlannerPerspective extends JDialog {

	private static final long serialVersionUID = 1L;

	//private JTextArea tasksArea;
	//private JScrollPane tasksScrollPane;	

	private JTextPane petriNetArea;	
	private DefaultStyledDocument petriNetDocument;
	private JScrollPane petriNetScrollPane;

	private JTextPane traceArea;
	private DefaultStyledDocument document;
	private JScrollPane traceScrollPane;

	private ButtonGroup buttonGroup;
	private JRadioButton lazyGreedyRadioButton;
	private JLabel lazyGreedyLabel;
	private JRadioButton optimalRadioButton;	
	private JLabel optimalLabel;

	private JLabel tracesIntervalLabel;
	private JComboBox<String> traceIdComboBoxFROM;
	private JComboBox<String> traceIdComboBoxTO;
	private JCheckBox tracesIntervalCheckBox;	

	private JLabel lenght_of_traces_label;
	private JComboBox<String> lenght_of_traces_ComboBox_FROM;
	private JComboBox<String> lenght_of_traces_ComboBox_TO;
	private JCheckBox lenght_of_traces_checkBox;

	private JLabel trace_duplicated_label;
	private JCheckBox trace_duplicated_checkBox;

	private JLabel initial_final_marking_label;
	private JComboBox<String> markingComboBox;
	private JTextField initialMarkingField;
	private JTextField finalMarkingField;

	private JCheckBox costCheckBox;

	private JComboBox<String> costComboBox;
	private JTextField addingCostField;
	private JTextField removalCostField;

	//private JLabel tasksRepoLabel;
	private JLabel traceLabel;
	private JLabel petriNetLabel;
	private JLabel plannerOptionsLabel;
	private JLabel actionsCostLabel;
	private JLabel blankLabel;
	private JLabel blankLabel_2;
	private JLabel blankLabel_3;
	private JLabel blankLabel_4;
	private JButton runPlannerButton;
	private JButton createDomainAndProblemButton;
	private JButton previousStepButton;

	protected H_PlannerPerspective _handler;

	public PlannerPerspective()
	{
		super();
		initComponent();
		initHandler();
	}

	public void initComponent()
	{		
		Container content = this.getContentPane();
		//content.setBackground(Color.white);

		content.setLayout(new FlowLayout()); 	

		//tasksRepoLabel = new JLabel("<html><u>Activities repository</u></html>");
		//tasksRepoLabel.setPreferredSize(new Dimension(390,25));
		traceLabel = new JLabel("<html><u>Event Log Summary</u></html>");
		traceLabel.setPreferredSize(new Dimension(390,25));
		petriNetLabel = new JLabel("<html><u>Details of the Petri Net</u></html>");
		petriNetLabel.setPreferredSize(new Dimension(390,25));
		plannerOptionsLabel = new JLabel("<html><u>Planner Search Algorithm</u></html>");
		plannerOptionsLabel.setPreferredSize(new Dimension(390,25));
		actionsCostLabel = new JLabel("<html><u>Cost to add/remove activities in/from the trace</u></html>");
		actionsCostLabel.setPreferredSize(new Dimension(350,25));		
		trace_duplicated_label= new JLabel("<html><u>Discard duplicated traces</u></html>");
		trace_duplicated_label.setPreferredSize(new Dimension(350,25));		
		tracesIntervalLabel= new JLabel("<html><u>Select an interval of traces to analyze</u></html>");
		tracesIntervalLabel.setPreferredSize(new Dimension(350,25));
		lenght_of_traces_label = new JLabel("<html><u>Select all the traces having length between</u></html>");
		lenght_of_traces_label.setPreferredSize(new Dimension(350,25));	
		initial_final_marking_label = new JLabel("<html><u>Specify the initial/final marking of the Petri Net</u></html>");
		initial_final_marking_label.setPreferredSize(new Dimension(390,25));

		//tasksArea = new JTextArea();
		//tasksArea.setEditable(false);
		//tasksScrollPane = new JScrollPane(tasksArea);
		//tasksScrollPane.setPreferredSize(new Dimension(390,60));
		//tasksScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//tasksScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		document = new DefaultStyledDocument();
		traceArea = new JTextPane(document);
		traceArea.setEditable(false);

		traceScrollPane = new JScrollPane(traceArea);
		traceScrollPane.setPreferredSize(new Dimension(390,60));
		traceScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		traceScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		petriNetDocument = new DefaultStyledDocument();
		petriNetArea = new JTextPane(petriNetDocument);
		petriNetArea.setEditable(false);

		petriNetScrollPane = new JScrollPane(petriNetArea);
		petriNetScrollPane.setPreferredSize(new Dimension(390,60));
		petriNetScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		petriNetScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		lazyGreedyLabel = new JLabel("Lazy Greedy (Suboptimal)");
		lazyGreedyLabel.setPreferredSize(new Dimension(350,15));
		lazyGreedyRadioButton = new JRadioButton();
		lazyGreedyRadioButton.setPreferredSize(new Dimension(30,15));
		optimalLabel = new JLabel("Blind A* (Cost-Optimal)");
		optimalLabel.setPreferredSize(new Dimension(350,15));
		optimalRadioButton = new JRadioButton();
		optimalRadioButton.setPreferredSize(new Dimension(30,15));
		optimalRadioButton.setSelected(true);

		buttonGroup = new ButtonGroup();
		buttonGroup.add(lazyGreedyRadioButton);
		buttonGroup.add(optimalRadioButton);

		trace_duplicated_checkBox = new JCheckBox();
		trace_duplicated_checkBox.setPreferredSize(new Dimension(30,15));

		tracesIntervalCheckBox = new JCheckBox();
		tracesIntervalCheckBox.setPreferredSize(new Dimension(30,15));

		traceIdComboBoxFROM = new JComboBox<String>();
		traceIdComboBoxFROM.setPreferredSize(new Dimension(180,25));		
		traceIdComboBoxTO = new JComboBox<String>();
		traceIdComboBoxTO.setPreferredSize(new Dimension(180,25));	
		traceIdComboBoxFROM.insertItemAt("-- FROM trace # --", 0);
		traceIdComboBoxTO.insertItemAt("-- TO trace # --", 0);
		for(int kd=0;kd<Constants.getAllTracesVector().size();kd++) {
			int numtr = kd + 1;			
			traceIdComboBoxFROM.insertItemAt("" + numtr, numtr);
			traceIdComboBoxTO.insertItemAt("" + numtr, numtr);
		}	
		traceIdComboBoxFROM.setSelectedIndex(0);
		traceIdComboBoxTO.setSelectedIndex(0);

		traceIdComboBoxFROM.setEnabled(false);
		traceIdComboBoxTO.setEnabled(false);

		///////////////////////////////////////////////////

		lenght_of_traces_checkBox = new JCheckBox();
		lenght_of_traces_checkBox.setPreferredSize(new Dimension(30,15));

		lenght_of_traces_ComboBox_FROM = new JComboBox<String>();
		lenght_of_traces_ComboBox_FROM.setPreferredSize(new Dimension(180,25));		
		lenght_of_traces_ComboBox_TO = new JComboBox<String>();
		lenght_of_traces_ComboBox_TO.setPreferredSize(new Dimension(180,25));	
		lenght_of_traces_ComboBox_FROM.insertItemAt("-- min length --", 0);
		lenght_of_traces_ComboBox_TO.insertItemAt("++ max length ++", 0);

		//////////////////////////////////////////////////

		markingComboBox = new JComboBox<String>();
		markingComboBox.insertItemAt("-- Place name --", 0);		


		markingComboBox.setPreferredSize(new Dimension(180,25));
		markingComboBox.setSelectedIndex(0);		
		initialMarkingField = new JTextField("Initial Marking");
		initialMarkingField.setPreferredSize(new Dimension(90,25));
		finalMarkingField = new JTextField("Final Marking");
		finalMarkingField.setPreferredSize(new Dimension(90,25));

		costCheckBox = new JCheckBox();
		costCheckBox.setPreferredSize(new Dimension(30,15));		
		costCheckBox.setSelected(true);
		costComboBox = new JComboBox<String>();
		costComboBox.insertItemAt("-- Activity name --", 0);

		costComboBox.setPreferredSize(new Dimension(180,25));
		costComboBox.setSelectedIndex(0);		
		costComboBox.setEnabled(true);
		addingCostField = new JTextField("Model move");
		addingCostField.setPreferredSize(new Dimension(90,25));
		addingCostField.setEnabled(true);
		removalCostField = new JTextField("Log move");
		removalCostField.setPreferredSize(new Dimension(90,25));
		removalCostField.setEnabled(true);

		blankLabel = new JLabel();
		blankLabel.setPreferredSize(new Dimension(150,30));
		blankLabel_2 = new JLabel();
		blankLabel_2.setPreferredSize(new Dimension(390,10));
		blankLabel_3 = new JLabel();
		blankLabel_3.setPreferredSize(new Dimension(390,10));
		blankLabel_4 = new JLabel();
		blankLabel_4.setPreferredSize(new Dimension(390,10));
		createDomainAndProblemButton = new JButton("Generate PDDL");
		runPlannerButton = new JButton("Run the Planner");
		previousStepButton = new JButton("< Back");

		//this.add(tasksRepoLabel);	
		//this.add(tasksScrollPane);	
		this.add(traceLabel);	
		this.add(traceScrollPane);
		this.add(petriNetLabel);	
		this.add(petriNetScrollPane);    
		this.add(plannerOptionsLabel);	 
		this.add(optimalLabel);
		this.add(optimalRadioButton);
		this.add(lazyGreedyLabel);
		this.add(lazyGreedyRadioButton);	


		this.add(initial_final_marking_label);
		this.add(markingComboBox);
		this.add(initialMarkingField);
		this.add(finalMarkingField);

		//this.add(LPGlabel);
		//this.add(LPGCheckBox);
		this.add(blankLabel_3); 
		this.add(tracesIntervalLabel);
		this.add(tracesIntervalCheckBox);
		this.add(traceIdComboBoxFROM);
		this.add(traceIdComboBoxTO);
		this.add(blankLabel_3); 
		this.add(trace_duplicated_label);
		this.add(trace_duplicated_checkBox);	    
		this.add(blankLabel_3); 
		this.add(lenght_of_traces_label);
		this.add(lenght_of_traces_checkBox);
		this.add(lenght_of_traces_ComboBox_FROM);
		this.add(lenght_of_traces_ComboBox_TO);   
		this.add(blankLabel_3); 
		this.add(actionsCostLabel);	
		this.add(costCheckBox);	
		this.add(costComboBox);
		this.add(addingCostField);
		this.add(removalCostField);
		this.add(blankLabel_3); 
		this.add(previousStepButton); 
		this.add(createDomainAndProblemButton); 
		// this.add(blankLabel); 
		this.add(runPlannerButton); 

		this.setTitle("STEP 4: Customize the alignment");
		this.setSize(400, 650);
		this.setResizable(false);

		int width = this.getWidth();
		int height = this.getHeight();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width / 2) - (width / 2), (screenSize.height / 2) - (height / 2), width, height);

	}

	private void initHandler() {
		_handler = new H_PlannerPerspective(this);
	}

	/*
	public JTextArea getTasksArea() {
		return tasksArea;
	}
	 */
	
	/* GETTERS & SETTERS */
	
	public JTextPane getPetriNetArea() {
		return petriNetArea;
	}

	public JTextPane getTraceArea() {
		return traceArea;
	}

	public JButton getRunPlannerButton() {
		return runPlannerButton;
	}

	public JButton getPreviousStepButton() {
		return previousStepButton;
	}

	public JRadioButton getLazyGreedyRadioButton() {
		return lazyGreedyRadioButton;
	}


	public JRadioButton getOptimalRadioButton() {
		return optimalRadioButton;
	}

	public JCheckBox getCostCheckBox() {
		return costCheckBox;
	}

	public JComboBox<String> getTraceIdComboBoxFROM() {
		return traceIdComboBoxFROM;
	}

	public JComboBox<String> getTraceIdComboBoxTO() {
		return traceIdComboBoxTO;
	}

	public JCheckBox getNumberOfTracesCheckBox() {
		return tracesIntervalCheckBox;
	}

	public JComboBox<String> getCostComboBox() {
		return costComboBox;
	}

	public void setCostComboBox(JComboBox<String> tasksComboBox) {
		this.costComboBox = tasksComboBox;
	}

	public JTextField getAddingCostField() {
		return addingCostField;
	}

	public void setAddingCostField(JTextField addingCostField) {
		this.addingCostField = addingCostField;
	}

	public JTextField getRemovalCostField() {
		return removalCostField;
	}

	public void setRemovalCostField(JTextField removalCostField) {
		this.removalCostField = removalCostField;
	}

	public JComboBox<String> getMarkingComboBox() {
		return markingComboBox;
	}

	public JTextField getInitialMarkingField() {
		return initialMarkingField;
	}

	public JTextField getFinalMarkingField() {
		return finalMarkingField;
	}

	public void setMarkingComboBox(JComboBox<String> markingComboBox) {
		this.markingComboBox = markingComboBox;
	}

	public void setInitialMarkingField(JTextField initialMarkingField) {
		this.initialMarkingField = initialMarkingField;
	}

	public void setFinalMarkingField(JTextField finalMarkingField) {
		this.finalMarkingField = finalMarkingField;
	}

	public JButton getCreateDomainAndProblemButton() {
		return createDomainAndProblemButton;
	}

	public void setCreateDomainAndProblemButton(JButton createDomainAndProblemButton) {
		this.createDomainAndProblemButton = createDomainAndProblemButton;
	}

	public JComboBox<String> getLenghtOfTracesComboBoxFROM() {
		return lenght_of_traces_ComboBox_FROM;
	}

	public JComboBox<String> getLenghtOfTracesComboBoxTO() {
		return lenght_of_traces_ComboBox_TO;
	}

	public JCheckBox getLenghtOfTracesCheckBox() {
		return lenght_of_traces_checkBox;
	}

	public void setLenghtOfTracesComboBoxFROM(
			JComboBox<String> lenght_of_traces_ComboBox_FROM) {
		this.lenght_of_traces_ComboBox_FROM = lenght_of_traces_ComboBox_FROM;
	}

	public void setLenghtOfTracesComboBoxTO(
			JComboBox<String> lenght_of_traces_ComboBox_TO) {
		this.lenght_of_traces_ComboBox_TO = lenght_of_traces_ComboBox_TO;
	}

	public void setLenghtOfTracesCheckBox(JCheckBox lenght_of_traces_checkBox) {
		this.lenght_of_traces_checkBox = lenght_of_traces_checkBox;
	}

	public JCheckBox getTraceDuplicatedCheckBox() {
		return trace_duplicated_checkBox;
	}

	public void setTraceDuplicatedCheckBox(JCheckBox trace_duplicated_checkBox) {
		this.trace_duplicated_checkBox = trace_duplicated_checkBox;
	}

	public DefaultStyledDocument getDocument() {
		return document;
	}

	public void setDocument(DefaultStyledDocument document) {
		this.document = document;
	}

	public DefaultStyledDocument getPetriNetDocument() {
		return petriNetDocument;
	}	
}