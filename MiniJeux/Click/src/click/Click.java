package click;

import java.io.IOException;

/**
*
* @author Mélanie
*/
public class Click {

	static String title = "Click !";
    static int difficulty = 2;
    static int seed = 28;
	
    public static void startGame() throws IOException, InterruptedException {
 	   // Nouveau modèle
        ClickMod mod = new ClickMod(difficulty, seed);
        // Nouvelle vue
       new ClickView(mod);      
       mod.start();
       
    }
    
   public static void main(String[] args) throws IOException, InterruptedException {
	   // Jouer
       Click.startGame();	
       // Récupérer les données
      
   }
}

