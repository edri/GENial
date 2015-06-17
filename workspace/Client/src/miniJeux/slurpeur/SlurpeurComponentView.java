package miniJeux.slurpeur;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

class SlurpeurComponentView extends JLabel implements MouseListener {
		
	// L'image du slurpeur
	private ImageIcon imgSlurpeur = new ImageIcon(ImageIO.read(new File("images/Slurpeur/slurpeur.png"))); 	
	// Le modèle
	private SlurpeurMod modele;
	
	public SlurpeurComponentView(SlurpeurMod modele) throws IOException {
		this.modele = modele;
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
