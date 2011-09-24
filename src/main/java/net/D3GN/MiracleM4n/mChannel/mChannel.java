package net.D3GN.MiracleM4n.mChannel;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ensifera.animosity.craftirc.CraftIRC;
import org.blockface.bukkitstats.CallHome;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class mChannel extends JavaPlugin {
    PluginManager pm;
    
    MPlayerListener pListener;
    MCommandSender cSender;
    MConfigListener cListener;
    MCConfigListener cCListener;
    
    //Booleans
    Boolean mChatB = false;
    Boolean FactionsB = false;
    Boolean IRCB = false;
    Boolean joinMessage = false;

    //CraftIRC
    CraftIRC cIRCHandle;
    
    // Coloring & Configuration
    Logger console = null;
    Configuration channel = null;
    Configuration config = null;
    
    //API
    public static mChannelAPI API = null;
    mChannelAPI mAPI = null;
    
    TreeMap<String, Object> channelMap = new TreeMap<String, Object>();
    TreeMap<String, Object> channelNodeList = new TreeMap<String, Object>();
    TreeMap<String, Object> tempChannelMap = new TreeMap<String, Object>();
    TreeMap<String, Object> tempChannel = new TreeMap<String, Object>();
    HashMap<Player, String> playersChannel = new HashMap<Player, String>();
    HashMap<Player, String> playerInviteChannel = new HashMap<Player, String>();
    HashMap<Player, Boolean> playerInvite = new HashMap<Player, Boolean>();

    public void onEnable() {
        // Default plugin data
        pm = getServer().getPluginManager();
        channel = new Configuration(new File(getDataFolder(), "channel.yml"));
        config = new Configuration(new File(getDataFolder(), "config.yml"));
        console = getServer().getLogger();
        PluginDescriptionFile pdfFile = getDescription();
        
        getmChat();
        
        if (mChatB) {
            pListener = new MPlayerListener(this);
            cSender = new MCommandSender(this);
            cCListener = new MCConfigListener(this);
            cListener = new MConfigListener(this);
            
            // Initialize Listeners and Configurations
            checkConfigs();
            
            //API Variable Assignment
            API = new mChannelAPI(this);
            
            //Local API Assignment
            mAPI = new mChannelAPI(this);   
            
            getCommand("mchannel").setExecutor(cSender);

            CallHome.load(this);
            
            pm.registerEvent(Event.Type.PLAYER_CHAT, pListener, Priority.High, this);

            console.log(Level.INFO, "[" + (pdfFile.getName()) + "]" + " version " + pdfFile.getVersion() + " is enabled!");
        }
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        console.log(Level.INFO, "[" + (pdfFile.getName()) + "]" + " version " + pdfFile.getVersion() + " is disabled!");
    }
    
    private void getmChat() {
        PluginDescriptionFile pdfFile = getDescription();
        Plugin mChatTest = this.getServer().getPluginManager().getPlugin("mChat");
        if(mChatTest != null) {
            mChatB = true;
            getFactions();
            getIRC();
            console.log(Level.INFO, "[" + (pdfFile.getName()) + "]" + " mChat " + (mChatTest.getDescription().getVersion()) + " found now using.");
        } else {
            mChatB = false;
            console.log(Level.INFO, "[" + (pdfFile.getName()) + "]" + " mChat not found shutting down.");
            pm.disablePlugin(this);
        }
    }
    
    private void getFactions() {
        PluginDescriptionFile pdfFile = getDescription();
        Plugin FactionsTest = this.getServer().getPluginManager().getPlugin("Factions");
        if(FactionsTest != null) {
            FactionsB = true;
            console.log(Level.INFO, "[" + (pdfFile.getName()) + "]" + " Factions " + (FactionsTest.getDescription().getVersion()) + " found now using.");
        } else {
            FactionsB = false;
        }
    }

    private void getIRC() {
        PluginDescriptionFile pdfFile = getDescription();
        Plugin IRCTest = this.getServer().getPluginManager().getPlugin("CraftIRC");
        if(IRCTest != null) {
            IRCB = true;
            cIRCHandle = (CraftIRC)IRCTest;
            console.log(Level.INFO, "[" + (pdfFile.getName()) + "]" + " CraftIRC " + (IRCTest.getDescription().getVersion()) + " found now using.");
        } else {
            IRCB = false;
        }
    }
    
    private void checkConfigs() {
        if (!(new File(getDataFolder(), "channel.yml")).exists()) {
            cCListener.defaultConfig();
            cCListener.checkConfig();
            cCListener.loadConfig();
        } else {
            cCListener.checkConfig();
            cCListener.loadConfig();
        }

        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            cListener.defaultConfig();
            cListener.checkConfig();
            cListener.loadConfig();
        } else {
            cListener.checkConfig();
            cListener.loadConfig();
        }
    }
}
