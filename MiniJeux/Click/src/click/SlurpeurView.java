package click;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

// Pour le fond
class ImagePanel extends JComponent {
   private final Image image;		// Background
   
   public ImagePanel(Image image) {
      this.image = image;
   }
   
   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(image, 0, 0, this);
   }
}



public class SlurpeurView implements Observer {

	private final static int HAUTEUR = 700;
   	private final static int LARGEUR = 700;
   	
   	JFrame frame;
   	JPanel panel;
   
   	// Le modèle
   	private final SlurpeurMod modele;
   	// Le modèle slurpeur
   	private final SlurpeurComponent slurpeur;
    // Le rendu du slurpeur
    JLabel imgSlurpeur;
    private SlurpeurComponentView slurpeurview;
   	
   	// Les infos sur le jeu
   	private final JLabel lblScore = new JLabel("Score : 0");
    private final JLabel lblTime = new JLabel("00:30");
   	private final JLabel lblCommencer = new JLabel("<html><span><div align = center>Appuyez sur une touche pour commencer !</div></span></html>");
    
    // Le boutton qui disparait quand on gagne des points
	private SoftJLabel lblPlus10;
	
	private JLabel lblPoints;
	private JLabel lblTermine;
	
	// Pour les bouttons qui disparaissent
	private Timer alphaChanger;
  
	
	public SlurpeurView(SlurpeurMod modele) throws IOException {
		
		this.frame = new JFrame();
		this.panel = new JPanel();
		frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			//	modele.startThread();
			//	slurpeur.moveSlurpeur();
				lblCommencer.setVisible(false);
				slurpeur.moveSlurpeur();
				System.out.println("Slurper!");
			}
		});
		
		this.modele = modele;
		this.modele.addObserver(this);
		
		this.frame.setTitle("Slurpeur");
		this.frame.setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		alphaChanger = new Timer(30, new ActionListener() {
            private float incrementer = -.03f;

            @Override
            public void actionPerformed(ActionEvent e) {
               // float newAlpha = lblPoints.getAlpha() + incrementer;
                if (newAlpha < 0) {
                    newAlpha = 0;
                    incrementer = -incrementer;
                } else if (newAlpha > 1f) {
                    newAlpha = 1f;
                    incrementer = -incrementer;
                }
                lblPoints.setVisible(false);
            }
        });
        */
		
		ImagePanel img = new ImagePanel(ImageIO.read(new File("backgroundHerbe.jpg")));
		this.frame.getContentPane().add(img, null);
		
		img.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				Image cursorImage = new ImageIcon("fleche.png").getImage().getScaledInstance(20, 139, 0);  
	
				Toolkit kit = Toolkit.getDefaultToolkit();
				Dimension dim = kit.getBestCursorSize(20, 139);
				Point hotspot = new Point(0, 0);  
				String cursorName = "fleche";
				img.setCursor(kit.createCustomCursor(cursorImage, hotspot, cursorName));
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {	
			}
		});

		this.slurpeur = SlurpeurComponent.getInstance();
		this.slurpeur.addObserver(this);
		
		this.slurpeurview = new SlurpeurComponentView(modele);
		
		lblPoints = new JLabel(new ImageIcon(ImageIO.read(new File("plus10.png"))));		
		lblPoints.setBounds(0, 0, 700, 700);
		lblPoints.setVisible(false);
		img.add(lblPoints);
		
		lblTermine = new JLabel(new ImageIcon(ImageIO.read(new File("termine.png"))));		
		lblTermine.setBounds(0, 0, 700, 700);
		lblTermine.setVisible(false);
		img.add(lblTermine);
		
		this.imgSlurpeur = new JLabel(new ImageIcon(ImageIO.read(new File("slurpeur.png"))));
		this.imgSlurpeur.setBounds(slurpeur.getX(), slurpeur.getY(), 50, 50);
		this.imgSlurpeur.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {

				System.out.println("+10");
				// lblPoints.setVisible(true);
				modele.incrementerScore();	
				try {
					slurpeur.jump();		// risque de bloquage
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		img.add(this.imgSlurpeur);
		
		lblCommencer.setForeground(Color.BLACK);
		lblCommencer.setFont(new Font("TimeRoman",  Font.BOLD, 40));
		lblCommencer.setBounds(0, 0, HAUTEUR, LARGEUR);
		lblCommencer.setAlignmentX(JLabel.CENTER);
		lblCommencer.setVisible(false);
		//lblCommencer.setHorizontalAlignment(frame.CENTER_ALIGNMENT);
		img.add(lblCommencer);
		
		lblScore.setForeground(Color.RED);
		lblScore.setFont(new Font("TimeRoman",  Font.BOLD, 25));
		lblScore.setBounds(5, 5, HAUTEUR, 25);
		img.add(lblScore);
	
		
		lblTime.setForeground(Color.RED);
		lblTime.setFont(new Font("TimeRoman",  Font.BOLD, 25));
		lblTime.setBounds(LARGEUR - 70, 5, HAUTEUR, 25);
		img.add(lblTime);
		
		this.frame.pack();
		this.frame.setVisible(true);
		this.frame.setLocationRelativeTo(null);
		
		this.modele.startThread();
	}
	
	
	

	@Override
	public void update(Observable o, Object arg) {
		lblScore.setText("Score : " + modele.getScore() + " ");
		if(modele.getCurrentLeftSeconds() == 0) {
			lblScore.setVisible(false);
			lblTermine.setVisible(true);
		} else {
			lblTime.setText("00:" + (modele.getCurrentLeftSeconds() < 10 ? "0" + modele.getCurrentLeftSeconds() : modele.getCurrentLeftSeconds()));	
		}	
		imgSlurpeur.setIcon(slurpeur.getImage());
		imgSlurpeur.setBounds(slurpeur.getX(), slurpeur.getY(), 100, 100);
	}

}
