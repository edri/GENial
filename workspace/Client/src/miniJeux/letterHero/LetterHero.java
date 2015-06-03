package miniJeux.letterHero;

import java.io.IOException;

import application.App;
import miniJeux.MiniJeu;

public class LetterHero extends MiniJeu {
	public LetterHero(App app)
	{
		super(app);
	}
	
	public void start(int difficulty, int seed) throws IOException
	{
		new LetterHeroView(new LetterHeroMod(difficulty, seed));
	}
}
