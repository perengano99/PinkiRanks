package com.perengano99.PinkiRanks.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.ConfigFile;

public class helpUtil {

	private static PC pc = PC.p;

	public static List<String> helpMessage() {
		ConfigurationSection msgs = ConfigFile.getConfig().getConfigurationSection("messages");
		List<String> lst = new ArrayList<String>();
		lst.clear();
		lst.add(pc.color(msgs.getString("help-header")));
		lst.add(pc.color("&d&o/pinkiranks" + " &r&6/pr &r" + msgs.getString("help-pr")));
		if (!ConfigFile.getConfig().getBoolean("general.only-subcommands", true)) {
			lst.add(pc.color("&d&o/nick" + "&r " + msgs.getString("help-nick")));
			lst.add(pc.color("&d&o/nick <player>" + "&r " + msgs.getString("help-nick")));
			lst.add(pc.color("&d&o/nick reset" + "&r " + msgs.getString("help-nick-reset")));
		} else {
			lst.add(pc.color("&d&o/pr nick" + "&r " + msgs.getString("help-nick")));
			lst.add(pc.color("&d&o/pr nick <player>" + "&r " + msgs.getString("help-nick")));
			lst.add(pc.color("&d&o/pr nick reset" + "&r " + msgs.getString("help-nick-reset")));
		}
		lst.add(pc.color("&d&o/pr removenamers" + "&r " + msgs.getString("help-remove-namers")));
		lst.add(pc.color("&d&o/pr reload" + "&r " + msgs.getString("help-reload")));
		lst.add(pc.color(msgs.getString("help-footer")));

		return lst;
	}
	
	public static void sendHelpMessage(Player player) {
		for (String message : helpMessage()) {
			player.sendMessage(pc.color(message));
		}
	}
	
	public static void sendHelpMessage(CommandSender sender) {
		for (String message : helpMessage()) {
			sender.sendMessage(pc.color(message));
		}
	}

}
