package view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import control.H_MenuPerspective;

public class MenuPerspective extends JMenuBar{

	private static final long serialVersionUID = 1L;

	private JMenu fileMenu;

	private JMenuItem newMenuItem;
	private JMenuItem openXESMenuItem;
	private JMenuItem importPetriNetMenuItem;
	private JMenuItem compareResults;
	private JMenuItem exitMenuItem;

	protected H_MenuPerspective _handler;

	public MenuPerspective(){
		super();
		initComponent();
		initHandler();		
	}

	private void initComponent() {

		fileMenu = new JMenu("File");

		newMenuItem = new JMenuItem("New ");	  
		openXESMenuItem = new JMenuItem("Open XES File ");
		importPetriNetMenuItem  = new JMenuItem("Import a Petri Net ");
		compareResults = new JMenuItem("Compare results");

		newMenuItem.setEnabled(true);
		openXESMenuItem.setEnabled(true);
		importPetriNetMenuItem.setEnabled(false);
		compareResults.setEnabled(true);
		
		exitMenuItem = new JMenuItem("Exit ");

		fileMenu.add(newMenuItem);
		fileMenu.add(openXESMenuItem);
		fileMenu.add(importPetriNetMenuItem);
		fileMenu.add(compareResults);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		this.add(fileMenu);


	}

	private void initHandler() {
		_handler = new H_MenuPerspective(this);
	}

	public JMenuItem getNewMenuItem() {
		return newMenuItem;
	}

	public void setNewMenuItem(JMenuItem newMenuItem) {
		this.newMenuItem = newMenuItem;
	}

	public JMenuItem getOpenMenuItem() {
		return openXESMenuItem;
	}

	public void setOpenMenuItem(JMenuItem openMenuItem) {
		this.openXESMenuItem = openMenuItem;
	}

	public JMenuItem getImportPetriNetMenuItem() {
		return importPetriNetMenuItem;
	}

	public void setImportPetriNetMenuItem(JMenuItem importPetriNetMenuItem) {
		this.importPetriNetMenuItem = importPetriNetMenuItem;
	}

	public JMenuItem getExitMenuItem() {
		return exitMenuItem;
	}

	public void setExitMenuItem(JMenuItem saveMenuItem) {
		this.exitMenuItem = saveMenuItem;
	}

	public JMenuItem getCompareResults() {
		return compareResults;
	}

	public void setCompareResults(JMenuItem saveCompareResults) {
		this.compareResults = saveCompareResults;
	}
}
