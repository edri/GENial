package miniJeux.challenger;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import application.App;
import miniJeux.MiniJeu;

public class Challenger extends MiniJeu implements Observer {
	private boolean isModelInitialized;
	private ChallengerMod modele;
	private ChallengerView view;
	
	public Challenger(App app)
	{
		super(app);
		isModelInitialized = false;
	}
	
	public void start(int difficulty, int seed) throws IOException
	{
		modele = new ChallengerMod(difficulty, seed);
		modele.addObserver(this);
		view = new ChallengerView(modele);
		isModelInitialized = true;
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
