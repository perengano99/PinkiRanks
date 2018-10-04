package com.perengano99.PinkiRanks.util;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.perengano99.PinkiRanks.files.AnimationsFile;
import com.perengano99.PinkiRanks.listener.Animations;

public class animationsUtil {
	
	private static int line;
	public static HashMap<String, String> Animation = Maps.newHashMap();

	public static void upanim() {
		for (String str : AnimationsFile.getConfig().getStringList("animations")) {
			Animations ani = Animations.getRank(str);
			line = ani.getListLines();

			updateAnimations(ani, line);
		}
	}

	private static void updateAnimations(Animations ani, int line) {

		if (line >= ani.getListSize()) {
			ani.resetListLines();
		}

		if (line < ani.getListSize()) {

			Animation.put(ani.getName(), ani.getList().get(line));

		}
		ani.addListLines();
	}

}
