/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package challenger  ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ImagePanel extends JComponent
{
   private final Image image;
   
   public ImagePanel(Image image)
   {
      this.image = image;
   }
   
   @Override
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      g.drawImage(image, 0, 0, this);
   }
}

/**
 *
 * @author Miguel
 */
public class ChallengerView extends JFrame implements Observer
{
   private final static int HAUTEUR = 600;
   private final static int LARGEUR = 800;
   
   private final ChallengerMod modele;
   private final JLabel lblScore = new JLabel("Score : 0");
   private final JLabel lblTime = new JLabel("00:30");

   public ChallengerView(ChallengerMod modele) throws IOException
   {
      this.modele = modele;
      this.modele.addObserver(this);
      
      setTitle("Challenger!");
      setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //addKeyListener(this);
      
      ImagePanel img = new ImagePanel(ImageIO.read(new File("background.png")));
      img.setLayout(null);
      setContentPane(img);
      
      lblScore.setForeground(Color.WHITE);
      lblScore.setFont(new Font("TimeRoman",  Font.BOLD, 15));
      lblScore.setBounds(5, 5, HAUTEUR, 15);
      img.add(lblScore);
      
      lblTime.setForeground(Color.WHITE);
      lblTime.setFont(new Font("TimeRoman",  Font.BOLD, 15));
      lblTime.setBounds(LARGEUR - 50, 5, HAUTEUR, 15);
      img.add(lblTime);
      
      img.add(modele.getPlanet());
      
      pack();
      
      // Center la fenêtre sur l'écran
      setLocationRelativeTo(null);
      
      setVisible(true);
   }

   @Override
   public void update(Observable o, Object arg)
   {
      lblScore.setText("Score : " + modele.getScore() + " ");
      lblTime.setText("00:" + (modele.getCurrentLeftSeconds() < 10 ? "0" + modele.getCurrentLeftSeconds() : modele.getCurrentLeftSeconds()));

      // Redessiner
      revalidate();
      repaint();
   }
}
