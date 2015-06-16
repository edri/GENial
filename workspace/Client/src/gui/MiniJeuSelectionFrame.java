package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import application.App;
import miniJeux.MiniJeu;

public class MiniJeuSelectionFrame extends JFrame implements ActionListener {
	private App app;
	private ButtonGroup buttonGroup;
	private JPanel radioPanel;
	private JPanel mainPanel;
	private JPanel buttonPanel;
	private JButton chooseButton;
	private String selectedGame;
	
	public MiniJeuSelectionFrame(App app, Map<String, MiniJeu> map){
		this.setTitle("GENial, selectionnez le mini-jeu a lancer");
		this.app = app;
		boolean firstCheckButton = true;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		radioPanel = new JPanel(new GridLayout(0,1));
		buttonGroup = new ButtonGroup();
		for (String s : map.keySet()){
			createCheckButton(s, firstCheckButton);
			if (firstCheckButton){
				firstCheckButton = false;
				selectedGame = s;
			}
		}
		buttonPanel = new JPanel();
		chooseButton = new JButton("Selectionner");
		chooseButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				app.setSelectedGame(selectedGame);
				setVisible(false);
			}
		});
		buttonPanel.add(chooseButton);
		
		mainPanel.add(radioPanel);
		mainPanel.add(buttonPanel);
		mainPanel.setOpaque(true);
		this.setContentPane(mainPanel);
		this.pack();
		this.setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	private void createCheckButton(String str, boolean selected){
		//Create the radio buttons.
	    JRadioButton radioButton = new JRadioButton(str);
	    radioButton.setSelected(selected);
	    radioButton.setActionCommand(str);
	    radioButton.addActionListener(this);
	    buttonGroup.add(radioButton);
	    radioPanel.add(radioButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		selectedGame = e.getActionCommand();
	}
}
