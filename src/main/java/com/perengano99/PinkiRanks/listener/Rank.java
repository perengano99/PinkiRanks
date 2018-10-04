package com.perengano99.PinkiRanks.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.ConfigFile;
import com.perengano99.PinkiRanks.files.RanksFile;

public class Rank {

	private static PC pc = PC.p;

	public static Map<String, Rank> ranks = new HashMap<String, Rank>();

	private static int maxint = 1000;
	int index;
	int indexg = ConfigFile.getConfig().getInt("general.no-tablist-rank-priority");
	String prefix;
	String suffix;
	boolean tablist;
	boolean tablist_anim;
	boolean nametag;
	boolean nametag_anim;
	boolean bypassOP;
	String tablist_prefix;
	String tablist_suffix;
	String nametag_prefix;
	String nametag_suffix;
	String nametag_color_color;
	String name;
	String layout;
	String color;
	String tabweight;
	String tabweightg;
	String currentNTcolor;

	public Rank(String name) {
		this.name = name;
		this.prefix = RanksFile.getConfig().getString(name + ".prefix");
		this.suffix = RanksFile.getConfig().getString(name + ".suffix");
		this.tablist = RanksFile.getConfig().getBoolean(name + ".tablist.enabled", true);
		this.tablist_anim = RanksFile.getConfig().getBoolean(name + ".tablist.animated", true);
		this.tablist_prefix = RanksFile.getConfig().getString(name + ".tablist.prefix");
		this.tablist_suffix = RanksFile.getConfig().getString(name + ".tablist.suffix");
		this.nametag = RanksFile.getConfig().getBoolean(name + ".nametag.enabled", true);
		this.nametag_prefix = RanksFile.getConfig().getString(name + ".nametag.prefix");
		this.nametag_suffix = RanksFile.getConfig().getString(name + ".nametag.suffix");
		this.nametag_color_color = RanksFile.getConfig().getString(name + ".nametag.color");
		this.nametag_anim = RanksFile.getConfig().getBoolean(name + ".nametag.animated", true);
		this.color = RanksFile.getConfig().getString(name + ".message-color");
		this.bypassOP = RanksFile.getConfig().getBoolean(name + ".bypass-OP", true);
		this.layout = RanksFile.getConfig().getString(name + ".chat-layout");
		this.index = RanksFile.getConfig().getInt(name + ".tablist.tab-priority");

		if ((index < 0) || (index > maxint)) {
			this.tabweight = "A";
			pc.log(Level.WARNING,
					"Rank \"" + name + "\" has a TAB Priority higher than " + maxint + " or smaller than 0");
			pc.log("This is not allowed and must be immediately fixed in the ranks.yml! the priority must be less than"
					+ maxint + "and greater than 0");
		} else {
			this.tabweight = String.valueOf(maxint - index);
		}

		if ((indexg < 0) || (indexg > maxint)) {
			this.tabweightg = "A";
			PC.p.log(Level.WARNING, "general TAB Priority higher than " + maxint + " or smaller than 0");
			PC.p.log(
					"This is not allowed and must be immediately fixed in the config.yml! the priority must be less than"
							+ maxint + "and greater than 0");
		} else {
			this.tabweightg = String.valueOf(maxint - index);
		}
		ranks.put(name, this);
	}
	
	public static void reload() {
		ranks.clear();
	}
	

	public String getName() {
		return this.name;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public boolean hasTablist() {
		return this.tablist;
	}

	public boolean hasNametag() {
		return this.nametag;
	}

	public boolean hasTBAnim() {
		return this.tablist_anim;
	}

	public boolean hasNTAnim() {
		return this.nametag_anim;
	}

	public String getTBPrefix() {
		return this.tablist_prefix;
	}

	public String getTBSuffix() {
		return this.tablist_suffix;
	}

	public String getNTPrefix() {
		return this.nametag_prefix;
	}

	public String getNTSuffix() {
		return this.nametag_suffix;
	}

	public String getNTColor() {
		String color = "abcdef1234567890onmklr";

		if (this.nametag_color_color.equalsIgnoreCase("")) {
			return "&r";
		}
		
		String[] format = this.nametag_color_color.split("&");

			for (String chars : format) {

				if (chars.length() > 2 || !color.contains(chars)) {
					pc.log(Level.WARNING, "Invalid characters in " + this.name
							+ ".nametag.color.color in ranks.yml, you can only use color codes!");
					return "&r";
				}
		}

		return this.nametag_color_color;
	}

	public String getChatColor() {
		String color = "abcdef1234567890onmklr";

		if (this.color.equalsIgnoreCase("")) {
			return "&r";
		}
		
		String[] format = this.color.split("&");

			for (String chars : format) {

				if (chars.length() > 2 || !color.contains(chars)) {
					pc.log(Level.WARNING, "Invalid characters in " + this.name
							+ ".nametag.color.color in ranks.yml, you can only use color codes!");
					return "&r";
				}
		}
		
		return this.color;
	}

	public boolean canBypassOP() {
		return this.bypassOP;
	}

	public String getChatFormat() {
		return this.layout;
	}

	public String getTABWeight() {
		return this.tabweight;
	}

	public String getTABWeightg() {
		return this.tabweightg;
	}

	public String getCurrentNTC() {
		return this.currentNTcolor;
	}

	public void setCurrenNTC(String color) {
		this.currentNTcolor = color;
	}

	public static Rank get(String rank) {
		return ranks.containsKey(rank) ? (Rank) ranks.get(rank) : new Rank(rank);
	}

	public static Rank getRank(Player p) {
		for (String rank : RanksFile.getConfig().getStringList("ranks")) {
			String permission = RanksFile.getConfig().getString(rank + ".required-permission");
			if (permission.equalsIgnoreCase("")) {
				return Rank.get(rank);
			}
			Rank group = Rank.get(rank);
			if (group.canBypassOP()) {
				if (p.hasPermission(new Permission(permission, PermissionDefault.FALSE))) {
					return group;
				}
			} else if (p.hasPermission(permission)) {
				return group;
			} else if (!p.hasPermission(permission)) {
				List<String> list = RanksFile.getConfig().getStringList("ranks");
				int line = list.size() - 1;
				Rank ugroup = Rank.get(list.get(line));
				return ugroup;
			}
		}
		return null;
	}

}
