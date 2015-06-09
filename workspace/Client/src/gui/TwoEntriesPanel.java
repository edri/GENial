package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import application.App;

public class TwoEntriesPanel extends JPanel {
	private ConnectionFrame mainFrame;
	
	private JPanel fieldsPanel;
	private JPanel mainPanel;

	private JPanel firstPanel;
	private JLabel firstFieldDesc;
	private TextField firstTextField;

	private JPanel secondPanel;
	private JLabel secondFieldDesc;
	private TextField secondTextField;

	private JPanel buttonPanel;
	private JButton confirmButton;
	private JButton backButton;

	private App app;

	public TwoEntriesPanel(App app, String field1, String field2, boolean addButton, ConnectionFrame mainFrame){
		this.app = app;

		fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));

		firstPanel = new JPanel();
		firstPanel.setLayout(new GridLayout(1,4));
		firstFieldDesc = new JLabel(field1, SwingConstants.RIGHT);
		firstTextField = new TextField(15);
		firstPanel.add(firstFieldDesc, Component.RIGHT_ALIGNMENT);
		firstPanel.add(firstTextField, Component.LEFT_ALIGNMENT);

		secondPanel = new JPanel();
		secondPanel.setLayout(new GridLayout(1,4));
		secondFieldDesc = new JLabel(field2, SwingConstants.RIGHT);
		secondTextField = new TextField(8);
		secondPanel.add(secondFieldDesc);
		secondPanel.add(secondTextField);

		fieldsPanel.add(firstPanel);
		fieldsPanel.add(secondPanel);

		buttonPanel = new JPanel();
		if (addButton){
			backButton = new JButton("Retour");
			backButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					app.prepareChoice();
				}
			});
			buttonPanel.add(backButton);
		}
		confirmButton = new JButton("Continuer");
		confirmButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String status = app.getStatus();
				switch(status){
				case "connection":
					try {
						app.connectToServer(firstTextField.getText(), Integer.parseInt(secondTextField.getText()));
					} catch (NumberFormatException e1) {
						mainFrame.display("Format du port incorrect (doit etre un entier positif).", Color.RED);
						//e1.printStackTrace();
					} catch (UnknownHostException e1) {
						mainFrame.display("Adresse inconnue.", Color.RED);
						//e1.printStackTrace();
					} catch (IOException e1) {
						mainFrame.display("Une erreur est survenue, vous avez ete deconnecte.", Color.RED);
						//e1.printStackTrace();
					}
					break;
				case "auth":
					System.out.println("AUTH");
					break;
				case "register":
					System.out.println("REGISTER");
					break;
				default:
					mainFrame.display("Erreur status inconnu...", Color.RED);
				}
			}

		});
		buttonPanel.add(confirmButton);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(fieldsPanel);
		mainPanel.add(buttonPanel);
		this.add(mainPanel);
	}

	public void updateField(String field1, String field2){
		firstFieldDesc.setText(field1);
		secondFieldDesc.setText(field2);
	}

	public String getFieldContent(int fieldNb){
		if (fieldNb == 1){
			return firstFieldDesc.getText();
		} else if (fieldNb == 2){
			return secondFieldDesc.getText();
		} else {
			return null;
		}
	}
}
