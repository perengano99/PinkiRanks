package com.perengano99.PinkiRanks.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.perengano99.PinkiRanks.files.AnimationsFile;

public class Animations {
	
	public static Map<String, Animations> anims = new HashMap<String, Animations>();

	String name;
	List<String> animations;
	int Line;

	public Animations(String name) {
		this.name = name;
		this.animations = AnimationsFile.getConfig().getStringList(name);
		
		anims.put(name, this);
	}
	
	public static void reload() {
		anims.clear();
	}

	public List<String> getList() {
		return this.animations;
	}
	
	public int getListSize() {
		return this.animations.size();
	}
	
	public int getListLines() {
		return this.Line;
	}
	
	public int addListLines() {
		return this.Line++;
	}
	
	public int resetListLines() {
		return this.Line = 0;
	}

	public String getName() {
		return this.name;
	}

	private static Animations get(String rank) {
		return anims.containsKey(rank) ? (Animations) anims.get(rank) : new Animations(rank);
	}

	public static Animations getRank(String name) {
				Animations group = Animations.get(name);
					return group;
	}

}
