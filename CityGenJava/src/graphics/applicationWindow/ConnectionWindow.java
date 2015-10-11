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
		super("Connection Window");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(300,200));

		setupDisplay();

		pack();
		setResizable(false);
		setVisible(true);
	}

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
			}
		});


		add(display);

	}
}
