package miniJeux.challenger;

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
   private final JLabel lblEndGame;

   public ChallengerView(ChallengerMod modele) throws IOException
   {
      this.modele = modele;
      this.modele.addObserver(this);
      
      setTitle("Challenger!");
      setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
      setResizable(false);
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      //addKeyListener(this);
      
      ImagePanel img = new ImagePanel(ImageIO.read(new File("images/Challenger/background.png")));
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
      
      lblEndGame = new JLabel("TERMINE !", JLabel.CENTER);
      lblEndGame.setForeground(Color.WHITE);
      lblEndGame.setFont(new Font("TimeRoman",  Font.BOLD, 80));
      lblEndGame.setBounds(0, 250, LARGEUR, 80);
      lblEndGame.setVisible(false);
      img.add(lblEndGame);
      
      img.add(modele.getPlanet());
      
      pack();
      
      // Center la fen�tre sur l'�cran
      setLocationRelativeTo(null);
      
      setVisible(true);
   }

   @Override
   public void update(Observable o, Object arg)
   {
      if (modele.isGameRunning())
      {
    	  lblScore.setText("Score : " + modele.getScore() + " ");
          lblTime.setText("00:" + (modele.getCurrentLeftSeconds() < 10 ? "0" + modele.getCurrentLeftSeconds() : modele.getCurrentLeftSeconds()));

          // Redessiner
          revalidate();
          repaint();
      }
      else
      {
         lblTime.setText("00:00");         
         lblEndGame.setVisible(true);
      }
   }
}
