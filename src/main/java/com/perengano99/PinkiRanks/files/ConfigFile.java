package com.perengano99.PinkiRanks.files;

import org.bukkit.configuration.file.FileConfiguration;

import com.perengano99.PinkiRanks.PC;

public class ConfigFile {

	private static PC pc = PC.p;
	
	public static FileConfiguration getConfig() {
		return pc.getConfig();
	}
	
	public static void loadFile() {
		pc.saveDefaultConfig();
	}
	
	public static void saveFile() {
		pc.saveConfig();
	}
}
