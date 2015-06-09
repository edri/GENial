package miniJeux;

import java.io.IOException;

import application.App;

public abstract class MiniJeu {
	App app;
	int difficulty;
	int seed;
	
	public MiniJeu(App app)
	{
		this.app = app;
	}
	
	public abstract void start(int difficulty, int seed) throws IOException;
	
	public void sendScore(int score)
	{
		app.sendScore(score);
	}
	
	public abstract void finish();
}
