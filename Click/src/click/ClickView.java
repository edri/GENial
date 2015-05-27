package click;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

// Pour le fond
class ImagePanel extends JComponent {
   private final Image image;
   
   public ImagePanel(Image image) {
      this.image = image;
   }
   
   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(image, 0, 0, this);
   }
}

class SlurpeurView extends JLabel implements MouseListener {
	
	// L'image du slurpeur
	private ImageIcon imgSlurpeur = new ImageIcon(ImageIO.read(new File("slurpeur.png"))); 	
	// Le modèle
	private ClickMod modele;
	
	public SlurpeurView(ClickMod modele) throws IOException {
		this.modele = modele;
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		modele.incrementerScore();
	}

	@Override
	public void mouseEntered(MouseEvent e) {	
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}


public class ClickView implements Observer {

	private final static int HAUTEUR = 700;
   	private final static int LARGEUR = 700;
   	
   	JFrame frame;
   	JPanel panel;
   
   	// Le modèle
   	private final ClickMod modele;
   	// Le modèle slurpeur
   	private final Slurpeur slurpeur;
   	
   	// Les infos sur le jeu
   	private final JLabel lblScore = new JLabel("Score : 0");
    private final JLabel lblTime = new JLabel("00:30");
    
    // Le rendu du slurpeur
    JLabel imgSlurpeur;
    
    private SlurpeurView slurpeurview;
	
	public ClickView(ClickMod modele) throws IOException {
		
		this.frame = new JFrame();
		this.panel = new JPanel();
		
		this.modele = modele;
		this.modele.addObserver(this);
		
		this.frame.setTitle("Click !");
		this.frame.setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImagePanel img = new ImagePanel(ImageIO.read(new File("background.jpg")));
		this.frame.getContentPane().add(img, null);

		this.slurpeur = Slurpeur.getInstance();
		this.slurpeur.addObserver(this);
		
		this.slurpeurview = new SlurpeurView(modele);
		
		this.imgSlurpeur = new JLabel(new ImageIcon(ImageIO.read(new File("slurpeur.png"))));
		this.imgSlurpeur.setBounds(slurpeur.getX(), slurpeur.getY(), 50, 50);
		this.imgSlurpeur.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				modele.incrementerScore();	
				try {
					slurpeur.jum();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		img.add(this.imgSlurpeur);
		
		lblScore.setForeground(Color.BLACK);
		lblScore.setFont(new Font("TimeRoman",  Font.BOLD, 25));
		lblScore.setBounds(5, 5, HAUTEUR, 25);
		img.add(lblScore);
	
		
		lblTime.setForeground(Color.BLACK);
		lblTime.setFont(new Font("TimeRoman",  Font.BOLD, 25));
		lblTime.setBounds(LARGEUR - 70, 5, HAUTEUR, 25);
		img.add(lblTime);
		
		this.frame.setUndecorated(true);
		this.frame.pack();
		this.frame.setVisible(true);
		this.frame.setLocationRelativeTo(null);
	}
	

	@Override
	public void update(Observable o, Object arg) {
		lblScore.setText("Score : " + modele.getScore() + " ");
		lblTime.setText("00:" + (modele.getCurrentLeftSeconds() < 10 ? "0" + modele.getCurrentLeftSeconds() : modele.getCurrentLeftSeconds()));
		imgSlurpeur.setIcon(slurpeur.getImage());
		imgSlurpeur.setBounds(slurpeur.getX(), slurpeur.getY(), 50, 50);
	}

}
