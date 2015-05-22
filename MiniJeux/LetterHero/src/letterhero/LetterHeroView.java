/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letterhero;

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
import javax.imageio.ImageIO;
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

class Letter extends JComponent
{
   // designed by Freepik.com
   private final Image image;
   private final char letter;
   
   public Letter(Image image, char letter)
   {
      this.image = image;
      this.letter = letter;
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
   private final static int HAUTEUR = 700;
   private final static int LARGEUR = 450;
   
   private final LetterHeroMod modele;
   private final JLabel lblScore = new JLabel("Score : 0");
   private final JLabel lblTime = new JLabel("00:30");
   private final JLabel[] flames = new JLabel[3];
   private final JLabel[] chars = new JLabel[3];

   public LetterHeroView(LetterHeroMod modele) throws IOException
   {
      this.modele = modele;
      this.modele.addObserver(this);
      
      setTitle("LetterHero");
      setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
      setResizable(false);
      setLocation(700, 300);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      addKeyListener(this);
      
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
      
      for (int i = 0; i < flames.length; ++i)
      {
         flames[i] = new JLabel(new ImageIcon(ImageIO.read(new File("images/flame" + (i + 1) + ".png"))));
         flames[i].setBounds(modele.getXPosition(i), modele.getYPosition(i), 100, 100);
         img.add(flames[i]);
         
         chars[i] = new JLabel(Character.toString(modele.getChar(i)), JLabel.CENTER);
         chars[i].setFont(new Font("TimeRoman",  Font.BOLD, 100));
         chars[i].setBounds(modele.getXPosition(i), 485, 100, 100);
         img.add(chars[i]);
      }
      
      pack();
      setVisible(true);
   }

   @Override
   public void update(Observable o, Object arg)
   {
      lblScore.setText("Score : " + modele.getScore() + " ");
      
      for (int i = 0; i < flames.length; ++i)
      {
         flames[i].setBounds(modele.getXPosition(i), modele.getYPosition(i), 100, 100);
         chars[i].setText(Character.toString(modele.getChar(i)));
      }
      
      lblTime.setText("00:" + (modele.getCurrentLeftSeconds() < 10 ? "0" + modele.getCurrentLeftSeconds() : modele.getCurrentLeftSeconds()));
   }

   @Override
   public void keyTyped(KeyEvent e) {}

   @Override
   public void keyPressed(KeyEvent e)
   {      
      if (!modele.isAlive())
      {
         modele.startThread();
      }
      else
      {
         boolean correct = false;

         for (int i = 0; i < flames.length; ++i)
         {
            if (modele.getChar(i) == Character.toUpperCase(e.getKeyChar()))
            {
               if (modele.getYPosition(i) >= 450 && modele.getYPosition(i) <= 500)
               {
                  correct = true;
                  modele.incScore(30);
               }
               else if (modele.getYPosition(i) >= 385 && modele.getYPosition(i) <= 585)
               {
                  correct = true;
                  modele.incScore(10);
               }
            }
         }

         if (!correct)
         {
            modele.incScore(-10);
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {}
   
}
