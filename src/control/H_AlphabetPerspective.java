package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import main.Constants;
import main.Trace;
import main.Utilities;
import view.AlphabetPerspective;

public class H_AlphabetPerspective {

	private AlphabetPerspective _view = null;

	public H_AlphabetPerspective (AlphabetPerspective i_view){
		_view = i_view;
		installListeners();
	}

	private void installListeners() {

		_view.getNextStepButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getAlphabetListModel().getSize()>0) {
					Constants.getAlphabetPerspective().setComponentEnabled(false);

					// Update Constants.log_activities_repository_vector -- 1
					Constants.setLogActivitiesRepositoryVector(new Vector<String>()); 

					for(int i=0;i<_view.getAlphabetListModel().getSize();i++) {
						String string = (String) _view.getAlphabetListModel().getElementAt(i);

						// Update the alphabet of activities for the second panel that manages traces (TracePerspective) -- 2
						if(!Constants.getTracePerspective().getAlphabetListModel().contains(string))
							Constants.getTracePerspective().getAlphabetListModel().addElement(string);

						// Update Constants.log_activities_repository_vector -- 3
						Constants.getLogActivitiesRepositoryVector().addElement(string);            			
					}

					Constants.getTracePerspective().setComponentEnabled(true);
				}
				else {
					JOptionPane.showMessageDialog(null, "The repository of activities can not be empty!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				}
			}
		});

		_view.getRightButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(_view.getTaskField().getText().equalsIgnoreCase("") || 
						_view.getTaskField().getText().equalsIgnoreCase(" ") ||	_view.getTaskField().getText().contains(" ")) {            		
					JOptionPane.showMessageDialog(null, "The name of the activity can not be empty and can not contain blank spaces!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png")); 		
				}
				else if(Utilities.isUpperCase(_view.getTaskField().getText()))
					JOptionPane.showMessageDialog(null, "Please use only lower space characters for the activity name!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));            	
				else if(_view.getTaskField().getText().charAt(0) >= '0' && _view.getTaskField().getText().charAt(0) <= '9')           	
					JOptionPane.showMessageDialog(null, "It is not allowed to use an Integer as the first character of the activity name!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				else {

					boolean task_name_already_exists = false;

					for(int i=0;i<_view.getAlphabetListModel().getSize();i++) {
						String string = (String) _view.getAlphabetListModel().getElementAt(i);
						if(string.equalsIgnoreCase(_view.getTaskField().getText())) {
							JOptionPane.showMessageDialog(null, "The activity '" + _view.getTaskField().getText() + "' already exists. Please choose a different name for the activity!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
							task_name_already_exists = true;
							break;
						}
					}

					if(!task_name_already_exists){
						_view.getAlphabetListModel().addElement(_view.getTaskField().getText());
						_view.getTaskField().setText("");
					}
				}

			}
		});

		_view.getRemoveButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{

				if(_view.getAlphabetList().getSelectedIndex() == -1) { //no task selected
					JOptionPane.showMessageDialog(null, "Please select an activity to be removed from the repository!", "Attention!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/info_icon.png"));
				} 
				else {
					int index = _view.getAlphabetList().getSelectedIndex();

					//Task selected for the deletion
					String elem = (String) _view.getAlphabetList().getSelectedValue();

					_view.getAlphabetListModel().removeElementAt(index);

					// Remove the selected task from the alphabet of activities in the second panel that manages traces (TracePerspective)
					if(Constants.getTracePerspective().getAlphabetListModel().contains(elem))
						Constants.getTracePerspective().getAlphabetListModel().removeElement(elem);                		              		

					// Remove the selected task from any of the already specified traces
					for(int k=0;k<Constants.getAllTracesVector().size();k++) {

						Trace trace = Constants.getAllTracesVector().elementAt(k);

						for(int g=0;g<trace.getTraceContentVector().size();g++) {

							String item = trace.getTraceContentVector().elementAt(g);

							if(item.equalsIgnoreCase(elem)) {
								trace.getTraceContentVector().removeElement(elem);
								g--;
							}
						}
					}

					// Remove the selected task from the list of activities visualized for the actually selected trace in the second panel (TracePerspective)
					while(Constants.getTracePerspective().getTraceListModel().contains(elem))
						Constants.getTracePerspective().getTraceListModel().removeElement(elem);

				}
			}
		});

	}

}
