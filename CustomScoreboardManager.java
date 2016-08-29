package fr.xtremelobbyscoreboard.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
 
public class CustomScoreboardManager implements ScoreboardManager{
   
    public Player player;
    public Scoreboard scoreboard;
    public Objective objective;
    
    Logger logger = Bukkit.getLogger();
 
    public void Skyblock(){
        try{
        	
            Socket socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress("0.0.0.0", 25599));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.write(0xFE);
            @SuppressWarnings("unused")
			int data = dis.read();
 
            socket.close();
        }catch(SocketTimeoutException e){
            logger.log(Level.SEVERE, "[xTremeLobbyScoreboard] Skyblock server timed out.");
        }catch(IOException e){
            logger.log(Level.SEVERE, "[xTremeLobbyScoreboard] Skyblock server timed out.");
            e.printStackTrace();
        }
    }
    
    
   
    private String name;
   
    public CustomScoreboardManager(Player p){
       
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.player = p;
       
        if(Scoreboards.getInstance().sb.containsKey(p)) return;
 
        Scoreboards.getInstance().sb.put(p, this);
 
        this.name = "sb." + new Random().nextInt(999999);
       
        objective = scoreboard.registerNewObjective(name, "dummy");
        objective = scoreboard.getObjective(name);
        objective.setDisplayName("§1§n§lxTreme §2§nNetwork");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
       
    }
 
    public void refresh(){
        for(String ligne : scoreboard.getEntries()){
           
            if(ligne.contains("§aLobby")){
                scoreboard.resetScores(ligne);
               
                String lastligne = ligne.split(":")[0];
                String newligne = lastligne + ":" + ScoreboardsRunnable.time;
               
                objective.getScore(newligne).setScore(1);
            }
           
        }
    }
   
    public void sendLine(){
        objective.getScore(ChatColor.GREEN+"§l➜ §lLobby :").setScore(4);

        objective.getScore(ChatColor.RED+"§l➜ §1SkyEvolution :").setScore(3);
        objective.getScore(ChatColor.RED+"§l➜ §5Pvp/Faction :").setScore(1);
        objective.getScore(ChatColor.RED+"§l➜ §8KitBattle :").setScore(2);
        objective.getScore(ChatColor.RED+"§l➜ §3Creatif :").setScore(0);
    }
   
    @Override
    public Scoreboard getMainScoreboard() {
        return scoreboard;
    }
 
    @Override
    public Scoreboard getNewScoreboard() {
        return null;
    }
 
    public void set() {
        player.setScoreboard(getMainScoreboard());
    }
 
}