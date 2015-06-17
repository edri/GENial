package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import application.App;

public class RollPopupFrame extends JFrame {
	private JPanel mainPanel;
	private JPanel textPanel;
	private JLabel text;
	private JPanel buttonPanel;
	private JButton button;
	
	public RollPopupFrame(App app, String str){
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		textPanel = new JPanel();
		text = new JLabel(str);
		textPanel.add(text);
		
		buttonPanel = new JPanel();
		button = new JButton("OK");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				app.sendRoll();
			}
		});
		buttonPanel.add(button);
		
		mainPanel.add(textPanel);
		mainPanel.add(buttonPanel);
		add(mainPanel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
}
