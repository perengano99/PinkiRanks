package com.perengano99.PinkiRanks.files;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import com.perengano99.PinkiRanks.PC;

public class ConfigFile {

	private static PC pc = PC.p;
	private static String filename = "config.yml";
	
	private static File File = FileManager.getFile(filename);
	private static FileConfiguration Config;
	
	public static FileConfiguration getConfig() {
		return Config;
	}
	
	public static void loadFile() {
		pc.log("--------------------");
		pc.log("Loading " + filename);
		FileManager.loadFile(filename);
		Config = FileManager.getConfiguration(filename);
		pc.log(File.getName() + " loaded");
		pc.log("--------------------");
	}
	
	public static void saveFile() {
		pc.saveConfig();
	}
}
