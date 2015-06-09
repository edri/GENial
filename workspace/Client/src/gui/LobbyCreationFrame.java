package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import application.App;
import settings.Settings;

public class LobbyCreationFrame extends JFrame{
	private JPanel mainPanel;
	private JPanel fieldsPanel;
	private JPanel[] panels;
	private JPanel buttonsPanel;
	
	private JButton back;
	private JButton create;
	
	private JLabel nameDesc;
	private JLabel nameField;
	
	private JLabel nbPlayerDesc;
	private JComboBox nbPlayerChoice;
	
	private JLabel difficultyDesc;
	private JComboBox difficultyChoice;
	
	private JLabel nbSquareDesc;
	private JComboBox nbSquareChoice;
	
	private App app;

	public LobbyCreationFrame(App app){
		this.setTitle("GENial, creation de partie");
		this.app = app;
		
		panels = new JPanel[4];
		for (int i = 0; i < 4; i++){
			panels[i] = new JPanel();
			panels[i].setLayout(new GridLayout(1,2));
		}
		int index = 0;
		
		
		fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
		
		nameDesc = new JLabel("Nom de la partie : ", SwingConstants.RIGHT);
		nameField = new JLabel();
		nameField.setText("Partie de " + Settings.userName);
		panels[index].add(nameDesc, Component.RIGHT_ALIGNMENT);
		panels[index].add(nameField, Component.LEFT_ALIGNMENT);
		index++;
		
		nbPlayerDesc = new JLabel("Nombre de joueurs : ",SwingConstants.RIGHT);
		nbPlayerChoice = new JComboBox(fillBox(Settings.MIN_PLAYER, Settings.MAX_PLAYER));
		panels[index].add(nbPlayerDesc, Component.RIGHT_ALIGNMENT);
		panels[index].add(nbPlayerChoice, Component.LEFT_ALIGNMENT);
		index++;
		
		difficultyDesc = new JLabel("Difficulte : ", SwingConstants.RIGHT);
		difficultyChoice = new JComboBox(Settings.difficulty);
		panels[index].add(difficultyDesc, Component.RIGHT_ALIGNMENT);
		panels[index].add(difficultyChoice, Component.LEFT_ALIGNMENT);
		index++;
		
		nbSquareDesc = new JLabel("Nombre de cases : ", SwingConstants.RIGHT);
		nbSquareChoice = new JComboBox(fillBox(Settings.MIN_SQUARE, Settings.MAX_SQUARE));
		panels[index].add(nbSquareDesc, Component.RIGHT_ALIGNMENT);
		panels[index].add(nbSquareChoice, Component.LEFT_ALIGNMENT);
		index++;
		
		for (int i = 0; i < panels.length; i++){
			fieldsPanel.add(panels[i]);
		}
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1,2));
		
		back = new JButton("Annuler");
		LobbyCreationFrame thisFrame = this;
		back.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				thisFrame.setVisible(false);
			}
		});
		create = new JButton("Creer");
		create.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				app.createAGame(getGameName(), getNbPlayers(), getDifficulty(), getNbSquare());
				thisFrame.setVisible(false);
			}
		});
		
		buttonsPanel.add(back);
		buttonsPanel.add(create);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(fieldsPanel);
		mainPanel.add(buttonsPanel);
		JPanel test = new JPanel();
		test.add(mainPanel);
		this.add(test);
	}
	
	private String[] fillBox(int min, int max){
		String[] table = new String[max - min + 1];
		for (int i = 0; i < table.length; i++){
			table[i] = String.valueOf(i+min);
		}
		
		return table;
	}
	
	private String getGameName(){
		return nameField.getText();
	}
	
	private int getNbPlayers(){
		return nbPlayerChoice.getSelectedIndex() + Settings.MIN_PLAYER;
	}
	
	private int getDifficulty(){
		return difficultyChoice.getSelectedIndex();
	}
	
	private int getNbSquare(){
		return nbSquareChoice.getSelectedIndex() + Settings.MIN_SQUARE;
	}
}
