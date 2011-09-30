package net.D3GN.MiracleM4n.mChannel;

import net.D3GN.MiracleM4n.mChat.mChat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MCommandSender implements CommandExecutor {
    mChannel plugin;

    public MCommandSender(mChannel plugin) {
        this.plugin = plugin;
    }

    HashMap<Player, Object> createStage = new HashMap<Player, Object>();
    HashMap<Player, String> createName = new HashMap<Player, String>();
    HashMap<Player, String> createType = new HashMap<Player, String>();
    HashMap<Player, String> createPrefix = new HashMap<Player, String>();
    HashMap<Player, String> createSuffix = new HashMap<Player, String>();

    HashMap<Player, Object> editStage = new HashMap<Player, Object>();
    HashMap<Player, String> editOldName = new HashMap<Player, String>();
    HashMap<Player, String> editNewName = new HashMap<Player, String>();
    HashMap<Player, String> editType = new HashMap<Player, String>();
    HashMap<Player, String> editPrefix = new HashMap<Player, String>();
    HashMap<Player, String> editSuffix = new HashMap<Player, String>();

    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        String commandName = command.getName();
        Integer intDistance = 0;
        if (commandName.equalsIgnoreCase("mchannel")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (plugin.playersChannel.get(player) == null) {
                    plugin.playersChannel.put(player,
                            plugin.mAPI.getPlayersDefaultChannel(player));
                    if (!plugin.joinMessage) {
                        player.sendMessage("You have joined channel "
                                + plugin.playersChannel.get(player) + ".");
                    } else {
                        plugin.getServer().broadcastMessage(
                                mChat.API.ParsePlayerName(player)
                                        + " has joined channel "
                                        + plugin.playersChannel.get(player)
                                        + ".");
                    }
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("leave")) {
                        plugin.mAPI.removePlayerFromChannel(player,
                                plugin.mAPI.getPlayersChannel(player));
                        return true;
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (mChat.API.checkPermissions(player,
                                "mchannel.reload")
                                || mChat.API.checkPermissions(player,
                                        "mchannel.*")) {
                            plugin.cCListener.checkConfig();
                            plugin.cCListener.loadConfig();
                            player.sendMessage("mChannel config reloaded.");
                            return true;
                        } else {
                            player.sendMessage("You dont have permissions to reload the config.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("accept")) {
                        if (plugin.playerInvite != null) {
                            if (plugin.playerInvite.get(player) != null) {
                                if (plugin.playerInvite.get(player)) {
                                    plugin.mAPI.setPlayersChannel(player,
                                            plugin.playerInviteChannel
                                                    .get(player));
                                    if (!plugin.joinMessage) {
                                        player.sendMessage("You have joined channel "
                                                + plugin.playersChannel
                                                        .get(player) + ".");
                                    } else {
                                        plugin.getServer()
                                                .broadcastMessage(
                                                        mChat.API
                                                                .ParsePlayerName(player)
                                                                + " has joined channel "
                                                                + plugin.playersChannel
                                                                        .get(player)
                                                                + ".");
                                    }
                                    player.sendMessage("Invitation accepted joining "
                                            + plugin.playerInviteChannel
                                                    .get(player) + ".");
                                    return true;
                                }
                            }
                        }
                        player.sendMessage("No invitation has been found for your name.");
                        return true;
                    } else if (args[0].equalsIgnoreCase("cList")) {
                        String sChannels = "";
                        for (String string : plugin.mAPI.getAllChannels()) {
                            String channelName = string;
                            if (mChat.API.checkPermissions(player,
                                    "mchannel.join." + string)
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.*")
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.join.*")) {
                                channelName = "&2" + channelName;
                                sChannels += mChat.API.addColour(channelName)
                                        + ", ";
                            } else {
                                channelName = "&4" + channelName;
                                sChannels += mChat.API.addColour(channelName)
                                        + ", ";
                            }
                        }
                        player.sendMessage(mChat.API
                                .addColour("&4[&fmChannel&4]&f " + sChannels));
                        return true;
                    } else if (args[0].equalsIgnoreCase("pList")) {
                        String sPlayers = "";
                        for (Player players : plugin.getServer()
                                .getOnlinePlayers()) {
                            if (plugin.mAPI.getPlayersChannel(player).equals(
                                    plugin.mAPI.getPlayersChannel(players))) {
                                sPlayers += mChat.API.ParseJoinName(players)
                                        + ", ";
                            }
                        }
                        player.sendMessage(mChat.API
                                .addColour("&4[&fmChannel&4]&f " + sPlayers));
                        return true;
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("join")) {
                        if (plugin.mAPI.isChannelReal(args[1])) {
                            if (plugin.mAPI.isChannelTypePassword(args[1])) {
                                player.sendMessage("Channel "
                                        + args[1]
                                        + " has a password, please use "
                                        + commandName
                                        + " join {password} to join this channel.");
                                return true;
                            } else if ((args[1].equals(plugin.mAPI
                                    .getPlayersDefaultChannel(player)))
                                    || (args[1].equals(plugin.mAPI
                                            .getGlobalDefaultChannel()))) {
                                plugin.mAPI.setPlayersChannel(player, args[1]);
                                if (!plugin.joinMessage) {
                                    player.sendMessage("You have joined channel "
                                            + plugin.playersChannel.get(player)
                                            + ".");
                                } else {
                                    plugin.getServer().broadcastMessage(
                                            mChat.API.ParsePlayerName(player)
                                                    + " has joined channel "
                                                    + plugin.playersChannel
                                                            .get(player) + ".");
                                }
                                return true;
                            } else if (mChat.API.checkPermissions(player,
                                    "mchannel.join." + args[1])
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.*")
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.join.*")) {
                                plugin.mAPI.setPlayersChannel(player, args[1]);
                                if (!plugin.joinMessage) {
                                    player.sendMessage("You have joined channel "
                                            + plugin.playersChannel.get(player)
                                            + ".");
                                } else {
                                    plugin.getServer().broadcastMessage(
                                            mChat.API.ParsePlayerName(player)
                                                    + " has joined channel "
                                                    + plugin.playersChannel
                                                            .get(player) + ".");
                                }
                                return true;
                            } else {
                                player.sendMessage("You dont have permissions to join channel "
                                        + args[1] + ".");
                                return true;
                            }
                        } else {
                            player.sendMessage("Channel " + args[1]
                                    + " is not real.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        plugin.mAPI.removePlayerFromChannel(player, args[1]);
                        return true;
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (plugin.mAPI.isChannelReal(args[1])) {
                            if (mChat.API.checkPermissions(player,
                                    "mchannel.remove." + args[1])
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.*")
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.remove.*")) {
                                plugin.mAPI.removeChannel(args[1]);
                                player.sendMessage("Channel " + args[1]
                                        + " has been removed.");
                                return true;
                            } else {
                                player.sendMessage("You dont have permissions to remove channel "
                                        + args[1] + ".");
                                return true;
                            }
                        } else {
                            player.sendMessage("Channel " + args[1]
                                    + " is not real.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("invite")) {
                        if (mChat.API
                                .checkPermissions(player, "mchannel.invite."
                                        + plugin.mAPI.getPlayersChannel(player))
                                || mChat.API.checkPermissions(player,
                                        "mchannel.*")
                                || mChat.API.checkPermissions(player,
                                        "mchannel.invite.*")) {
                            if (plugin.getServer().getPlayer(args[1]) != null) {
                                Player sPlayer = plugin.getServer().getPlayer(
                                        args[1]);
                                plugin.playerInvite.put(sPlayer, true);
                                plugin.playerInviteChannel.put(sPlayer,
                                        plugin.mAPI.getPlayersChannel(player));
                                sPlayer.sendMessage("You have been invited to channel "
                                        + plugin.mAPI.getPlayersChannel(player)
                                        + ", By "
                                        + mChat.API.ParsePlayerName(player)
                                        + mChat.API.addColour("&f" + ". Use /"
                                                + commandName
                                                + " accept to accept."));
                                player.sendMessage("Player "
                                        + mChat.API.ParsePlayerName(sPlayer)
                                        + mChat.API
                                                .addColour("&f"
                                                        + " has been invited to your channel."));
                                return true;
                            } else {
                                player.sendMessage("Player " + args[1]
                                        + " not found.");
                                return true;
                            }
                        } else {
                            player.sendMessage("You dont have permissions to invite people to your channel.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("create")) {
                        if (!(plugin.mAPI.isChannelReal(args[1]))) {
                            if (mChat.API.checkPermissions(player,
                                    "mchannel.create." + args[1])
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.*")
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.create.*")) {
                                createStage.put(player, 1);
                                createName.put(player, args[1]);
                                player.sendMessage("ChannelName set to "
                                        + args[1] + ".");
                                player.sendMessage("Please set the Channel type now by doing,");
                                player.sendMessage("/"
                                        + commandName
                                        + " create type [global, local, private, chunk, world, password].");
                                return true;
                            } else {
                                player.sendMessage("You dont have permissions to create channel "
                                        + args[1] + ".");
                                return true;
                            }
                        } else {
                            player.sendMessage("Channel " + args[1]
                                    + " is already created.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("edit")) {
                        if (plugin.mAPI.isChannelReal(args[1])) {
                            if (mChat.API.checkPermissions(player,
                                    "mchannel.edit." + args[1])
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.*")
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.edit.*")) {
                                editStage.put(player, 1);
                                editOldName.put(player, args[1]);
                                player.sendMessage("ChannelName you would like to edit is "
                                        + args[1] + ".");
                                player.sendMessage("Please set the new ChannelName now by doing,");
                                player.sendMessage("/" + commandName
                                        + " edit newName [NewChannelName].");
                                return true;
                            } else {
                                player.sendMessage("You dont have permissions to edit channel "
                                        + args[1] + ".");
                                return true;
                            }
                        } else {
                            player.sendMessage("Channel " + args[1]
                                    + " has not yet been created.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("default")) {
                        if (plugin.mAPI.isChannelReal(args[1])) {
                            if (mChat.API.checkPermissions(player,
                                    "mchannel.default." + args[1])
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.*")
                                    || mChat.API.checkPermissions(player,
                                            "mchannel.default.*")) {
                                plugin.mAPI.makeDefault(args[1]);
                                player.sendMessage("Channel " + args[1]
                                        + " is now the global default channel.");
                                return true;
                            } else {
                                player.sendMessage("You dont have permissions to make channel "
                                        + args[1] + " global default.");
                                return true;
                            }
                        }
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("join")) {
                        if (plugin.mAPI.isChannelReal(args[1])) {
                            if (plugin.mAPI.isChannelTypePassword(args[1])) {
                                if (args[2].equalsIgnoreCase(plugin.mAPI
                                        .getChannelPassword(args[1]))) {
                                    player.sendMessage("Password correct entering channel.");
                                    plugin.mAPI.setPlayersChannel(player,
                                            args[1]);
                                    if (!plugin.joinMessage) {
                                        player.sendMessage("You have joined channel "
                                                + plugin.playersChannel
                                                        .get(player) + ".");
                                    } else {
                                        plugin.getServer()
                                                .broadcastMessage(
                                                        mChat.API
                                                                .ParsePlayerName(player)
                                                                + " has joined channel "
                                                                + plugin.playersChannel
                                                                        .get(player)
                                                                + ".");
                                    }
                                    return true;
                                } else {
                                    player.sendMessage(args[2]
                                            + " is not the password for channel "
                                            + args[1] + ".");
                                    return true;
                                }
                            } else {
                                player.sendMessage("Channel " + args[1]
                                        + " is not a passworded channel.");
                                return true;
                            }
                        } else {
                            player.sendMessage("Channel " + args[1]
                                    + " is not real.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("send")) {
                        if (plugin.mAPI.isChannelReal(args[1])
                                && !plugin.mAPI.isChannelTypePassword(args[1])
                                && ((args[1].equals(plugin.mAPI
                                        .getPlayersDefaultChannel(player)))
                                        || (args[1].equals(plugin.mAPI
                                                .getGlobalDefaultChannel()))
                                        || mChat.API.checkPermissions(player,
                                                "mchannel.join." + args[1])
                                        || mChat.API.checkPermissions(player,
                                                "mchannel.*") || mChat.API
                                        .checkPermissions(player,
                                                "mchannel.join.*"))) {
                            final String currentChannel = plugin.mAPI.getPlayersChannel(player);
                            String message = "";
                            for(int i = 2; i < args.length; i++) {
                                message += args[i];
                                if(i != args.length - 1)
                                    message += " ";
                            }
                            plugin.mAPI.setPlayersChannel(player, args[1]);
                            player.chat(message);
                            plugin.mAPI.setPlayersChannel(player, currentChannel);
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("create")) {
                        if (args[1].equalsIgnoreCase("type")) {
                            if (createStage.get(player).equals(1)) {
                                if (args[2].equalsIgnoreCase("global")
                                        || args[2].equalsIgnoreCase("local")
                                        || args[2].equalsIgnoreCase("private")
                                        || args[2].equalsIgnoreCase("chunk")
                                        || args[2].equalsIgnoreCase("world")
                                        || args[2].equalsIgnoreCase("password")
                                        || args[2].equalsIgnoreCase("irc")) {
                                    createStage.put(player, 2);
                                    createType.put(player, args[2]);
                                    player.sendMessage(createName.get(player)
                                            + "'s type will be set to "
                                            + args[2] + ".");
                                    player.sendMessage("Please set the Channels prefix now by doing,");
                                    player.sendMessage("/" + commandName
                                            + " create prefix [ChannelPrefix].");
                                    return true;
                                } else {
                                    // Not Right Type
                                    return true;
                                }
                            } else {
                                // Not Right Stage
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("prefix")) {
                            if (createStage.get(player).equals(2)) {
                                createStage.put(player, 3);
                                createPrefix.put(player, args[2]);
                                player.sendMessage(createName.get(player)
                                        + "'s prefix will be set to " + args[2]
                                        + ".");
                                player.sendMessage("Please set the Channels suffix now by doing,");
                                player.sendMessage("/" + commandName
                                        + " create suffix [ChannelSuffix].");
                                return true;
                            } else {
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("suffix")) {
                            if (createStage.get(player).equals(3)) {
                                createStage.put(player, 4);
                                createSuffix.put(player, args[2]);
                                player.sendMessage(createName.get(player)
                                        + "'s suffix will be set to " + args[2]
                                        + ".");
                                if (createType.get(player).equalsIgnoreCase(
                                        "local")) {
                                    player.sendMessage("Please set Channels distance now by doing,");
                                    player.sendMessage("/" + commandName
                                            + " create distance [Distance].");
                                    return true;
                                } else {
                                    editStage.remove(player);
                                    plugin.mAPI.createChannel(
                                            createName.get(player),
                                            createType.get(player),
                                            createPrefix.get(player),
                                            createSuffix.get(player),
                                            intDistance, false);
                                    player.sendMessage("Channel "
                                            + createName.get(player)
                                            + " has been successfully created.");
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("distance")) {
                            if (createStage.get(player).equals(4)) {
                                try {
                                    intDistance = new Integer(args[2]);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(args[2]
                                            + " is not a valid Distance value.");
                                    return true;
                                }
                                editStage.remove(player);
                                player.sendMessage(createName.get(player)
                                        + "'s distance will be set to "
                                        + args[2] + ".");
                                plugin.mAPI.createChannel(
                                        createName.get(player),
                                        createType.get(player),
                                        createPrefix.get(player),
                                        createSuffix.get(player), intDistance,
                                        false);
                                player.sendMessage("Channel "
                                        + createName.get(player)
                                        + " has been successfully created.");
                                return true;
                            } else {
                                return true;
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("edit")) {
                        if (args[1].equalsIgnoreCase("newName")) {
                            if (editStage.get(player).equals(1)) {
                                editStage.put(player, 2);
                                editNewName.put(player, args[2]);
                                player.sendMessage(editOldName.get(player)
                                        + "'s new name set to " + args[2] + ".");
                                player.sendMessage("Please set the Channels new type now by doing,");
                                player.sendMessage("/"
                                        + commandName
                                        + " edit newType [global, local, private, chunk, world, password].");
                                return true;
                            } else {
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("newType")) {
                            if (editStage.get(player).equals(2)) {
                                if (args[2].equalsIgnoreCase("global")
                                        || args[2].equalsIgnoreCase("local")
                                        || args[2].equalsIgnoreCase("private")
                                        || args[2].equalsIgnoreCase("chunk")
                                        || args[2].equalsIgnoreCase("world")
                                        || args[2].equalsIgnoreCase("password")
                                        || args[2].equalsIgnoreCase("irc")) {
                                    editStage.put(player, 3);
                                    editType.put(player, args[2]);
                                    player.sendMessage(editOldName.get(player)
                                            + "'s new type set to " + args[2]
                                            + ".");
                                    player.sendMessage(editOldName.get(player)
                                            + "'s new type set to " + args[2]
                                            + ".");
                                    player.sendMessage("Please set the new Channels prefix now by doing,");
                                    player.sendMessage("/"
                                            + commandName
                                            + " edit newPrefix [NewChannelPrefix].");
                                    return true;
                                } else {
                                    // Not Right Type
                                    return true;
                                }
                            } else {
                                // Not Right Stage
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("newPrefix")) {
                            if (editStage.get(player).equals(3)) {
                                editStage.put(player, 4);
                                editPrefix.put(player, args[2]);
                                player.sendMessage(editOldName.get(player)
                                        + "'s new tag set to " + args[2] + ".");
                                player.sendMessage("Please set the new Channels suffix now by doing,");
                                player.sendMessage("/" + commandName
                                        + " edit newSuffix [NewChannelSuffix].");
                                return true;
                            } else {
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("newSuffix")) {
                            if (editStage.get(player).equals(4)) {
                                editStage.put(player, 5);
                                editSuffix.put(player, args[2]);
                                player.sendMessage(editOldName.get(player)
                                        + "'s new suffix set to " + args[2]
                                        + ".");
                                if (editType.get(player).equalsIgnoreCase(
                                        "local")) {
                                    player.sendMessage("Please set the new Channels distance now by doing,");
                                    player.sendMessage("/" + commandName
                                            + " edit newDistance [Distance].");
                                    return true;
                                } else {
                                    editStage.remove(player);
                                    plugin.mAPI.editChannel(
                                            editOldName.get(player),
                                            editNewName.get(player),
                                            editType.get(player),
                                            editPrefix.get(player),
                                            editSuffix.get(player),
                                            intDistance, false);
                                    player.sendMessage("Channel "
                                            + editOldName.get(player)
                                            + " has been successfully edited.");
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("newDistance")) {
                            if (editStage.get(player).equals(5)) {
                                try {
                                    intDistance = new Integer(args[2]);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(args[2]
                                            + " is not a valid Distance value.");
                                    return true;
                                }
                                editStage.remove(player);
                                player.sendMessage(editOldName.get(player)
                                        + "'s new distance set to " + args[2]
                                        + ".");
                                plugin.mAPI.editChannel(
                                        editOldName.get(player),
                                        editNewName.get(player),
                                        editType.get(player),
                                        editPrefix.get(player),
                                        editSuffix.get(player), intDistance,
                                        false);
                                player.sendMessage("Channel "
                                        + editOldName.get(player)
                                        + " has been successfully edited.");
                                return true;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage("Consoles can't...AW FUX Need to fix this.");
                return true;
            }
        }
        return false;
    }
}
