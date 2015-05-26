/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letterhero;

import java.io.IOException;

/**
 *
 * @author Miguel
 */
public class LetterHero
{

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) throws IOException
   {
      int difficulty = 2;
      int seed = 28;
      
      LetterHeroMod mod = new LetterHeroMod(difficulty, seed);
      new LetterHeroView(mod);
      
      //while 
   }
   
}
