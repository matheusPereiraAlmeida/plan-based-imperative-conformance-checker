package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import control.H_ResultsPerspective;
import main.Constants;

public class ResultsPerspective extends JDialog {

	private static final long serialVersionUID = 1L;	

	protected H_ResultsPerspective _handler;

	private DefaultStyledDocument resultDocument;
	private JTextPane resultsArea;
	private JScrollPane resultsScrollPane;	
	private JComboBox<String> alignedTracesCombobox;
	private JButton okButton;
	private JButton generateAlignedEvLogButton;
	private JLabel resultsLabel;
	
	private Style style = (new StyleContext()).addStyle("test", null);

	public ResultsPerspective() {
		super();
		initComponent();
		initHandler();
		this.setModal(true);
		this.setVisible(true);
	}

	private void initHandler() {		
		_handler = new H_ResultsPerspective(this);
	}

	private void initComponent() {		
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

		this.add(resultsLabel);	
		this.add(alignedTracesCombobox);	
		this.add(resultsScrollPane);	

		this.add(okButton); 
		this.add(generateAlignedEvLogButton);

		this.setTitle("Event Log Alignment");
		this.setSize(500, 600);
		this.setResizable(true);

		int width = this.getWidth();
		int height = this.getHeight();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width / 2) - (width / 2), (screenSize.height / 2) - (height / 2), width, height);
	}


	/**
	 * Append a String to the results to be presented to the user.
	 * 
	 * @param content
	 * @param color
	 * @throws BadLocationException 
	 */
	public void appendToResults(String content, Color color) throws BadLocationException {
		StyleConstants.setForeground(style, color);
		resultDocument.insertString(resultDocument.getLength(), content, style);
	}


	/**
	 * Display the chosen settings for planner execution to the user.
	 * 
	 * @param style
	 * @param resultsPerspective
	 * @throws BadLocationException
	 */
	public void showPlannerSettings() throws BadLocationException {

		appendToResults(">> EVENT LOG FILE = ", Color.BLACK);
		appendToResults(Constants.getEventLogFileName() + "\n", Color.RED);
		appendToResults(">> SEARCH ALGORITHM = ", Color.BLACK);

		if(Constants.getPlannerPerspective().getOptimalRadioButton().isSelected())
			appendToResults("Blind A* (Cost-Optimal) \n", Color.BLUE);
		else
			appendToResults("Lazy Greedy (Suboptimal) \n", Color.BLUE);

		appendToResults(">> SIZE OF THE EVENT LOG = ", Color.BLACK);
		appendToResults(Constants.getAllTracesVector().size() + "\n", Color.BLUE);
		appendToResults(">> ANALYZE FROM ", Color.BLACK);
		appendToResults("trace#" + _handler.getTraceIdToCheckFrom(), Color.BLUE);
		appendToResults(" TO ", Color.BLACK);
		appendToResults("trace#" + _handler.getTraceIdToCheckTo() + "\n", Color.BLUE);
		appendToResults(">> DISCARD DUPLICATED TRACES = ", Color.BLACK);

		if(Constants.isDiscardDuplicatedTraces())
			appendToResults("YES \n", Color.RED);
		else
			appendToResults("NO \n", Color.BLUE);

		appendToResults(">> ANALYZE TRACES IN THE LOG HAVING LENGHT BETWEEN ", Color.BLACK);
		appendToResults(_handler.getMinTracesLength() + "", Color.BLUE);
		appendToResults(" AND ", Color.BLACK);
		appendToResults(_handler.getMaxTracesLength() + "\n\n", Color.BLUE);
	}


	/**
	 * Display the overview of the results of the planner execution to the user.
	 * 
	 * @param style
	 * @param resultsPerspective
	 * @throws BadLocationException
	 */
	public void showPlannerResultsOverview() throws BadLocationException {

		int totalTracesNumber = _handler.getTraceIdToCheckTo() - _handler.getTraceIdToCheckFrom() + 1;
		int tracesWithFailureNumber = _handler.getTracesWithFailureVector().size();
		int totalAnalyzedTracesNumber = totalTracesNumber - tracesWithFailureNumber;
		float totalAlignmentTime = _handler.getTotalAlignmentTime();
		float totalAlignmentCost = _handler.getTotalAlignmentCost();

		StyleConstants.setBold(style, true);
		appendToResults("\n--- RESULTS OF THE ALIGNMENT ---\n", Color.decode("#009933"));
		StyleConstants.setBold(style, false);

		appendToResults("\n>> NUMBER OF TRACES ANALYZED = ", Color.BLACK);
		appendToResults(totalAnalyzedTracesNumber + "\n", Color.BLUE);
		appendToResults(">> NUMBER OF TRACES REPAIRED = ", Color.BLACK);
		appendToResults(_handler.getAlignedTracesAmount() + "\n", Color.RED);
		appendToResults("\n>> TOTAL ALIGNMENT TIME = ", Color.BLACK);
		appendToResults(totalAlignmentTime + Constants.TIME_UNIT + "\n", Color.BLUE);
		appendToResults(">> AVG ALIGNMENT TIME = ", Color.BLACK);
		appendToResults(totalAlignmentTime/totalAnalyzedTracesNumber + Constants.TIME_UNIT + "\n", Color.BLUE);
		appendToResults("\n>> TOTAL ALIGNMENT COST = ", Color.BLACK);
		appendToResults(totalAlignmentCost + "\n", Color.BLUE);
		appendToResults(">> AVG ALIGNMENT COST = ", Color.BLACK);
		appendToResults(totalAlignmentCost/totalAnalyzedTracesNumber + "\n", Color.BLUE); 
	}


	/* GETTERS & SETTERS */

	public DefaultStyledDocument getResultDocument() {
		return resultDocument;
	}

	public void setResultDocument(DefaultStyledDocument resultDocument) {
		this.resultDocument = resultDocument;
	}

	public JTextPane getResultsArea() {
		return resultsArea;
	}

	public void setResultsArea(JTextPane resultsArea) {
		this.resultsArea = resultsArea;
	}
	
	public void resetResultsArea() {
		resultsArea.setText("");
	}

	public JScrollPane getResultsScrollPane() {
		return resultsScrollPane;
	}

	public void setResultsScrollPane(JScrollPane resultsScrollPane) {
		this.resultsScrollPane = resultsScrollPane;
	}

	public JComboBox<String> getAlignedTracesCombobox() {
		return alignedTracesCombobox;
	}

	public void setAlignedTracesCombobox(JComboBox<String> alignedTracesCombobox) {
		this.alignedTracesCombobox = alignedTracesCombobox;
	}

	public JButton getOkButton() {
		return okButton;
	}

	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}

	public JButton getGenerateAlignedEvLogButton() {
		return generateAlignedEvLogButton;
	}

	public void setGenerateAlignedEvLogButton(JButton generateAlignedEvLogButton) {
		this.generateAlignedEvLogButton = generateAlignedEvLogButton;
	}

	public JLabel getResultsLabel() {
		return resultsLabel;
	}

	public void setResultsLabel(JLabel resultsLabel) {
		this.resultsLabel = resultsLabel;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

}