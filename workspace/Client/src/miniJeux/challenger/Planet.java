package miniJeux.challenger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author James Nolan
 */
public class Planet extends JPanel {
    
    // Modèle associé
    protected ChallengerMod modele;
    
    // Ellipse représentant la planète
    protected Ellipse2D.Double circle;
    
    // Couleur de la planète
    protected Color color = new Color(0, 187, 234);
    
    // Liste des lunes associées
    private ArrayList<Moon> moons;

    public Planet(ChallengerMod modele, int posX, int posY, int diameter) {
        this.modele = modele;
        
        // Création de l'ellipse associée à la planète
        circle = new Ellipse2D.Double(25, 25, diameter - 50, diameter - 50);
        setBounds(posX, posY, diameter, diameter);
        
        setLayout(null);
        
        // Initialisation de la liste des lunes
        moons = new ArrayList<Moon>();
        
        // Ne pas dessiner le background du jpanel
        setOpaque(false);
        
        // Avertir le contrôleur si le curseur a atteint la planète
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (modele.isGameRunning() && circle.contains(e.getX(), e.getY())) {
                    // Lorsque la souris a atteint la planète, la déplacer
                    notifyModele();
                }
            }
        });
    }
    
    public void setColor(Color c) {
        this.color = c;
    }
    
    protected void notifyModele() {
        // Demander au modèle de réagir
        modele.planetTouched();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Graphiques haute qualité !
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_ON));
        
        // Couleur de la planète
        g2d.setColor(color);
        
        // Dessiner la planète
        g2d.fill(circle);
        
        // Dessiner les lunes
        for (Moon m : moons) {
            m.paintComponent(g);
        }
    }
    
    // Déplacer la planète
    public void setPosition(int x, int y) {
        setBounds(x, y, circle.getBounds().width + 50, circle.getBounds().height + 50);
    }
}
