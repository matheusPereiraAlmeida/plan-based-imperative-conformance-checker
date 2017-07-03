package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultStyledDocument;

import control.H_PetriNetsPerspective;

public class PetriNetsPerspective extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton nextStepButton;
	private JButton previousStepButton;
	private JLabel blankLabel;
	private JLabel blankLabel_2;

	private DefaultStyledDocument document;
	private JTextPane petriNetArea;
	private JScrollPane petriNetScrollPane;	

	protected H_PetriNetsPerspective _handler;

	public PetriNetsPerspective(){
		super();
		initComponent();
		initHandler();		
	}

	private void initComponent() {

		this.setLayout(new FlowLayout());
		this.setBorder(new TitledBorder(null, "STEP 3: Import an existing Petri Net [ --> File --> Import a Petri Net ]", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		blankLabel_2 = new JLabel();
		blankLabel_2.setPreferredSize(new Dimension(550,5));

		blankLabel = new JLabel();
		blankLabel.setPreferredSize(new Dimension(400,20));

		nextStepButton = new JButton("Next Step >");
		nextStepButton.setEnabled(false);
		previousStepButton = new JButton("< Previous Step");

		document = new DefaultStyledDocument();
		petriNetArea = new JTextPane(document);
		petriNetArea.setEditable(false);
		petriNetScrollPane = new JScrollPane(petriNetArea);
		petriNetScrollPane.setPreferredSize(new Dimension(670,105));
		petriNetScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		petriNetScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(petriNetScrollPane);
		this.add(blankLabel_2);
		this.add(previousStepButton);
		this.add(blankLabel);
		this.add(nextStepButton);

		this.setPreferredSize(new Dimension(690,180));
		this.setVisible(true);

	}

	public void setComponentEnabled(boolean enabled) {

		petriNetArea.setEnabled(enabled);
		nextStepButton.setEnabled(enabled);
		previousStepButton.setEnabled(enabled);
		this.setEnabled(enabled);
	}

	public void resetComponent() {

		petriNetArea.setText(new String());

	}

	private void initHandler() {

		_handler = new H_PetriNetsPerspective(this);

	}

	public JButton getNextStepButton() {
		return nextStepButton;
	}

	public JButton getPreviousStepButton() {
		return previousStepButton;
	}

	public JTextPane getPetriNetArea() {
		return petriNetArea;
	}

	public void setNextStepButton(JButton nextStepButton) {
		this.nextStepButton = nextStepButton;
	}

	public void setPreviousStepButton(JButton previousStepButton) {
		this.previousStepButton = previousStepButton;
	}

	public void setPetriNetArea(JTextPane petriNetArea) {
		this.petriNetArea = petriNetArea;
	}

	public DefaultStyledDocument getDocument() {
		return document;
	}

	public void setDocument(DefaultStyledDocument document) {
		this.document = document;
	}
}
