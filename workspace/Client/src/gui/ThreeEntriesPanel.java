package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import application.App;

public class ThreeEntriesPanel extends EntriesPanel {
	private AppFrame mainFrame;

	private JPanel fieldsPanel;
	private JPanel mainPanel;

	private JPanel firstPanel;
	private JLabel firstFieldDesc;
	private JTextField firstTextField;

	private JPanel secondPanel;
	private JLabel secondFieldDesc;
	private JTextField secondTextField;

	private JPanel thirdPanel;
	private JLabel thirdFieldDesc;
	private JTextField thirdTextField;

	private JPanel buttonPanel;
	private JButton confirmButton;
	private JButton backButton;

	private App app;

	public ThreeEntriesPanel(App app, String field1, String field2, String field3, AppFrame mainFrame){
		this.app = app;

		fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));

		firstPanel = new JPanel();
		firstPanel.setLayout(new GridLayout(1,4));
		firstFieldDesc = new JLabel(field1, SwingConstants.RIGHT);
		firstTextField = new JTextField(15);
		firstPanel.add(firstFieldDesc, Component.RIGHT_ALIGNMENT);
		firstPanel.add(firstTextField, Component.LEFT_ALIGNMENT);

		secondPanel = new JPanel();
		secondPanel.setLayout(new GridLayout(1,4));
		secondFieldDesc = new JLabel(field2, SwingConstants.RIGHT);
		secondTextField = new JPasswordField(8);
		secondPanel.add(secondFieldDesc);
		secondPanel.add(secondTextField);
		
		thirdPanel = new JPanel();
		thirdPanel.setLayout(new GridLayout(1,4));
		thirdFieldDesc = new JLabel(field3, SwingConstants.RIGHT);
		thirdTextField = new JPasswordField(8);
		thirdPanel.add(thirdFieldDesc);
		thirdPanel.add(thirdTextField);

		fieldsPanel.add(firstPanel);
		fieldsPanel.add(secondPanel);
		fieldsPanel.add(thirdPanel);

		buttonPanel = new JPanel();
		backButton = new JButton("Retour");
		backButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				app.prepareChoice();
			}
		});
		
		confirmButton = new JButton("Continuer");
		confirmButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String status = app.getStatus();
				switch(status){
				case "register":
					if (firstTextField.getText().equals("") || firstTextField.getText() == null
					|| secondTextField.getText().equals("") || secondTextField.getText() == null
					|| thirdTextField.getText().equals("") || thirdTextField.getText() == null){
						mainFrame.display("Un champs n'est pas renseigne.", Color.RED);
					} else if(!(secondTextField.getText().equals(thirdTextField.getText()))){
						mainFrame.display("Vos deux mots de passe ne sont pas identique.", Color.RED);
					} else {
						app.register(firstTextField.getText(), secondTextField.getText());
					}
					break;
				default:
					mainFrame.display("Erreur status inconnu...", Color.RED);
				}
			}

		});
		
		buttonPanel.add(backButton);
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
