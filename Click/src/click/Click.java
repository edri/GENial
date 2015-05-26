package click;

import java.io.IOException;

/**
*
* @author M�lanie
*/
public class Click {

	static String title = "Click !";
    static int difficulty = 1;
    static int seed = 28;
	
    public static void loop() throws IOException, InterruptedException {
 	   // Nouveau mod�le
        ClickMod mod = new ClickMod(difficulty, seed);
        // Nouvelle vue
       new ClickView(mod);
       
       mod.start(difficulty, seed);
       
    }
    
   public static void main(String[] args) throws IOException, InterruptedException {
    
      loop();			// Le jeu tourne - l'arr�ter quand le chrono se termine
      
   }
}

