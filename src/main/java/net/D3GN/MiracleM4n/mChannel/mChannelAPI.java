package net.D3GN.MiracleM4n.mChannel;

import java.util.Map.Entry;

import net.D3GN.MiracleM4n.mChat.mChat;
import org.bukkit.entity.Player;

public class mChannelAPI {
    mChannel plugin;
    
    public mChannelAPI(mChannel plugin) {
        this.plugin = plugin;
    }

    public String getGlobalDefaultChannel() {
    	for (Entry<String, Object> entry : plugin.channelNodeList.entrySet()) {
			if (plugin.channelNodeList.get(entry.getKey()).equals(true)) {
				return entry.getKey().replace(".default", "");
			}
		}
    	return "";
    }

    public String getPlayersDefaultChannel(Player player) {
    	for (Entry<String, Object> entry : plugin.channelNodeList.entrySet()) {
            if (mChat.API.checkPermissions(player, "mchannel.default." + entry.getKey().replace(".default", "")))
				return entry.getKey().replace(".default", "");
		}
    	return getGlobalDefaultChannel();
    }

    public String[] getAllChannels() {
        String aChannels = "";
    	for (Entry<String, Object> entry : plugin.channelNodeList.entrySet()) {
            if (entry.getKey().contains(".default"))
                aChannels += entry.getKey().replace(".default", "") + ",";
		}
        return aChannels.split(",");
    }
	
    public String getChannelType(String channelName) {
    	if (isChannelReal(channelName)) {
    		if (plugin.channelNodeList.get(channelName + ".type").toString() != null)
    			return plugin.channelNodeList.get(channelName + ".type").toString();
    	}
    	return "";
    }
    
    public String getChannelPrefix(String channelName) {
    	if (isChannelReal(channelName)) {
    		if (plugin.channelNodeList.get(channelName + ".prefix").toString() != null)
    			return plugin.channelNodeList.get(channelName + ".prefix").toString();
    	}
    	return "";
    }

    public String getChannelSuffix(String channelName) {
    	if (isChannelReal(channelName)) {
    		if (plugin.channelNodeList.get(channelName + ".suffix").toString() != null)
    			return plugin.channelNodeList.get(channelName + ".suffix").toString();
    	}
    	return "";
    }
    
    public Integer getChannelDistance(String channelName) {
    	if (isChannelReal(channelName)) {
    		if (plugin.channelNodeList.get(channelName + ".distance").toString() != null)
    			return (Integer) plugin.channelNodeList.get(channelName + ".distance");
    	}
    	return 0;
    }
    
    public String getChannelPassword(String channelName) {
        if (isChannelReal(channelName)) {
            if (plugin.channelNodeList.get(channelName + ".password").toString() != null)
                return plugin.channelNodeList.get(channelName + ".password").toString();
        }
        return "";
    }
    
    public Boolean isChannelReal(String channelName) {
        return plugin.channelMap.get(channelName) != null;
    }
    
    public Boolean isChannelTypeGlobal(String channelName) {
        return getChannelType(channelName).equalsIgnoreCase("global");
    }
    
    public Boolean isChannelTypeLocal(String channelName) {
        return getChannelType(channelName).equalsIgnoreCase("local");
    }
    
    public Boolean isChannelTypePrivate(String channelName) {
        return getChannelType(channelName).equalsIgnoreCase("private");
    }
    
    public Boolean isChannelTypeWorld(String channelName) {
        return getChannelType(channelName).equalsIgnoreCase("world");
    }
    
    public Boolean isChannelTypeChunk(String channelName) {
        return getChannelType(channelName).equalsIgnoreCase("chunk");
    }

    public Boolean isChannelTypePassword(String channelName) {
        return getChannelType(channelName).equalsIgnoreCase("password");
    }

    public Boolean isChannelGlobalDefault(String channelName) {
        return isChannelReal(channelName)
                && (plugin.channelNodeList.get(channelName + ".default").equals(true));
    }

