package click;

import java.io.IOException;

/**
*
* @author M�lanie
*/
public class Slurpeur {

	static String title = "Click !";
    static int difficulty = 2;
    static int seed = 28;
	
    public static void startGame() throws IOException, InterruptedException {
 	   // Nouveau mod�le
        SlurpeurMod mod = new SlurpeurMod(difficulty, seed);
        // Nouvelle vue
       new SlurpeurView(mod);      
       mod.start();
    }
    
   public static void main(String[] args) throws IOException, InterruptedException {
	   // Jouer
       Slurpeur.startGame();	
       // R�cup�rer les donn�es
      
   }
}

