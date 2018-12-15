package com.perengano99.PinkiRanks.cmds;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.perengano99.PinkiLibraries.CommandApi.SenderType;
import com.perengano99.PinkiLibraries.CommandApi.executors.PLIBCommand;
import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.ConfigFile;
import com.perengano99.PinkiRanks.nametag.NametagUtil;
import com.perengano99.PinkiRanks.util.permissionsUtil;

public class NIckCmd extends PLIBCommand {
    
    private static PC pc = PC.p;
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
	
	if (args.length == 1) {
	    if (sender instanceof Player) {
		if (args[0].equalsIgnoreCase("reset")) {
		    NametagUtil.resetNameTag((Player) sender);
		    sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.reset-nick")));
		    return true;
		}
		
		String nick = args[0];
		String nicknocolor = pc.nocolor(args[0]);
		if (nicknocolor.length() > 16) {
		    sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.nick-too-long")));
		    return false;
		}
		if (nick.equalsIgnoreCase(((Player) sender).getName())) {
		    sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.nick-equals-playername")));
		    return false;
		}
		for (OfflinePlayer players : pc.getServer().getOfflinePlayers()) {
		    if (nick.equalsIgnoreCase(players.getName())) {
			sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.nick-equals-otherplayername")));
			return false;
		    }
		}
		for (Player players : pc.getServer().getOnlinePlayers()) {
		    if (nick.equalsIgnoreCase(players.getName())) {
			sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.nick-equals-otherplayername")));
			return false;
		    }
		}
		
		NametagUtil.setNameTag((Player) sender, pc.color(args[0]));
		sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.change-nick")));
		return true;
	    } else {
		sender.sendMessage(pc.nocolor(ConfigFile.getConfig().getString("messages.no-console")));
		return false;
	    }
	    
	}
	
	if (args.length == 2) {
	    if (permissionsUtil.havePermission(sender, permissionsUtil.PERMISSION_NICK_OTHERS)) {
		Player target = pc.getServer().getPlayer(args[0]);
		
		if (target == null) {
		    sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.player-no-exist")));
		    return false;
		}
		
		if (args[1].equalsIgnoreCase("reset")) {
		    NametagUtil.resetNameTag(target);
		    sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.reset-nick")));
		    target.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.reset-nick")));
		    return true;
		}
		
		String nick = args[1];
		String nicknocolor = pc.nocolor(args[1]);
		if (nicknocolor.length() > 16) {
		    sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.nick-too-long")));
		    return false;
		}
		if (nick.equalsIgnoreCase((target.getName()))) {
		    sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.nick-equals-playername")));
		    return false;
		}
		for (OfflinePlayer players : pc.getServer().getOfflinePlayers()) {
		    if (nick.equalsIgnoreCase(players.getName())) {
			sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.nick-equals-otherplayername")));
			return false;
		    }
		}
		for (Player players : pc.getServer().getOnlinePlayers()) {
		    if (nick.equalsIgnoreCase(players.getName())) {
			sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.nick-equals-otherplayername")));
			return false;
		    }
		}
		
		NametagUtil.setNameTag(target, pc.color(args[1]));
		sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.change-nick")));
		target.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.change-nick")));
		return true;
	    }
	}
	
	return false;
    }
    
    @Override
    public String[] setAliases() {
	// TODO Auto-generated method stub
	return null;
    }
    
    @Override
    public String setDescription() {
	// TODO Auto-generated method stub
	return ConfigFile.getConfig().getString("messages.help-nick");
    }
    
    @Override
    public int setMaxArgs() {
	// TODO Auto-generated method stub
	return 2;
    }
    
    @Override
    public int setMinArgs() {
	// TODO Auto-generated method stub
	return 1;
    }
    
    @Override
    public String setFewArgumentsMessage() {
	// TODO Auto-generated method stub
	return pc.color(ConfigFile.getConfig().getString("messages.use-command-nick"));
    }
    
    @Override
    public String setTooManyArgumentsMessage() {
	// TODO Auto-generated method stub
	return pc.color(ConfigFile.getConfig().getString("messages.too-many-arguments"));
    }
    
    @Override
    public String setPermission() {
	// TODO Auto-generated method stub
	return permissionsUtil.getPermission(permissionsUtil.PERMISSION_NICK);
    }
    
    @Override
    public String setNotPermissionMessage() {
	// TODO Auto-generated method stub
	return pc.color(ConfigFile.getConfig().getString("messages.no-permission"));
    }
    
    @Override
    public SenderType onlyFor() {
	// TODO Auto-generated method stub
	return null;
    }
    
    @Override
    public String setOnlyForMessage() {
	// TODO Auto-generated method stub
	return null;
    }    
}
