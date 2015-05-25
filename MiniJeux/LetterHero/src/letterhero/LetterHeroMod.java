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
   private final int[] yPositions_ = {MIN_NEW_Y, MAX_NEW_Y, (MIN_NEW_Y + MAX_NEW_Y) / 2};
   private final int[] xPositions = {20, 175, 325};
   private final char[] chars = new char[3];
   private final TimerTask task;
   private final Random random;
   private int currentLeftSeconds = 6;
   private Timer timer = new Timer();
   private boolean running;
   
   private int score = 0;
   
   public LetterHeroMod(int difficulty, int seed)
   {
      threadTime = (4 - difficulty);
      random = new Random(seed);
      
      for (int i = 0; i < chars.length; ++i)
      {
         chars[i] = (char)(random.nextInt(90 - 65 + 1) + 65);
      }
      
      running = true;
      activity = new Thread(this);
      
      task = new TimerTask() {
         @Override
         public void run() {
            if (--currentLeftSeconds == -1)
            {
               running = false;
               currentLeftSeconds = 0;
               timer.cancel();
               timer.purge();
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
      while (running)
      {
         try {Thread.sleep(threadTime);}catch(InterruptedException e) {}         
         move();
      }
      
      // On indique que l'état de l'objet a été modifié.
      setChanged();
      // On notifie les observateurs que l'objet a été modifié.
      notifyObservers();
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
   
   public boolean isGameRunning()
   {
      return running;
   }
}
