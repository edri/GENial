/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letterhero;

import java.util.Observable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Miguel
 */
public class LetterHeroMod extends Observable implements Runnable
{
   private final int MIN_NEW_Y = -100;
   private final int MAX_NEW_Y = -500;
   private final int threadTime;
   private final Thread activity;
   private int currentLeftSeconds = 5;
   private int[] yPositions_ = {MIN_NEW_Y, MAX_NEW_Y, (MIN_NEW_Y + MAX_NEW_Y) / 2};
   private int[] xPositions = {20, 175, 325};
   private char[] chars = new char[3];
   private Random random;
   private Timer timer = new Timer();
   private TimerTask task;
   
   private int score = 0;
   
   public LetterHeroMod(int difficulty, int seed)
   {
      threadTime = (4 - difficulty);
      random = new Random(seed);
      
      for (int i = 0; i < chars.length; ++i)
      {
         chars[i] = (char)(random.nextInt(90 - 65 + 1) + 65);
      }
      
      activity = new Thread(this);
      
      task = new TimerTask() {
         @Override
         public void run() {
            if (--currentLeftSeconds == -1)
            {
               System.exit(0);
            }
         }
      };
   }
   
   private void move()
   {      
      for (int i = 0; i < yPositions_.length; ++i)
      {
         yPositions_[i]++;

         if (yPositions_[i] > 650)
         {
            yPositions_[i] = -1 * (random.nextInt(-MAX_NEW_Y + MIN_NEW_Y + 1) - MIN_NEW_Y);
            chars[i] = (char)(random.nextInt(90 - 65 + 1) + 65);
         }
      }
      
      // On indique que l'état de l'objet a été modifié.
      setChanged();
      // On notifie les observateurs que l'objet a été modifié.
      notifyObservers();
   }
   
   public void startThread()
   {      
      if (!activity.isAlive())
         activity.start();
      
      timer.scheduleAtFixedRate(task, 0, 1000);
   }

   @Override
   public void run()
   {
      while (true)
      {
         try {Thread.sleep(threadTime);}catch(InterruptedException e) {}         
         move();
      }
   }
   
   public void incScore(int inc)
   {
      score += inc;
      
      // On indique que l'état de l'objet a été modifié.
      setChanged();
      // On notifie les observateurs que l'objet a été modifié.
      notifyObservers();
   }
   
   public int getScore()
   {
      return score;
   }
   
   public int getYPosition(int index)
   {
      return yPositions_[index % 3];
   }
   
   public int getXPosition(int index)
   {
      return xPositions[index % 3];
   }
   
   public char getChar(int index)
   {
      return chars[index % 3];
   }
   
   public int getCurrentLeftSeconds()
   {
      return currentLeftSeconds;
   }
   
   public boolean isAlive()
   {
      return activity.isAlive();
   }
}
