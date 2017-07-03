package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import main.JLinkButton;
import control.H_TracePerspective;

public class TracePerspective extends JPanel{

	private static final long serialVersionUID = 1L;

	private JPanel addTaskButtonPanel;
	private JPanel moveTasksInTheTraceButtonPanel;

	private JLabel firstBoxLabel;
	private JLabel secondBoxLabel;
	private JLabel blankLabel;

	private DefaultListModel<String> alphabetListModel;
	private JList<String> alphabetList;
	private JScrollPane alphabetScrollPane;

	private JComboBox<String> tracesComboBox;

	private DefaultListModel<String> traceListModel;
	private JList<String> traceList;
	private JScrollPane traceScrollPane;

	private JButton rightButton;
	private JButton removeButton;
	private JButton upButton;
	private JButton downButton;
	private JButton infoButton;

	private JLinkButton editTraceButton;

	private JButton nextStepButton;
	private JButton previousStepButton;

	protected H_TracePerspective _handler;

	public TracePerspective(){
		super();
		initComponent();
		initHandler();		
	}

	private void initComponent() {

		this.setLayout(new FlowLayout());
		this.setBorder(new TitledBorder(null, "STEP 2: Create traces of activities taken from the repository", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		//
		// JLabels on top of the JFrame
		//
		firstBoxLabel = new JLabel("Repository of activities");
		firstBoxLabel.setPreferredSize(new Dimension(385,15));		

		secondBoxLabel  = new JLabel("Activities in the Trace");
		secondBoxLabel.setPreferredSize(new Dimension(280,15));

		//
		// Alphabet of the tasks
		//
		alphabetListModel = new DefaultListModel<String>();
		alphabetList = new JList<String>(alphabetListModel);
		alphabetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		alphabetList.setSelectedIndex(-1);

		alphabetScrollPane = new JScrollPane(alphabetList);
		alphabetScrollPane.setPreferredSize(new Dimension(255,90));
		alphabetScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		alphabetScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		//
		// JPanel containing the JButtons for creating new traces and for adding/removing tasks from specific traces
		//
		addTaskButtonPanel = new JPanel();
		addTaskButtonPanel.setPreferredSize(new Dimension(120,110)); 
		addTaskButtonPanel.setLayout(new FlowLayout());

		rightButton = new JButton("ADD>>");
		rightButton.setPreferredSize(new Dimension(110,25)); 
		removeButton = new JButton("<<DEL");
		removeButton.setPreferredSize(new Dimension(110,25));

		tracesComboBox = new JComboBox<String>();
		tracesComboBox.setPreferredSize(new Dimension(110,25)); 
		tracesComboBox.addItem(" --- ");

		editTraceButton = new JLinkButton();
		editTraceButton.setText("Create/Del");
		editTraceButton.setPreferredSize(new Dimension(110,15)); 

		addTaskButtonPanel.add(rightButton);
		addTaskButtonPanel.add(removeButton);
		addTaskButtonPanel.add(tracesComboBox);
		addTaskButtonPanel.add(editTraceButton);

		//
		// JPanel containing the JTextField for adding the name of the tasks
		//

		traceListModel = new DefaultListModel<String>();
		traceList = new JList<String>(traceListModel);
		traceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		traceList.setSelectedIndex(-1);

		traceScrollPane = new JScrollPane(traceList);
		traceScrollPane.setPreferredSize(new Dimension(230,90));
		traceScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		traceScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		//
		// JPanel containing the JTextField for adding the name of the tasks
		//

		moveTasksInTheTraceButtonPanel = new JPanel();
		moveTasksInTheTraceButtonPanel.setPreferredSize(new Dimension(45,110)); 
		moveTasksInTheTraceButtonPanel.setLayout(new FlowLayout());

		upButton = new JButton("\u21D1");
		upButton.setPreferredSize(new Dimension(45,30)); 
		downButton = new JButton("\u21D3");
		downButton.setPreferredSize(new Dimension(45,30));		
		infoButton = new JButton("\u24D8");
		infoButton.setPreferredSize(new Dimension(45,25));

		moveTasksInTheTraceButtonPanel.add(upButton);
		moveTasksInTheTraceButtonPanel.add(downButton);
		moveTasksInTheTraceButtonPanel.add(infoButton);

		blankLabel = new JLabel();
		blankLabel.setPreferredSize(new Dimension(400,20));
		nextStepButton = new JButton("Next Step >");
		previousStepButton = new JButton("< Previous Step");

		this.add(firstBoxLabel);
		this.add(secondBoxLabel);
		this.add(alphabetScrollPane);
		this.add(addTaskButtonPanel);
		this.add(traceScrollPane);
		this.add(moveTasksInTheTraceButtonPanel);
		this.add(previousStepButton);
		this.add(blankLabel);
		this.add(nextStepButton);
		this.setPreferredSize(new Dimension(690,200));
		this.setVisible(true);

	}

	public void setComponentEnabled(boolean enabled) {

		firstBoxLabel.setEnabled(enabled);
		secondBoxLabel.setEnabled(enabled);
		traceList.setEnabled(enabled);
		traceScrollPane.setEnabled(enabled);
		rightButton.setEnabled(enabled);
		removeButton.setEnabled(enabled);
		tracesComboBox.setEnabled(enabled);
		editTraceButton.setEnabled(enabled);
		alphabetList.setEnabled(enabled);
		alphabetScrollPane.setEnabled(enabled);
		upButton.setEnabled(enabled);
		downButton.setEnabled(enabled);	
		infoButton.setEnabled(enabled);
		nextStepButton.setEnabled(enabled);
		previousStepButton.setEnabled(enabled);
		this.setEnabled(enabled);
	}

	public void resetComponent() {

		alphabetListModel.removeAllElements();
		traceListModel.removeAllElements();
		tracesComboBox.removeAllItems();
		tracesComboBox.addItem(" --- ");

	}

	private void initHandler() {

		_handler = new H_TracePerspective(this);

	}

	public DefaultListModel<String> getAlphabetListModel() {
		return alphabetListModel;
	}

	public JList<String> getAlphabetList() {
		return alphabetList;
	}

	public DefaultListModel<String> getTraceListModel() {
		return traceListModel;
	}

	public void resetTraceListModel() {
		traceListModel.removeAllElements();
	}

	public JList<String> getTraceList() {
		return traceList;
	}

	public JButton getRightButton() {
		return rightButton;
	}

	public JButton getRemoveButton() {
		return removeButton;
	}

	public JComboBox<String> getTracesComboBox() {
		return tracesComboBox;
	}

	public JButton getUpButton() {
		return upButton;
	}

	public JButton getDownButton() {
		return downButton;
	}

	public JButton getInfoButton() {
		return infoButton;
	}

	public JButton getNextStepButton() {
		return nextStepButton;
	}

	public JButton getPreviousStepButton() {
		return previousStepButton;
	}

	public JLinkButton getEditTraceButton() {
		return editTraceButton;
	}

}
