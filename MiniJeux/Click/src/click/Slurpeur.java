package click;

import java.io.IOException;

/**
*
* @author Mélanie
*/
public class Slurpeur {

	static String title = "Click !";
    static int difficulty = 2;
    static int seed = 28;
	
    public static void startGame() throws IOException, InterruptedException {
 	   // Nouveau modèle
        SlurpeurMod mod = new SlurpeurMod(difficulty, seed);
        // Nouvelle vue
       new SlurpeurView(mod);      
       mod.start();
    }
    
   public static void main(String[] args) throws IOException, InterruptedException {
	   // Jouer
       Slurpeur.startGame();	
       // Récupérer les données
      
   }
}

