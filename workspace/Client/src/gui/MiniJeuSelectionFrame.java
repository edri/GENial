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

public class MiniJeuSelectionFrame extends JFrame {
	private App app;
	private ButtonGroup buttonGroup;
	private JPanel radioPanel;
	private JPanel testPanel;
	private JPanel buttonPanel;
	private JButton chooseButton;
	
	public MiniJeuSelectionFrame(App app, Map<String, MiniJeu> map){
		this.app = app;
		boolean firstCheckButton = true;
		
		testPanel = new JPanel();
		testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.Y_AXIS));
		
		radioPanel = new JPanel(new GridLayout(0,1));
		buttonGroup = new ButtonGroup();
		for (String s : map.keySet()){
			System.out.println("nom du jeu : " + s);
			createCheckButton(s, firstCheckButton);
			firstCheckButton = false;
		}
		buttonPanel = new JPanel();
		chooseButton = new JButton("Selectionner");
		chooseButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		buttonPanel.add(chooseButton);
		
		testPanel.add(radioPanel);
		testPanel.add(buttonPanel);
		testPanel.setOpaque(true);
		this.setContentPane(testPanel);
		this.pack();
		this.setVisible(true);
	}
	
	private void createCheckButton(String str, boolean selected){
		//Create the radio buttons.
	    JRadioButton radioButton = new JRadioButton(str);
	    radioButton.setSelected(selected);
	    radioButton.setActionCommand(str);
	    buttonGroup.add(radioButton);
	    radioPanel.add(radioButton);
	}
}
