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
    
    // Mod�le associ�
    protected ChallengerMod modele;
    
    // Ellipse repr�sentant la plan�te
    protected Ellipse2D.Double circle;
    
    // Couleur de la plan�te
    protected Color color = new Color(0, 187, 234);
    
    // Liste des lunes associ�es
    private ArrayList<Moon> moons;

    public Planet(ChallengerMod modele, int posX, int posY, int diameter) {
        this.modele = modele;
        
        // Cr�ation de l'ellipse associ�e � la plan�te
        circle = new Ellipse2D.Double(25, 25, diameter - 50, diameter - 50);
        setBounds(posX, posY, diameter, diameter);
        
        setLayout(null);
        
        // Initialisation de la liste des lunes
        moons = new ArrayList<Moon>();
        
        // Ne pas dessiner le background du jpanel
        setOpaque(false);
        
        // Avertir le contr�leur si le curseur a atteint la plan�te
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (modele.isGameRunning() && circle.contains(e.getX(), e.getY())) {
                    // Lorsque la souris a atteint la plan�te, la d�placer
                    notifyModele();
                }
            }
        });
    }
    
    public void setColor(Color c) {
        this.color = c;
    }
    
    protected void notifyModele() {
        // Demander au mod�le de r�agir
        modele.planetTouched();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Graphiques haute qualit� !
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_ON));
        
        // Couleur de la plan�te
        g2d.setColor(color);
        
        // Dessiner la plan�te
        g2d.fill(circle);
        
        // Dessiner les lunes
        for (Moon m : moons) {
            m.paintComponent(g);
        }
    }
    
    // D�placer la plan�te
    public void setPosition(int x, int y) {
        setBounds(x, y, circle.getBounds().width + 50, circle.getBounds().height + 50);
    }
}
