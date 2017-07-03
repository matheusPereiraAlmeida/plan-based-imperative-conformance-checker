package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import main.Constants;

public class Desktop extends JFrame {
	private static final long serialVersionUID = 1L;

	private MenuPerspective menuBar;
	private AlphabetPerspective alphabetPanel;
	private TracePerspective tracePanel;
	private PetriNetsPerspective pnPanel;

	public Desktop() {
		super();
		initComponent();
	}

	public void initComponent() {		
		Container content = this.getContentPane();

		content.setLayout(new FlowLayout()); 	

		menuBar = new MenuPerspective();	    
		alphabetPanel = new AlphabetPerspective();
		tracePanel = new TracePerspective();
		pnPanel = new PetriNetsPerspective();

		Constants.setMenuPerspective(menuBar);
		Constants.setAlphabetPerspective(alphabetPanel);
		Constants.setTracePerspective(tracePanel);
		Constants.setPetriNetPerspective(pnPanel);

		Constants.getTracePerspective().setComponentEnabled(false);
		Constants.getPetriNetPerspective().setComponentEnabled(false);

		this.setJMenuBar(menuBar);
		this.add(alphabetPanel);	
		this.add(tracePanel);	
		this.add(pnPanel);	

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);		
		this.setTitle("Plan-based Alignment Tool for Procedural Processes");

		Constants.setDesktop(this);

		this.setSize(700, 660);
		this.setResizable(false);
		this.setVisible(true);

		int width = this.getWidth();
		int height = this.getHeight();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width / 2) - (width / 2), (screenSize.height / 2) - (height / 2), width, height);
	}
}