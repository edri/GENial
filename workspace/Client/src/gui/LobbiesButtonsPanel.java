package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import application.App;

public class LobbiesButtonsPanel extends JPanel {
	private JFrame creationFrame;
	private JPanel panel;
	private JButton create;
	private JButton refresh;
	private JButton join;
	private App app;
	private LobbiesFrame mainFrame;

	public LobbiesButtonsPanel(App app, LobbiesFrame mainFrame){
		this.app = app;
		this.mainFrame = mainFrame;

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		create = new JButton("Creer");
		create.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (app.getStatus().equals("connected")){
					creationFrame = new LobbyCreationFrame(app);
					creationFrame.setSize(350, 150);
					creationFrame.setVisible(true);
					creationFrame.setResizable(false);
				}
			}
		});
		refresh = new JButton("Actualiser");
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				app.refreshGamesList();
			}
		});
		join = new JButton("Rejoindre");
		join.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String gameName = mainFrame.getSelectedInTable();
				if (gameName != null){
					app.joinAGame(gameName);
				}
			}
		});

		panel.add(join);
		panel.add(refresh);
		panel.add(create);

		this.add(panel);

	}
}
