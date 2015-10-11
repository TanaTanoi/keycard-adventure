package graphics.applicationWindow;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ConnectionWindow extends JFrame{
	public ConnectionWindow(){
		super("Connection Window");//Give the window a title
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//Should only be closable if the player has entered a name and IP
		
		setLocationRelativeTo(null);//center the window
		setLocation(getLocation().x-150, getLocation().y-100);
		setMinimumSize(new Dimension(300,200));
		
		setupDisplay();

		pack();
		
		setResizable(false);
		setVisible(true);
	}

	
	/**
	 * Adds all buttons and text fields to the main connection window
	 */
	private void setupDisplay() {
		JPanel display = new JPanel(new GridLayout(5,1));

		JTextField playerName = new JTextField();
		JTextField IP = new JTextField();

		display.add(new JLabel("Player Name:"));
		display.add(playerName);
		display.add(new JLabel("IP Address:"));
		display.add(IP);

		JButton completed = new JButton(new AbstractAction("Enter") {
			public void actionPerformed(ActionEvent e){
				String name = playerName.getText();
				String address = IP.getText();
				//
				//TODO code to deal with network here
				//
				dispose();
			}
		});
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));//To get the button to sit nicely in the middle of the window
		buttonPanel.add(new JLabel());
		buttonPanel.add(completed);
		buttonPanel.add(new JLabel());
		
		display.add(buttonPanel);
		
		add(display);
	}
}
