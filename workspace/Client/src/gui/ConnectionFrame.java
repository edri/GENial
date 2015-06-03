package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import application.App;

public class ConnectionFrame extends JFrame {
	private JPanel mainPanel;
	private JLabel banner;
	//private JTextArea display;
	private JTextPane display;
	private TwoEntriesPanel entriesPanel;
	private TwoChoicesPanel choicesPanel;
	private App app;

	/**
	 * Constructeur prive s'occupant du code commun
	 * @param title
	 */
	private ConnectionFrame(String title){
		setTitle(title);
		setSize(800, 600);
		mainPanel = new JPanel();
		mainPanel.setLayout(null);

		banner = new JLabel("GENial, le jeu de plateau next-gen ! (logo)", SwingConstants.CENTER);
		banner.setSize(500, 250);
		banner.setLocation(150, 20);
		banner.setBorder(BorderFactory.createLineBorder(Color.black));

		display = new JTextPane();
		display.setText("Zone pour texte informatif");
		display.setSize(new Dimension(300,80));
		display.setLocation(250, 400);
		display.setEditable(false);
		//display.setLineWrap(true);
		//display.setWrapStyleWord(true);
		display.setBorder(BorderFactory.createLineBorder(Color.black));

		// truc kikoo affichage swing style windows
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
	}

	/**
	 * Constructeur pour une frame utilisant TwoChoicesPanel
	 * @param app
	 * @param title
	 */
	public ConnectionFrame(App app, String title){
		this(title);

		this.app = app;

		choicesPanel = new TwoChoicesPanel(app);
		choicesPanel.setSize(300, 90);
		choicesPanel.setLocation(250, 300);
		choicesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		mainPanel.add(banner);
		mainPanel.add(choicesPanel);
		mainPanel.add(display);

		add(mainPanel);
		setResizable(false);
	}

	/**
	 * Constructeur pour une fenetre utilisant TwoEntriesPanel
	 * @param app
	 * @param title
	 * @param field1
	 * @param field2
	 * @param backButton
	 */
	public ConnectionFrame(App app, String title, String field1, String field2, boolean backButton){
		this(title);
		
		this.app = app;

		entriesPanel = new TwoEntriesPanel(app, field1, field2, backButton, this);
		entriesPanel.setSize(300, 90);
		entriesPanel.setLocation(250, 300);
		entriesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		mainPanel.add(banner);
		mainPanel.add(entriesPanel);
		mainPanel.add(display);

		add(mainPanel);
		setResizable(false);
	}

	public void display(String str, Color color){
		display.setText(str);
		display.setForeground(color);
	}
}
