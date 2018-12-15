package com.perengano99.PinkiRanks.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Maps;
import com.perengano99.PinkiRanks.PC;

public class FileManager extends PC {
	private static PC pc = PC.p;

	public static HashMap<String, Boolean> newFile = Maps.newHashMap();

	public static void loadFiles() {
		ConfigFile.loadFile();
		AnimationsFile.loadFile();
		ReplacerTextFile.loadFile();
		RanksFile.loadFile();
		NicksFile.loadFile();
	}

	public static File getFile(String name) {
		return new File(pc.getDataFolder(), name);
	}

	public static FileConfiguration getConfiguration(String name) {
		File file = new File(pc.getDataFolder(), name);
		InputStream defLangStream = pc.getResource(name);
		if (!newFile.containsKey(name)) {
			return YamlConfiguration.loadConfiguration(file);
		} else {
		return YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(defLangStream)));
		}
	}

	public static void loadFile(String fileName) {
		File lang = new File(pc.getDataFolder(), fileName);
		OutputStream out = null;
		InputStream defLangStream = pc.getResource(fileName);
		if (!lang.exists()) {
			newFile.put(fileName, true);
			try {
				pc.getDataFolder().mkdir();
				lang.createNewFile();
				if (defLangStream != null) {
					out = new FileOutputStream(lang);
					int read;
					byte[] bytes = new byte[1024];

					while ((read = defLangStream.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (defLangStream != null) {
					try {
						defLangStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

}
