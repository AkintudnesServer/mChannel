package net.D3GN.MiracleM4n.mChannel;

import net.D3GN.MiracleM4n.mChat.mChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;

public class MPlayerListener extends PlayerListener {
	mChannel plugin;
	
	public MPlayerListener(mChannel plugin) {
		this.plugin = plugin;
	}

	public void onPlayerChat(PlayerChatEvent event) {
		if (event.isCancelled()) return;
		final Player player = event.getPlayer();
		String msg = event.getMessage();
		if (msg == null) return;
		if (plugin.playersChannel.get(player) == null) {
			plugin.playersChannel.put(player, plugin.mAPI.getPlayersDefaultChannel(player));
            if (!plugin.joinMessage) {
                player.sendMessage("You have joined channel " + plugin.playersChannel.get(player) + ".");
            } else {
                plugin.getServer().broadcastMessage(mChat.API.ParsePlayerName(player) + " has joined channel " + plugin.playersChannel.get(player) + ".");
            }
		}
		if (plugin.mAPI.getChannelType(plugin.playersChannel.get(player)).equalsIgnoreCase("global")) {
			event.setFormat(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
		} else if (plugin.mAPI.getChannelType(plugin.playersChannel.get(player)).equalsIgnoreCase("local")) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (players.getWorld() != player.getWorld()) {
					event.getRecipients().remove(players);
				} else if (players.getLocation().distance(player.getLocation()) > plugin.mAPI.getChannelDistance(plugin.playersChannel.get(player))) {
					event.getRecipients().remove(players);
				}
				if(mChat.API.checkPermissions(players, "mchannel.*")) {
					event.getRecipients().add(players);
				}
			}
			event.setFormat(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
		} else if (plugin.mAPI.getChannelType(plugin.playersChannel.get(player)).equalsIgnoreCase("private")) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (plugin.playersChannel.get(players).equalsIgnoreCase(plugin.playersChannel.get(player))) {
                    String eventFormat = event.getFormat();
				    if (plugin.FactionsB) {
					    FPlayer me = FPlayer.get(player);
	                    if (!Conf.chatTagReplaceString.isEmpty()) {
	                        eventFormat = eventFormat.replace(Conf.chatTagReplaceString, (me.getRelationColor(me.getFaction()) + me.getChatTag()));
	                    }
	                    players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + eventFormat);
					} else {
		                players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
					}
				} else if (mChat.API.checkPermissions(players, "mchannel.*")) {
				    players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
				}
			}
            event.setCancelled(true);
		} else if (plugin.mAPI.getChannelType(plugin.playersChannel.get(player)).equalsIgnoreCase("world")) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (players.getWorld() != player.getWorld()) {
					event.getRecipients().remove(players);
				}
				if(mChat.API.checkPermissions(players, "mchannel.*")) {
				    event.getRecipients().add(players);
				}
			}
			event.setFormat(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
		} else if (plugin.mAPI.getChannelType(plugin.playersChannel.get(player)).equalsIgnoreCase("chunk")) {
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
			event.setFormat(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
		} else if (plugin.mAPI.getChannelType(plugin.playersChannel.get(player)).equalsIgnoreCase("password")) {
			for (Player players : plugin.getServer().getOnlinePlayers()) {
				if (plugin.playersChannel.get(players).equalsIgnoreCase(plugin.playersChannel.get(player))) {
                    String eventFormat = event.getFormat();
				    if (plugin.FactionsB) {
					    FPlayer me = FPlayer.get(player);
	                    if (!Conf.chatTagReplaceString.isEmpty()) {
	                        eventFormat = eventFormat.replace(Conf.chatTagReplaceString, (me.getRelationColor(me.getFaction()) + me.getChatTag()));
	                    }
	                    players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + eventFormat);
	                    event.setCancelled(true);
					} else {
		                players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
		                event.setCancelled(true);
					}
				} else if (mChat.API.checkPermissions(players, "mchannel.*")) {
				    players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
				}
			}
		} else if (plugin.mAPI.isChannelTypeIRC(plugin.playersChannel.get(player))) {
            if (plugin.IRCB) {
                plugin.cIRCHandle.sendMessageToTag((mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player)) + " " + mChat.API.ParseChatMessage(player, msg))), plugin.mAPI.getChannelIRCChannelTag(plugin.playersChannel.get(player)));
		    }
		    for (Player players : plugin.getServer().getOnlinePlayers()) {
                if (plugin.playersChannel.get(players).equalsIgnoreCase(plugin.playersChannel.get(player))) {
                    String eventFormat = event.getFormat();
                    if (plugin.FactionsB) {
                        FPlayer me = FPlayer.get(player);
                        if (!Conf.chatTagReplaceString.isEmpty()) {
                            eventFormat = eventFormat.replace(Conf.chatTagReplaceString, (me.getRelationColor(me.getFaction()) + me.getChatTag()));
                        }
                        players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + eventFormat);
                        event.setCancelled(true);
                    } else {
                        players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
                        event.setCancelled(true);
                    }
                } else if (mChat.API.checkPermissions(players, "mchannel.*")) {
                    players.sendMessage(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
                }
		    }
        } else {
			event.setFormat(mChat.API.addColour(plugin.mAPI.getChannelTag(plugin.playersChannel.get(player))) + " " + mChat.API.ParseChatMessage(player, msg));
		}
	}
}
