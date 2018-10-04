package com.perengano99.PinkiRanks.listener;

import java.util.HashMap;
import java.util.Map;

import com.perengano99.PinkiRanks.files.ReplacerTextFile;


public class Emojis {
	
	public static Map<String, Emojis> emojis = new HashMap<String, Emojis>();
	
	String from;
	String to;
	String perm;
	String name;
	
	
	public Emojis(String name) {
		this.name = name;
		this.from = ReplacerTextFile.getConfig().getString(name + ".from");
		this.to = ReplacerTextFile.getConfig().getString(name + ".to");
		this.perm = ReplacerTextFile.getConfig().getString(name + ".required-permission");
		
		emojis.put(name, this);
	}
	
	public static void reload() {
		emojis.clear();
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFrom() {
		return this.from;
	}
	
	public String getTo() {
		return this.to;
	}
	
	public String getPerm() {
		return this.perm;
	}
	
	private static Emojis get(String rank) {
		return emojis.containsKey(rank) ? (Emojis) emojis.get(rank) : new Emojis(rank);
	}

	public static Emojis getEmoji(String name) {
				Emojis group = Emojis.get(name);
					return group;
	}
	
}
