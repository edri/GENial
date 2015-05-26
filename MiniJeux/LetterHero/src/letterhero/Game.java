/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letterhero;

/**
 *
 * @author Miguel
 */
public abstract class Game
{
   private int code;
   private int difficulty;
   private int seed;

   public void startGame()
   {
      code = 1;
      difficulty = 0;
      seed = 28;
   }

   public void finishGame(int score)
   {

   }
}