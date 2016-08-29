package fr.xtremelobbyscoreboard.main;

import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardsRunnable extends BukkitRunnable {

   static int time = 0;
  
   @Override
   public void run() {
       for(Entry<Player, CustomScoreboardManager> scoreboard : Scoreboards.getInstance().sb.entrySet()){
           CustomScoreboardManager board = scoreboard.getValue();
           board.refresh();
           time++;
       }

   }

}