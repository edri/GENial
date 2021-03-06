package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import application.App;

public class ConnectionFrame extends AppFrame {
	private JPanel mainPanel;
	private JLabel banner;
	private JTextPane display;
	private EntriesPanel entriesPanel;
	private TwoChoicesPanel choicesPanel;
	private App app;
	
	private String imgLocation;

	/**
	 * Constructeur prive s'occupant du code commun
	 * @param title
	 */
	private ConnectionFrame(String title){
		setTitle(title);
		setSize(800, 600);
		imgLocation = "images"+File.separator+"logo_v2.png";
		mainPanel = new JPanel();
		mainPanel.setLayout(null);

		banner = new JLabel("GENial, le jeu de plateau next-gen ! (logo)", SwingConstants.CENTER);
		URL imgURL = getClass().getResource(imgLocation);
		banner.setIcon(new ImageIcon(imgURL, "Logo"));
		banner.setSize(490, 250);
		banner.setLocation(150, 20);
		banner.setBorder(BorderFactory.createLineBorder(Color.black));

		display = new JTextPane();
		display.setText("Zone pour texte informatif");
		display.setSize(new Dimension(300,80));
		display.setLocation(250, 420);
		display.setEditable(false);
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
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
	
	/**
	 * Constructeur pour une fenetre utilisant ThreeEntriesPanel
	 * @param app
	 * @param title
	 * @param field1
	 * @param field2
	 * @param field3
	 */
	public ConnectionFrame(App app, String title, String field1, String field2, String field3){
		this(title);

		this.app = app;

		entriesPanel = new ThreeEntriesPanel(app, field1, field2, field3, this);
		entriesPanel.setSize(300, 100);
		entriesPanel.setLocation(250, 300);
		entriesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		mainPanel.add(banner);
		mainPanel.add(entriesPanel);
		mainPanel.add(display);

		add(mainPanel);
		setResizable(false);
	}

	@Override
	public void display(String str, Color color){
		display.setText(str);
		display.setForeground(color);
	}

	@Override
	public void updateList() {
		// nothing to do
	}
}
