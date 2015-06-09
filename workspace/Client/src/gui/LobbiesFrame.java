package gui;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import settings.Settings;
import application.App;
import application.Lobby;

public class LobbiesFrame extends AppFrame {
	private App app;
	private String[] columnNames;
	private Object[][] data;
	
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel buttons;
	
	public LobbiesFrame(App app){
		this.app = app;
		this.setTitle("GENial, salons de jeu");
		
		columnNames = new String[1];
		columnNames[0] = "Liste des parties existantes ["+app.getGameList().size()+"]";
		data = new Object[0][0];
				/*	= {
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"},
						   {"Partie de Fab, 3/8 joueurs, plateau de 32 cases, difficulte : Hardcore"},
						   {"Partie de Kevin, 6/6 joueurs, plateau de 40 cases, difficulte : Easy"}
		}; */
		
		this.setSize(800,600);
		this.setLayout(null);
		
		updateTable();
		
		JPanel panel1 = new JPanel();
		panel1.setSize(200, 480);
		panel1.setBorder(BorderFactory.createLineBorder(Color.black));
		panel1.setLocation(600, 0);
		
		buttons = new LobbiesButtonsPanel(app, this);
		buttons.setSize(600, 120);
		buttons.setBorder(BorderFactory.createLineBorder(Color.black));
		buttons.setLocation(0, 480);

		//this.add(scrollPane);
		this.add(panel1);
		this.add(buttons);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void display(String msg, Color color) {
		// TODO Auto-generated method stub
	}
	
	public void updateList(){
		List<Lobby> temp = app.getGameList();
		int size = temp.size();
		columnNames[0] = "Liste des parties existantes ["+size+"]";
		data = new Object[size][1];
		int i = 0;
		for (Lobby l : temp){
			data[i][0] = l.getName() + ", " + l.getPlayers().size() + "/" + l.getMaxPlayers()
					+ " joueurs, plateau de " + l.getNbSquares() + " cases, difficulte : "
					+ Settings.difficulty[l.getDifficulty()];
			i++;
		}
		this.remove(scrollPane);
		updateTable();
	}
	
	public String getSelectedInTable(){
		String result = null;
		int index = table.getSelectedRow();
		if (index >= 0){
			String gameStr = (String)data[index][0];
			String[] tab = gameStr.split(",");
			result = tab[0];
		}
		return result;
	}
	
	private void updateTable(){
		table = new JTable(data, columnNames);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		//TODO le tableau ne doit pas être éditable
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		scrollPane.setColumnHeaderView(table.getTableHeader());
		scrollPane.setSize(600, 480);
		scrollPane.setLocation(0, 0);
		this.add(scrollPane);
	}
}
