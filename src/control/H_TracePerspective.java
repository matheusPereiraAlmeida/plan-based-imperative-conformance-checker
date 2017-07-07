package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import main.Constants;
import main.Trace;
import view.TracePerspective;

public class H_TracePerspective {

	public TracePerspective _view = null;

	public H_TracePerspective (TracePerspective i_view){
		_view = i_view;
		installListeners();
	}

	private void installListeners() {	

		_view.getRightButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getAlphabetList().getSelectedIndex() == -1) {            		
					JOptionPane.showMessageDialog(null, "Please select an activity to be inserted in the trace!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				}   
				else if(_view.getTracesComboBox().getSelectedItem().toString().equalsIgnoreCase(" --- ")) {            		
					JOptionPane.showMessageDialog(null, "Please select a valid trace where to insert the activity!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				}   
				else {
					String selected_element = (String) _view.getAlphabetList().getSelectedValue();
					_view.getTraceListModel().addElement(selected_element);     

					//Update the selected trace and rebuilds every time the "structures" associated to the trace
					String selected_trace_name = (String) _view.getTracesComboBox().getSelectedItem();
					Vector<Trace> all_traces_vector = Constants.getAllTracesVector();

					for(int h=0;h<all_traces_vector.size();h++) {
						Trace t = all_traces_vector.elementAt(h);

						if(t.getTraceName().equalsIgnoreCase(selected_trace_name)) {
							updateTrace(t);
							break;
						}
					}
				}
			}
		});

		_view.getRemoveButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getTraceList().getSelectedIndex() == -1) { //no activity selected
					JOptionPane.showMessageDialog(null, "Please select an activity to be removed from the trace!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				} 
				else {
					int index = _view.getTraceList().getSelectedIndex();   	
					_view.getTraceListModel().removeElementAt(index);

					//Update the selected trace and rebuilds every time the "structures" associated to the trace
					String selected_trace_name = (String) _view.getTracesComboBox().getSelectedItem();
					Vector<Trace> all_traces_vector = Constants.getAllTracesVector();

					for(int h=0;h<all_traces_vector.size();h++) {
						Trace t = all_traces_vector.elementAt(h);

						if(t.getTraceName().equalsIgnoreCase(selected_trace_name)) {
							updateTrace(t);
							break;
						}
					}
				}
			}
		});

		_view.getUpButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getTraceList().getSelectedIndex() == -1) {            		
					JOptionPane.showMessageDialog(null, "Please select which activity of the trace you want to move!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				}            	
				else {

					if(_view.getTraceList().getSelectedIndex() > 0) {

						int index = _view.getTraceList().getSelectedIndex();
						String element = (String) _view.getTraceList().getSelectedValue();

						int index_down = index -1;
						_view.getTraceListModel().removeElementAt(index);
						_view.getTraceListModel().insertElementAt(element, index_down);
						_view.getTraceList().setSelectedIndex(index_down);


						//Update the selected trace and rebuilds every time the "structures" associated to the trace
						String selected_trace_name = (String) _view.getTracesComboBox().getSelectedItem();
						Vector<Trace> all_traces_vector = Constants.getAllTracesVector();

						for(int h=0;h<all_traces_vector.size();h++) {
							Trace t = all_traces_vector.elementAt(h);

							if(t.getTraceName().equalsIgnoreCase(selected_trace_name)) {
								updateTrace(t);
								break;
							}
						}
					}
				}

			}
		});

		_view.getDownButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getTraceList().getSelectedIndex() == -1) {            		
					JOptionPane.showMessageDialog(null, "Please select the activity of the trace you want to move!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				}            	
				else {

					if(_view.getTraceList().getSelectedIndex() + 1 < _view.getTraceListModel().size()) {

						int index = _view.getTraceList().getSelectedIndex();
						String element = (String) _view.getTraceList().getSelectedValue();

						int index_up = index + 1;
						_view.getTraceListModel().removeElementAt(index);
						_view.getTraceListModel().insertElementAt(element, index_up);
						_view.getTraceList().setSelectedIndex(index_up);

						//Update the selected trace and rebuilds every time the "structures" associated to the trace
						String selected_trace_name = (String) _view.getTracesComboBox().getSelectedItem();
						Vector<Trace> all_traces_vector = Constants.getAllTracesVector();

						for(int h=0;h<all_traces_vector.size();h++) {
							Trace t = all_traces_vector.elementAt(h);

							if(t.getTraceName().equalsIgnoreCase(selected_trace_name)) {
								updateTrace(t);
								break;
							}
						}
					}
				}

			}
		});

		_view.getInfoButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{

				Vector<Trace> all_traces_vector = Constants.getAllTracesVector();

				int total_number_of_events = 0;

				String min_trace_name = "";
				String max_trace_name = "";

				int min_trace_length = 0;
				int max_trace_length = 0;

				Vector<String> ev_log_alphabet_vector = new Vector<String>();

				for(int h=0;h<all_traces_vector.size();h++) {
					Trace t = all_traces_vector.elementAt(h);

					for(int u = 0;u<t.getTraceAlphabet().size();u++)  {
						if(!ev_log_alphabet_vector.contains(t.getTraceAlphabet().elementAt(u)))
							ev_log_alphabet_vector.addElement(t.getTraceAlphabet().elementAt(u));
					}


					if(h==0) {
						min_trace_length = t.getTraceContentVector().size();
						min_trace_name = t.getTraceName(); 
					}

					if(t.getTraceContentVector().size() < min_trace_length) {
						min_trace_name = t.getTraceName(); 
						min_trace_length = t.getTraceContentVector().size();
					}

					if(t.getTraceContentVector().size() > max_trace_length) {
						max_trace_name = t.getTraceName(); 
						max_trace_length = t.getTraceContentVector().size();
					}

					total_number_of_events += t.getTraceContentVector().size();

				}

				JOptionPane.showMessageDialog(null, new JLabel("<html><p style=\"font-size:12px\"><i><u>Event Log Summary</u></i></p><br/>" +
						"- <i>Event Log file: </i><font color=red>" + Constants.getEventLogFileName() + "</font>&nbsp;&nbsp;&nbsp;&nbsp;<br />" +
						"- <i>Total number of traces: </i><font color=blue>" + all_traces_vector.size() + "</font> <br />" +
						"- <i>Total number of events: </i><font color=blue>" + total_number_of_events + "</font> <br />" +
						"- <i>Average trace length: </i><font color=blue>" + (float) total_number_of_events/all_traces_vector.size() + "</font> <br/>" +
						"- <i>Max trace length: </i><font color=blue>" + max_trace_length + "</font> [ " + max_trace_name + " ] <br/>" +
						"- <i>Min trace length: </i><font color=blue>" + min_trace_length + "</font> [ " + min_trace_name + " ] <br/>" +
						"- <i>Number of activities involved: </i><font color=blue>" + ev_log_alphabet_vector.size() + "</font>&nbsp;&nbsp;&nbsp;&nbsp; <br/><br>" +
						"</html>"), "Event Log Summary", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));


			}
		});

		_view.getEditTraceButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{

				String[] options = new String[] {"Create", "Remove", "Cancel"};
				int response = JOptionPane.showOptionDialog(null, "Do you want to create a new trace, \nor to remove an existing one?", "Create/Remove Traces",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("images/question_icon.png"), options, options[0]);

				// -- Create a new trace
				if(response==0) {
					int number_of_elements_in_combo_box = _view.getTracesComboBox().getItemCount();

					String new_item = new String();

					for(int i=1;i<=number_of_elements_in_combo_box;i++) {
						new_item = "Trace#" + i;            	    		
						String item = _view.getTracesComboBox().getItemAt(i);
						if(!new_item.equalsIgnoreCase(item)) {
							_view.getTracesComboBox().insertItemAt(new_item, i);
							_view.getTracesComboBox().setSelectedIndex(i);
							_view.resetTraceListModel();
							break;
						}
					}
					Constants.getAllTracesVector().addElement(new Trace(new_item));
				}

				// -- Remove an existing trace
				else if(response==1) {
					String old_item = (String) _view.getTracesComboBox().getSelectedItem();

					if (old_item.equalsIgnoreCase(" --- "))
						JOptionPane.showMessageDialog(null, "Please select a valid trace to remove!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
					else {
						_view.getTracesComboBox().removeItem(old_item);
						_view.resetTraceListModel();
						_view.getTracesComboBox().setSelectedItem(" --- ");

						//Remove the selected trace from the vector of traces
						Vector<Trace> all_traces_vector = Constants.getAllTracesVector();

						for(int h=0;h<all_traces_vector.size();h++) {
							Trace t = all_traces_vector.elementAt(h);

							if(t.getTraceName().equalsIgnoreCase(old_item)) {
								Constants.getAllTracesVector().removeElementAt(h);
								break;
							}
						}

						JOptionPane.showMessageDialog(null, old_item + " correctly removed!", "Confirmation!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
					}
				}
			}
		});


		_view.getTracesComboBox().addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				String selected_trace_name = (String) _view.getTracesComboBox().getSelectedItem();

				if (event.getStateChange() == ItemEvent.SELECTED && !(selected_trace_name.equalsIgnoreCase(" --- ")) )
				{
					_view.resetTraceListModel();

					Vector<Trace> all_traces_vector = Constants.getAllTracesVector();

					for(int h=0;h<all_traces_vector.size();h++) {
						Trace t = all_traces_vector.elementAt(h);

						if(t.getTraceName().equalsIgnoreCase(selected_trace_name)) {

							for(int index=0;index<t.getTraceContentVector().size();index++) {
								_view.getTraceListModel().addElement(t.getTraceContentVector().elementAt(index));
							}

							break;
						}
					}

				}
				else if(event.getStateChange() == ItemEvent.SELECTED && (selected_trace_name.equalsIgnoreCase(" --- "))) {
					_view.resetTraceListModel();
				}
			}
		});

		_view.getNextStepButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(Constants.getAllTracesVector().size()==0)  {
					JOptionPane.showMessageDialog(null, "There is no trace defined for the log!\nAt least a trace (even if empty) is required to run the software!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				}
				else {
					Constants.getAlphabetPerspective().setComponentEnabled(false);
					Constants.getTracePerspective().setComponentEnabled(false);
					Constants.getPetriNetPerspective().setComponentEnabled(true);

					if(Constants.getPetriNetPerspective().getPetriNetArea().getText().isEmpty())
						Constants.getPetriNetPerspective().getNextStepButton().setEnabled(false);

				}

				Constants.getMenuPerspective().getImportPetriNetMenuItem().setEnabled(true);
			}
		});

		_view.getPreviousStepButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
			{
				Constants.getAlphabetPerspective().setComponentEnabled(true);
				Constants.getTracePerspective().setComponentEnabled(false);

			}
		});

	}

	private void updateTrace(Trace trace) {

		trace.setTraceContentVector(new Vector<String>());
		trace.setTraceTextualContent(new StringBuffer());
		trace.setTraceAlphabet(new Vector<String>());

		if(!Constants.getEventLogFileName().equalsIgnoreCase("Created from scratch") && !Constants.getEventLogFileName().contains("(modified by the user)"))
			Constants.setEventLogFileName(Constants.getEventLogFileName() + " (modified by the user)");

		for(int j=0;j<_view.getTraceListModel().size();j++) {
			String string = (String) _view.getTraceListModel().getElementAt(j);
			trace.getTraceContentVector().addElement(string);

			if(!trace.getTraceAlphabet().contains(string)) {
				trace.getTraceAlphabet().addElement(string);
			}

			trace.getTraceTextualContent().append(string);
			if(j<_view.getTraceListModel().size()-1)
				trace.getTraceTextualContent().append(",");
		}

	}

}
