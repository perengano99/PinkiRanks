package com.perengano99.PinkiRanks.placeholder;

import org.bukkit.entity.Player;

import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.AnimationsFile;
import com.perengano99.PinkiRanks.files.ReplacerTextFile;
import com.perengano99.PinkiRanks.listener.Animations;
import com.perengano99.PinkiRanks.listener.Emojis;
import com.perengano99.PinkiRanks.listener.Rank;
import com.perengano99.PinkiRanks.nametag.NametagUtil;
import com.perengano99.PinkiRanks.util.animationsUtil;

public class LegacyPlaceholders {
	
	private static PC pc = PC.p;

	public static String applyVariables(Player p, String format) {
		String rank = Rank.getRank(p).getName();
		String namecolor = "";
		String nametag = NametagUtil.getNickName(p);

		if (format.contains("{PLAYER}")) {
			format = format.replace("{PLAYER}", pc.color(namecolor) + "%s");
		}
		if (format.contains("{MESSAGE}")) {
			format = format.replace("{MESSAGE}", "%s");
		}
		if (format.contains("{PREFIX}")) {
			format = format.replace("{PREFIX}", Rank.getRank(p).getPrefix());
		}
		if (format.contains("{SUFFIX}")) {
			format = format.replace("{SUFFIX}", Rank.getRank(p).getSuffix());
		}
		if (format.contains("{WORLD}")) {
			format = format.replace("{WORLD}", p.getWorld().getName());
		}
		if (format.contains("{RANK}")) {
			format = format.replace("{RANK}", rank);
		}
		if (NametagUtil.isRenamed(p)) {
			if (format.contains("{NICK}")) {
				format = format.replace("{NICK}", pc.color(nametag));
			}
		}
			
		return format;
	}
	
	public static String applyReplaces(Player player, String format) {
		
		for (String str : ReplacerTextFile.getConfig().getStringList("replaces")) {
			Emojis emojis = Emojis.getEmoji(str);
			if (format.contains(emojis.getFrom())) {
				if (emojis.getPerm().equalsIgnoreCase("")) {
					format = format.replace(emojis.getFrom(), emojis.getTo() + Rank.getRank(player).getChatColor());
				}
				if (player.hasPermission(emojis.getPerm())) {
					format = format.replace(emojis.getFrom(), emojis.getTo() + Rank.getRank(player).getChatColor());
				}
			}
		}
		
		return format;
	}
	
	public static String apllyAnimations(String format) {
		
		for (String str : AnimationsFile.getConfig().getStringList("animations")) {
			Animations ani = Animations.getRank(str);
			String name = ani.getName();

			if (format.contains("{" + name + "}")) {
				format = format.replace("{" + name + "}", animationsUtil.Animation.get(name));
			}
		}
		
		return format;
	}
}
