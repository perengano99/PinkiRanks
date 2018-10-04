package com.perengano99.PinkiRanks.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.ConfigFile;

public enum permissionsUtil {

	PERMISSION_NICK, PERMISSION_RELOAD, PERMISSION_NICK_OTHERS, PERMISSION_REMOVE_NAMERS, PERMISSION_CHAT_COLOR;

	private static PC pc = PC.p;

	private String getPermission() {
		switch (this) {
		case PERMISSION_NICK:
			return "PinkiRanks.nick";
		case PERMISSION_NICK_OTHERS:
			return "PinkiRanks.nick-others";
		case PERMISSION_CHAT_COLOR:
			return "PinkiRanks.chat-color";
		case PERMISSION_REMOVE_NAMERS:
			return "PinkiRanks.remove-namers";
		case PERMISSION_RELOAD:
			return "PinkiRanks.reload";
		default:
			break;
		}
		return null;
	}
	
	public static String getPermission(permissionsUtil permission) {
		return permission.getPermission();
	}

	public static boolean havePermission(Player player, permissionsUtil permission) {
		if (player.hasPermission(permission.getPermission())) {
			return true;
		} else {
			player.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.no-permission")));
		}
		return false;
	}

	public static boolean havePermission(CommandSender sender, permissionsUtil permission) {
		if (sender.hasPermission(permission.getPermission())) {
			return true;
		} else {
			sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.no-permission")));
		}
		return false;
	}

}
