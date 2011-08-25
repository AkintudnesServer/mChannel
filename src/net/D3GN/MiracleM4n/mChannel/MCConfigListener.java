package net.D3GN.MiracleM4n.mChannel;

import java.util.HashMap;
import java.util.TreeMap;

import org.bukkit.util.config.Configuration;

public class MCConfigListener {
	mChannel plugin;
	
	public MCConfigListener(mChannel plugin) {
		this.plugin = plugin;
	}
 	
	TreeMap<String, Object> defaultGlobal = new TreeMap<String, Object>();
	TreeMap<String, Object> defaultLocal = new TreeMap<String, Object>();
	TreeMap<String, Object> defaultPrivate = new TreeMap<String, Object>();
	TreeMap<String, Object> defaultWorld = new TreeMap<String, Object>();
	TreeMap<String, Object> defaultChunk = new TreeMap<String, Object>();
	TreeMap<String, Object> defaultPassword = new TreeMap<String, Object>();
    TreeMap<String, Object> defaultIRC = new TreeMap<String, Object>();

	protected void defaultConfig() {
		Configuration channel = plugin.channel;
		channel.save();
		channel.load();
		channel.setHeader(
	            "# mChannel Channel File",
	            "");
		defaultGlobal.put("tag", "&eGlobal");
		defaultGlobal.put("type", "Global");
		defaultGlobal.put("distance", 0);
		defaultGlobal.put("default", true);
		defaultLocal.put("tag", "&eLocal");
		defaultLocal.put("type", "Local");
		defaultLocal.put("distance", 60);
		defaultLocal.put("default", false);
		defaultPrivate.put("tag", "&ePrivate");
		defaultPrivate.put("type", "Private");
		defaultPrivate.put("distance", 0);
		defaultPrivate.put("default", false);
		defaultWorld.put("tag", "&eWorld");
		defaultWorld.put("type", "World");
		defaultWorld.put("distance", 0);
		defaultWorld.put("default", false);
		defaultChunk.put("tag", "&eChunk");
		defaultChunk.put("type", "Chunk");
		defaultChunk.put("distance", 5);
		defaultChunk.put("default", false);
        defaultPassword.put("tag", "&ePassword");
        defaultPassword.put("type", "Password");
        defaultPassword.put("distance", 0);
        defaultPassword.put("password", "hello");
        defaultPassword.put("default", false);
        defaultIRC.put("tag", "&eIRC");
        defaultIRC.put("type", "ircchanneltag");
        defaultIRC.put("distance", 0);
        defaultIRC.put("default", false);
        defaultIRC.put("ircchanneltag", "d3gnTag");
		plugin.channelMap.put("Global", defaultGlobal);
		plugin.channelMap.put("Local", defaultLocal);
		plugin.channelMap.put("Private", defaultPrivate);
		plugin.channelMap.put("World", defaultWorld);
		plugin.channelMap.put("Chunk", defaultChunk);
        plugin.channelMap.put("IRC", defaultIRC);
        plugin.channelMap.put("Password", defaultPassword);
		channel.setProperty("mchannel", plugin.channelMap);
		channel.save();
		defaultGlobal.clear();
		defaultLocal.clear();
		defaultPrivate.clear();
		defaultWorld.clear();
		defaultChunk.clear();
	    defaultPassword.clear();
	    defaultIRC.clear();
	}
	
	protected void checkConfig() {
		Configuration channel = plugin.channel;
		channel.load();
		if (channel.getProperty("mchannel") == null) {
		channel.setHeader(
	            "# mChannel Channel File",
	            "");
		defaultGlobal.put("tag", "&eGlobal");
		defaultGlobal.put("type", "Global");
		defaultGlobal.put("distance", 0);
		defaultGlobal.put("default", true);
		defaultLocal.put("tag", "&eLocal");
		defaultLocal.put("type", "Local");
		defaultLocal.put("distance", 60);
		defaultLocal.put("default", false);
		defaultPrivate.put("tag", "&ePrivate");
		defaultPrivate.put("type", "Private");
		defaultPrivate.put("distance", 0);
		defaultPrivate.put("default", false);
		defaultWorld.put("tag", "&eWorld");
		defaultWorld.put("type", "World");
		defaultWorld.put("distance", 0);
		defaultWorld.put("default", false);
		defaultChunk.put("tag", "&eChunk");
		defaultChunk.put("type", "Chunk");
		defaultChunk.put("distance", 5);
		defaultChunk.put("default", false);
        defaultPassword.put("tag", "&ePassword");
        defaultPassword.put("type", "Password");
        defaultPassword.put("distance", 0);
        defaultPassword.put("password", "hello");
        defaultPassword.put("default", false);
        defaultIRC.put("tag", "&eIRC");
        defaultIRC.put("type", "ircchanneltag");
        defaultIRC.put("distance", 0);
        defaultIRC.put("default", false);
        defaultIRC.put("ircchanneltag", "d3gnTag");
		plugin.channelMap.put("Global", defaultGlobal);
		plugin.channelMap.put("Local", defaultLocal);
		plugin.channelMap.put("Private", defaultPrivate);
		plugin.channelMap.put("World", defaultWorld);
		plugin.channelMap.put("Chunk", defaultChunk);
        plugin.channelMap.put("IRC", defaultIRC);
        plugin.channelMap.put("Password", defaultPassword);
		channel.setProperty("mchannel", plugin.channelMap);
		channel.save();
		defaultGlobal.clear();
		defaultLocal.clear();
		defaultPrivate.clear();
		defaultWorld.clear();
		defaultChunk.clear();
	    defaultPassword.clear();
	    defaultIRC.clear();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void loadConfig() {
		Configuration channel = plugin.channel;
		channel.load();
		if (plugin.channelMap != null) {
			plugin.channelMap.clear();
		}
		if (plugin.channelNodeList != null) {
			plugin.channelNodeList.clear();
		}
		plugin.channelMap.putAll((HashMap) channel.getProperty("mchannel"));
		plugin.channelNodeList.putAll(plugin.channel.getNode("mchannel").getAll());
	}
}
