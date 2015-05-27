/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package challenger;

import java.io.IOException;

/**
 *
 * @author Miguel
 * @author James
 */
public class Challenger
{

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) throws IOException
   {
      int difficulty = 2;
      int seed = 28;
      
      ChallengerMod mod = new ChallengerMod(difficulty, seed);
      new ChallengerView(mod);
      
   }
}
