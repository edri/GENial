package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import application.App;

public class TwoChoicesPanel extends JPanel {
	private JPanel mainPanel; // TODO test si besoin pour positionnemenet
	private JPanel firstPanel;
	private JPanel secondPanel;
	private JButton choiceOne;
	private JButton choiceTwo;
	
	private App app;
	
	public TwoChoicesPanel(App app){
		this.app = app;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		
		firstPanel = new JPanel();
		choiceOne = new JButton("S'identifier");
		choiceOne.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		firstPanel.add(choiceOne);
		
		secondPanel = new JPanel();
		choiceTwo = new JButton("S'enregistrer");
		choiceTwo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		secondPanel.add(choiceTwo);
		
		mainPanel.add(firstPanel);
		mainPanel.add(secondPanel);
		this.add(mainPanel);
	}

}
