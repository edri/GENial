package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import application.App;
import application.Game;

@SuppressWarnings("serial")
class CasesPanel extends JPanel {
	
	
	// Modèle
	Game modele;
	
	// Générateur de nombres aléatoires, pour décaller légèrement les pions des joueurs
	// afin qu'ils ne se superposent pas
	Random rand;
	
	// Constructeur
	public CasesPanel(Game modele) {
		this.modele = modele;
		rand = new Random();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Améliorer la qualité du rendu
		// Nombre de cases 
		int nbCases = modele.getNbCases();
		
		// Dessiner autant de cases que nécessaire
		int caseWidth = getPreferredSize().width / nbCases;
		int caseGap = (int)(.10 * caseWidth); // Espacer les cases de 10%
		caseWidth -= caseGap;
		
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < nbCases; ++i) {
			g2d.drawRect(i * (caseWidth + caseGap), 0, caseWidth, caseWidth);
		}
		
		// Positions des joueurs
		Map<String, Integer> positions = modele.getPositions();
		
		// Parcourir les joueurs
		for (String player : modele.getPlayers()) {
			// Récupérer la position du joueur
			int position = positions.get(player);
			
			// Dessiner un cercle à cette position
			// Position en pixels à laquelle correspond cette position (centre de la case associée)
			int circleWidth = caseWidth/4;
			int circleHeight = caseWidth/4;
			int posX = position * (caseWidth + caseGap) + caseWidth/2 - circleWidth/2;
			System.out.println("Case " + position);
			int posY = caseWidth/2 - circleHeight/2;
			
			// Légérement excentrer les pions des joueurs de façon aléatoire pour ne pas qu'ils
			// se superposent
			posX += getHash(player + position)%caseWidth - caseWidth/2;
			posY += getHash(position + player)%caseWidth - caseWidth/2;
			

			// Si le joueur est le gagnant, colorier le fond d'une certaine façon
			if (modele.getWinner() != null && modele.getWinner().equals(player)) {
				g2d.setColor(Color.yellow);
				g2d.fillOval(posX - 3, posY - 3, circleWidth + 6, circleHeight + 6);
			}
			
			// Contour du pion
			g2d.setColor(Color.darkGray);
			g2d.fillOval(posX + 2, posY + 2, circleWidth, circleHeight);
			
			// La couleur du pion est déduite du nom du joueur (hash du string puis conversion en RGB
			int hash = getHash(player);

			// Dessiner le pion
			g2d.setColor(new Color((hash >>= 4) % 255, (hash >>= 4) % 255, (hash >>= 4) % 255));
			g2d.fillOval(posX, posY, circleWidth, circleHeight);
			
			// Dessiner le nom du joueur à côté du pion
			g2d.setColor(Color.gray);
			g2d.drawString(player, posX, posY);
		}
	}
	
	private int getHash(String player) {
		int hash = 7;
		for (int i=0; i < player.length(); i++) {
		    hash = hash*31+player.charAt(i);
		}
		if (hash < 0) {
			hash *= -1;
		}
		
		return hash;
	}
}

public class GameView extends JFrame implements Observer {
	private App app;
	private Game modele;
	private JTextArea textDisplayArea;
	private CasesPanel cases; // Plateau en soit
	private JButton startButton;
	
	public GameView(Game modele, App app) {
		super(modele.getName());
		this.modele = modele;
		this.app = app;
		modele.addObserver(this);
		
		setLayout(new FlowLayout());
		
		// Initialisation de la "console" du plateau
		textDisplayArea = new JTextArea(5, 20);
		JScrollPane scrollPane = new JScrollPane(textDisplayArea); 
		textDisplayArea.setEditable(false);
		textDisplayArea.setPreferredSize(new Dimension(950, 100));
		textDisplayArea.setText("Initialisation");
		getContentPane().add(textDisplayArea);
		
		// Initialisation du panel contenant les cases du tableau
		cases = new CasesPanel(modele);
		cases.setPreferredSize(new Dimension(950, 100));
		getContentPane().add(cases);
		
		startButton = new JButton("Lancer la partie");
		startButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (app.getStatus().equals("onGame")){
					app.startGame();
					startButton.setEnabled(false);
				}
			}
		});
		
		startButton.setEnabled(modele.isCreator());
		getContentPane().add(startButton);
		
		pack();
		validate();
		
		this.setSize(980, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		// Redessiner le plateau
		repaint();
	}
	
	public void display(String msg){
		// met a jour l'affichage de la partie
		textDisplayArea.setText(msg);
	}
}
