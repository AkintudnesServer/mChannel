package net.D3GN.MiracleM4n.mChannel;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;

import net.D3GN.MiracleM4n.mChat.mChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class MPlayerListener extends PlayerListener {
	mChannel plugin;
	
	public MPlayerListener(mChannel plugin) {
		this.plugin = plugin;
	}

	public void onPlayerChat(PlayerChatEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
        String pChannel = plugin.playersChannel.get(player);
		String msg = event.getMessage();
		if (msg == null) return;
		if (pChannel == null) {
			plugin.playersChannel.put(player, plugin.mAPI.getPlayersDefaultChannel(player));
            if (!plugin.joinMessage) {
                player.sendMessage("You have joined channel " + pChannel + ".");
            } else {
                plugin.getServer().broadcastMessage(mChat.API.ParsePlayerName(player) + " has joined channel " + pChannel + ".");
            }
		}
        if(plugin.channelLeaves.containsKey(pChannel)) {
            for(Player playerx : plugin.getServer().getOnlinePlayers()) {
                if(plugin.channelLeaves.get(pChannel).contains(playerx)) {
                    event.getRecipients().remove(playerx);
                }
            }
        }
		if (plugin.mAPI.isChannelTypeLocal(pChannel)) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (players.getWorld() != player.getWorld()) {
					event.getRecipients().remove(players);
				} else if (players.getLocation().distance(player.getLocation()) > plugin.mAPI.getChannelDistance(pChannel)) {
					event.getRecipients().remove(players);
				}
				if(mChat.API.checkPermissions(players, "mchannel.*")) {
					event.getRecipients().add(players);
				}
			}
		} else if (plugin.mAPI.isChannelTypePrivate(pChannel)) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (plugin.playersChannel.get(players).equalsIgnoreCase(pChannel)) {
                    String eventFormat = event.getFormat();
				    if (plugin.FactionsB) {
					    FPlayer me = FPlayer.get(player);
	                    if (!Conf.chatTagReplaceString.isEmpty()) {
	                        eventFormat = eventFormat.replace(Conf.chatTagReplaceString, (me.getRelationColor(me.getFaction()) + me.getChatTag()));
	                    }
	                    players.sendMessage(eventFormat);
					} else {
		                players.sendMessage(mChat.API.ParseChatMessage(player, msg));
					}
				} else if (mChat.API.checkPermissions(players, "mchannel.*")) {
				    players.sendMessage(mChat.API.ParseChatMessage(player, msg));
				}
			}
            event.setCancelled(true);
		} else if (plugin.mAPI.isChannelTypeWorld(pChannel)) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (players.getWorld() != player.getWorld()) {
					event.getRecipients().remove(players);
				}
				if(mChat.API.checkPermissions(players, "mchannel.*")) {
				    event.getRecipients().add(players);
				}
			}
		} else if (plugin.mAPI.isChannelTypeChunk(pChannel)) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (players.getWorld() != player.getWorld()) {
					event.getRecipients().remove(players);
				} else if ((players.getWorld().getChunkAt(players.getLocation()).getX() != player.getWorld().getChunkAt(player.getLocation()).getX()) ||
						(players.getWorld().getChunkAt(players.getLocation()).getZ() != player.getWorld().getChunkAt(player.getLocation()).getZ())) {
					event.getRecipients().remove(players);
				}
				if(mChat.API.checkPermissions(players, "mchannel.*")) {
				    event.getRecipients().add(players);
				}
			}
        } else if (plugin.mAPI.isChannelTypePassword(pChannel)) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (plugin.playersChannel.get(players).equalsIgnoreCase(pChannel)) {
                    String eventFormat = event.getFormat();
				    if (plugin.FactionsB) {
					    FPlayer me = FPlayer.get(player);
	                    if (!Conf.chatTagReplaceString.isEmpty()) {
	                        eventFormat = eventFormat.replace(Conf.chatTagReplaceString, (me.getRelationColor(me.getFaction()) + me.getChatTag()));
	                    }
	                    players.sendMessage(eventFormat);
	                    event.setCancelled(true);
					} else {
		                players.sendMessage(mChat.API.ParseChatMessage(player, msg));
		                event.setCancelled(true);
					}
				} else if (mChat.API.checkPermissions(players, "mchannel.*")) {
				    players.sendMessage(mChat.API.ParseChatMessage(player, msg));
				}
			}
		}
	}
}
