package miniJeux.letterHero;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import application.App;
import miniJeux.MiniJeu;

public class LetterHero extends MiniJeu implements Observer {
	private boolean isModelInitialized;
	private LetterHeroMod modele;
	private LetterHeroView view;
	
	public LetterHero(App app)
	{
		super(app);
		isModelInitialized = false;
	}
	
	public void start(int difficulty, int seed) throws IOException
	{
		modele = new LetterHeroMod(difficulty, seed);
		modele.addObserver(this);
		view = new LetterHeroView(modele);
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
