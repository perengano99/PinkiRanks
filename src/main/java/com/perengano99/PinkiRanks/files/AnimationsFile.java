package com.perengano99.PinkiRanks.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import com.perengano99.PinkiRanks.PC;

public class AnimationsFile {
	
	private static PC pc = PC.p;
	private static String filename = "animations.yml";
	
	private static File File = FileManager.getFile(filename);
	private static FileConfiguration Config;
	
	public static void loadFile() {
		pc.log("--------------------");
		pc.log("Loading " + filename);
		FileManager.loadFile(filename);
		Config = FileManager.getConfiguration(filename);
		pc.log(File.getName() + " loaded");
		pc.log("Loaded animations:");
		for (String str : FileManager.getConfiguration(filename).getStringList("animations"))
			pc.log(str);
		pc.log("--------------------");
	}
	
	public static FileConfiguration getConfig() {
		return Config;
	}
	
	public static void save() {
		try {
			Config.save(File);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
