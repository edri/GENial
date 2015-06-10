package miniJeux.challenger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

/**
*
* @author James Nolan
*/
public class Moon extends Planet {

   private ChallengerMod modele;
   
   // Orbite (distance depuis le centre de la planète associée) et angle
   // de la planète
   private int orbite;
   private double angle;
   private double speed;
   
   // Thread gérant l'orbite
   private Thread orbiteThread;
   
   public Moon(ChallengerMod modele, int posX, int posY, int diameter, int orbite, double speed, double angle) {
       super(modele, posX, posY, diameter);
       this.modele = modele;
       
       circle = new Ellipse2D.Double(0, 0, diameter, diameter);
       
       this.orbite = orbite;
       this.angle = angle;
       this.speed = speed;
       
       // Les lunes sont de couleur différente
       color = new Color(192, 192, 192);
       
       // Thread mettant à jour la position de la lune
       orbiteThread = new Thread() {
           public void run () {
               while (modele.isGameRunning()) {
                   updateOrbite();
                   try {
                       Thread.sleep(10);
                   } catch (InterruptedException e) { }
               }
           }
       };
       
       orbiteThread.start();
   }
   
   private void updateOrbite() {
       angle += speed;
       Rectangle r = getBounds();
       int newX = (int)(orbite/2. * Math.sin(angle)) + (orbite - r.width)/2;
       int newY = (int)(orbite/2. * Math.cos(angle)) + (orbite - r.height)/2;
       setBounds(newX, newY, circle.getBounds().width, r.height);
   }
   
   protected void notifyModele() {
       modele.moonTouched();
   }
}
