package fr.xtremelobbyscoreboard.main;

/*     */ import java.io.File;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
import java.util.HashMap;
/*     */ import java.util.logging.FileHandler;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogRecord;

/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;
/*     */ import com.google.common.io.FileWriteMode;
/*     */ import com.google.common.io.Files;
 
public class Scoreboards extends JavaPlugin implements Listener{
   
    public HashMap<Player, CustomScoreboardManager> sb = new HashMap<>();
   
    public static Scoreboards instance;

/*  36 */   private boolean debugMode = false;
    public static Scoreboards getInstance(){
        return instance;
    }
   
    public void onEnable(){
        instance = this;
        //start task
        new CustomScoreboardManager(null).Skyblock();
        new ScoreboardsRunnable().runTaskTimer(this, 0L, 20L);
        getServer().getPluginManager().registerEvents(this, this);
    }
    public boolean toggleDebug()
    {
      if (this.debugMode) {
        for (Handler handler : getLogger().getHandlers()) {
          getLogger().removeHandler(handler);
        }
        getLogger().setLevel(Level.INFO);
        getLogger().setUseParentHandlers(true);
        getLogger().info("The debug mode is now disabled ! Log are available in the debug.log file located in BPC folder");
      } else {
        try {
          File debugFile = new File(getDataFolder(), "debug.log");
          if (debugFile.exists()) {
            debugFile.delete();
          }

          Files.asCharSink(debugFile, Charsets.UTF_8, new FileWriteMode[0]).writeLines(Arrays.asList(new String[] { "BPC log debug file - If you have an error with BPC, you should post this file on BPC topic on spigotmc", "Bukkit build : " + Bukkit.getVersion(), "BPC version : " + getDescription().getVersion(), "Operating System : " + System.getProperty("os.name"), "------------------------------------------------------------" }));

          FileHandler handler = new FileHandler(debugFile.getAbsolutePath(), true);
          handler.setFormatter(new Formatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            @SuppressWarnings("unused")
			private final String pattern = "time [level] message\n";

            public String format(LogRecord record) {
              return "time [level] message\n".replace("level", record.getLevel().getName()).replace("message", record.getMessage()).replace("[BungeePlayerCounter]", "").replace("time", this.sdf.format(Calendar.getInstance().getTime()));
            }
          });
          getLogger().addHandler(handler);
          getLogger().setLevel(Level.CONFIG);
          getLogger().info("The debug mode is now enabled ! Log are available in the debug.log file located in BPC folder");
          getLogger().setUseParentHandlers(false);
        } catch (Exception e) {
          getLogger().log(Level.SEVERE, "An exception occured during the initialization of debug logging file", e);
        }
      }
      return this.debugMode = !this.debugMode;
    }
   
    @EventHandler
    public void join(PlayerJoinEvent e){
        //initilizer
        Player p = e.getPlayer();
        CustomScoreboardManager scoreboard = new CustomScoreboardManager(p);
        scoreboard.sendLine();
        scoreboard.set();
    }
 
}