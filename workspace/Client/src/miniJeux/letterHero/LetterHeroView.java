package miniJeux.letterHero;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
public class LetterHeroView extends JFrame implements Observer, KeyListener
{
   private final int HAUTEUR;
   private final int LARGEUR;
   private final int SIZE_FLAMES;
   
   private final LetterHeroMod modele;
   private final JLabel lblScore;
   private final JLabel lblTime;
   private final JLabel lblEndGame;
   private final JLabel[] flames = new JLabel[3];
   private final JLabel[] chars = new JLabel[3];
   private final JLabel[] messages = new JLabel[3];
   private final boolean[] isPositionOccuped;
   private int position;

   public LetterHeroView(LetterHeroMod modele) throws IOException
   {
      this.modele = modele;
      this.modele.addObserver(this);
      
      HAUTEUR = modele.getHauteur();
      LARGEUR = modele.getLargeur();
      SIZE_FLAMES = modele.getSizeFlames();
      
      isPositionOccuped = new boolean[3];
      
      setTitle("LetterHero");
      setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
      setResizable(false);
      setLocation(700, 300);
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addKeyListener(this);
      
      ImagePanel img = new ImagePanel(ImageIO.read(new File("images/LetterHero/background.png")));
      img.setLayout(null);
      setContentPane(img);
      
      lblScore = new JLabel("Score : 0");
      lblScore.setForeground(Color.WHITE);
      lblScore.setFont(new Font("TimeRoman",  Font.BOLD, 15));
      lblScore.setBounds(5, 5, HAUTEUR, 15);
      img.add(lblScore);
      
      lblTime = new JLabel("00:" + (modele.getCurrentLeftSeconds() < 10 ? "0" + modele.getCurrentLeftSeconds() : modele.getCurrentLeftSeconds()));
      lblTime.setForeground(Color.WHITE);
      lblTime.setFont(new Font("TimeRoman",  Font.BOLD, 15));
      lblTime.setBounds(LARGEUR - 50, 5, HAUTEUR, 15);
      img.add(lblTime);
      
      lblEndGame = new JLabel(new ImageIcon(ImageIO.read(new File("images/LetterHero/finish.png"))), JLabel.CENTER);
      lblEndGame.setBounds(0, 250, LARGEUR, 80);
      lblEndGame.setVisible(false);
      img.add(lblEndGame);
      
      for (int i = 0; i < flames.length; ++i)
      {
         flames[i] = new JLabel(new ImageIcon(ImageIO.read(new File("images/LetterHero/flame" + (i + 1) + ".png"))));
         flames[i].setBounds(modele.getXPosition(i), modele.getYPosition(i), SIZE_FLAMES, SIZE_FLAMES);
         img.add(flames[i]);
         
         chars[i] = new JLabel(Character.toString(modele.getChar(i)), JLabel.CENTER);
         chars[i].setFont(new Font("TimeRoman",  Font.BOLD, 100));
         chars[i].setBounds(modele.getXPosition(i), 485, SIZE_FLAMES, SIZE_FLAMES);
         img.add(chars[i]);
         
         messages[i] = new JLabel("PARFAIT !", JLabel.CENTER);
         messages[i].setFont(new Font("TimeRoman",  Font.BOLD, 40));
         messages[i].setBounds(modele.getXPosition(i) - 45, 450, 200, 50);
         messages[i].setVisible(false);
         img.add(messages[i]);
         
         isPositionOccuped[i] = false;
      }
      
      pack();
      setVisible(true);
      
      try {
          modele.startThread();
       } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
          Logger.getLogger(LetterHeroView.class.getName()).log(Level.SEVERE, null, ex);
       }
   }

   @Override
   public void update(Observable o, Object arg)
   {
      if (modele.isGameRunning())
      {
         lblScore.setText("Score : " + modele.getScore() + " ");

         for (int i = 0; i < flames.length; ++i)
         {
            flames[i].setBounds(modele.getXPosition(i), modele.getYPosition(i), SIZE_FLAMES, SIZE_FLAMES);
            chars[i].setText(Character.toString(modele.getChar(i)));
         }
         lblTime.setText("00:" + (modele.getCurrentLeftSeconds() < 10 ? "0" + modele.getCurrentLeftSeconds() : modele.getCurrentLeftSeconds()));
      }
      else
      {
         lblTime.setText("00:00");
         lblEndGame.setVisible(true);
         
         for (int i = 0; i < flames.length; ++i)
         {
            flames[i].setVisible(false);
         }
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {}

   @Override
   public void keyPressed(KeyEvent e)
   {
      if (modele.isGameRunning())
      {
         position = -1;
         
         for (int i = 0; i < flames.length; ++i)
         {
            if (!isPositionOccuped[i] && modele.getChar(i) == Character.toUpperCase(e.getKeyChar()))
            {
               if (modele.getYPosition(i) >= 450 && modele.getYPosition(i) <= 500)
               {                  
                  position = i;
                  messages[i].setText("PARFAIT !");
                  modele.incScore(30);
                  break;
               }
               else if (modele.getYPosition(i) >= 385 && modele.getYPosition(i) <= 585)
               {
                  position = i;
                  messages[i].setText("BIEN !");
                  modele.incScore(10);
                  break;
               }
            }
         }

         if (position == -1)
         {
            modele.incScore(-10);
         }
         else if (!isPositionOccuped[position])
         {
            isPositionOccuped[position] = true;
                  
            Timer timer = new Timer("Timer" + position);            
            
            messages[position].setVisible(true);
            
            timer.scheduleAtFixedRate(new TimerTask() {
               private int initSize = 40;
               private final int positionC;
               
               {
                  positionC = position;
               }
               
               @Override
               public void run() {
                  messages[positionC].setFont(new Font("TimeRoman",  Font.BOLD, --initSize));
                  messages[positionC].setBounds(messages[positionC].getX() + 2, 
                                               messages[positionC].getY() - 3, 
                                               messages[positionC].getFontMetrics(messages[positionC].getFont()).stringWidth(messages[positionC].getText()), 
                                               50);
                  
                  if (initSize == 0)
                  {
                     messages[positionC].setVisible(false);
                     messages[positionC].setFont(new Font("TimeRoman",  Font.BOLD, 40));
                     messages[positionC].setBounds(modele.getXPosition(positionC) - 45, 450, 200, 50);
                     
                     isPositionOccuped[positionC] = false;
                     
                     timer.cancel();
                     timer.purge();
                  }
               }
            }, 0, 15);
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {}
   
}