    public Boolean isChannelPlayersDefault(String channelName, Player player) {
    	if (isChannelReal(channelName)) {
    		if (getPlayersDefaultChannel(player).equals(channelName))
                return true;
    	}
    	return false;
    }
    
    public String getPlayersChannel(Player player) {
    	if(plugin.playersChannel.get(player) == null) {
    		player.performCommand("mchannel join " + getPlayersDefaultChannel(player));
    		return getPlayersDefaultChannel(player);
    	} else {
    		return (plugin.playersChannel.get(player));
    	}
    }
    
    public void setPlayersChannel(Player player, String channelName) {
    	if (player != null) {
        	if (isChannelReal(channelName)) {
        		plugin.playersChannel.remove(player);
        		plugin.playersChannel.put(player, channelName);
        	}
    	}
    }
    
    public void removePlayersChannel(Player player) {
    	if (player != null) {
        	plugin.playersChannel.remove(player);
    	}
    }
    
    public void moveAllInChannel(String oldChannelName, String newChannelName) {
    	if (isChannelReal(oldChannelName)) {
    		if (plugin.playersChannel != null) {
            	for (Entry<Player, String> entry : plugin.playersChannel.entrySet()) {
        			if (entry.getValue().equals(oldChannelName)) {
                        Player pEntry = (Player) entry;
                        plugin.playersChannel.remove(pEntry);
                        plugin.playersChannel.put(pEntry, newChannelName);
                        pEntry.sendMessage("Channel has been changed from " + oldChannelName + " to " + newChannelName + ".");
        			}
        		}
    		}
    	}
    }
    
    public void createChannel(String channelName, String channelType, String channelPrefix, String channelSuffix, Integer channelDistance, Boolean channelDefault) {
    	if (!(isChannelReal(channelName))) {
    		plugin.channel.load();
    		plugin.tempChannelMap.putAll(plugin.channelMap);
    		plugin.channelMap.clear();
    		plugin.channelMap.putAll(plugin.tempChannelMap);
    		plugin.tempChannel.put("prefix", channelPrefix);
    		plugin.tempChannel.put("suffix", channelSuffix);
    		plugin.tempChannel.put("type", channelType);
    		plugin.tempChannel.put("distance", channelDistance);
    		plugin.tempChannel.put("default", channelDefault);
            plugin.tempChannel.put("ircTag", "");
    		plugin.channelMap.put(channelName, plugin.tempChannel);
    		plugin.channel.setProperty("mchannel", plugin.channelMap);
    		plugin.channel.save();
    		plugin.cCListener.loadConfig();
    	}
		plugin.tempChannelMap.clear();
		plugin.tempChannel.clear();
    }

    public void removeChannel(String channelName) {
    	if (isChannelReal(channelName)) {
    		plugin.channel.load();
    		plugin.channelMap.remove(channelName);
    		plugin.tempChannelMap.putAll(plugin.channelMap);
    		plugin.channelMap.clear();
    		plugin.channelMap.putAll(plugin.tempChannelMap);
    		plugin.channel.setProperty("mchannel", plugin.channelMap);
    		plugin.channel.save();
    		plugin.cCListener.loadConfig();
    	}
		plugin.tempChannelMap.clear();
		plugin.tempChannel.clear();
    }

    public void editChannel(String oldChannelName, String newChannelName, String channelType, String channelPrefix, String channelSuffix, Integer channelDistance, Boolean channelDefault) {
    	createChannel(newChannelName, channelType, channelPrefix, channelSuffix, channelDistance, channelDefault);
        moveAllInChannel(oldChannelName, newChannelName);
        removeChannel(oldChannelName);
		plugin.cCListener.loadConfig();
    }

    public void makeDefault(String channelName) {
    	editChannel(channelName, channelName, getChannelType(channelName), getChannelPrefix(channelName), getChannelSuffix(channelName), getChannelDistance(channelName), true);
    }
}
