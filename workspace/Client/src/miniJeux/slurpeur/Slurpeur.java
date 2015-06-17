package miniJeux.slurpeur;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import application.App;
import miniJeux.MiniJeu;

/**
*
* @author Mélanie
*/
public class Slurpeur extends MiniJeu implements Observer {
	private boolean isModelInitialized;
	private SlurpeurMod modele;
	private SlurpeurView view;
	
	public Slurpeur(App app)
	{
		super(app);
		isModelInitialized = false;
	}
	
	@Override
	public void start(int difficulty, int seed) throws IOException
	{
		modele = new SlurpeurMod(difficulty, seed);
		modele.addObserver(this);
		view = new SlurpeurView(modele, seed);
		isModelInitialized = true;
		
		try {
			modele.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void finish() {
		view.dispose();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (isModelInitialized && !modele.isGameRunning())
		{
			super.sendScore(modele.getScore());
			isModelInitialized = false;
		}
	}
}

