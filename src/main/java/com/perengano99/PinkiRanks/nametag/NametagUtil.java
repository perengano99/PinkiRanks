package com.perengano99.PinkiRanks.nametag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;
import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.NametagAPI.NameTagChanger;
import com.perengano99.PinkiRanks.files.NicksFile;

public class NametagUtil {

	private static PC pc = PC.p;

	private static HashMap<UUID, String> nickNames = Maps.newHashMap();
	public static HashMap<UUID, Boolean> playersRenamed = Maps.newHashMap();
	public static List<Player> updatePlayer = new ArrayList<Player>();

	public static boolean isRenamed(Player player) {
		if (playersRenamed.containsKey(player.getUniqueId())) {
			return true;
		}
		return false;
	}

	public static String getNickName(Player player) {
		return nickNames.get(player.getUniqueId());
	}

	@SuppressWarnings("deprecation")
	public static void setNameTag(Player player, String newName) {

		List<String> listnicks = NicksFile.getConfig().getStringList("nicks");

		if (NameTagChanger.checkPreConditions(player, newName) == false) {
			return;
		}

		if (listnicks.contains(player.getName() + ": " + nickNames.get(player.getUniqueId()))) {
			listnicks.remove(player.getName() + ": " + nickNames.get(player.getUniqueId()));
			NicksFile.getConfig().set("nicks", listnicks);
			NicksFile.save();
		}
		Bukkit.getScheduler().scheduleAsyncDelayedTask(pc, new BukkitRunnable() {

			@Override
			public void run() {
				nickNames.put(player.getUniqueId(), newName);
				listnicks.add(player.getName() + ": " + nickNames.get(player.getUniqueId()));
				NicksFile.getConfig().set("nicks", listnicks);
				NicksFile.save();
				playersRenamed.put(player.getUniqueId(), true);
			}

		}, 3L);

		NameTagChanger.changePlayerName(player, newName);

	}

	public static void resetNameTag(Player p) {

		List<String> listnicks = NicksFile.getConfig().getStringList("nicks");

		if (listnicks.contains(p.getName() + ": " + nickNames.get(p.getUniqueId()))) {
			listnicks.remove(p.getName() + ": " + nickNames.get(p.getUniqueId()));
			NicksFile.getConfig().set("nicks", listnicks);
			NicksFile.save();
		}

		playersRenamed.remove(p.getUniqueId());
		nickNames.remove(p.getUniqueId());

		NameTagChanger.resetPlayerName(p);
		updatePlayer.add(p);

	}

	@SuppressWarnings("deprecation")
	public static void loadNames() {

		List<String> listnicks = NicksFile.getConfig().getStringList("nicks");

		for (String str : listnicks) {
			String[] words = str.split(": ");
			nickNames.put(pc.getServer().getOfflinePlayer(words[0]).getUniqueId(), words[1]);
			playersRenamed.put(pc.getServer().getOfflinePlayer(words[0]).getUniqueId(), true);
		}

		for (UUID uuid : new ArrayList<>(NameTagChanger.gameProfiles.keySet())) {
			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				continue;
			}
			if (!listnicks.contains(player.getName() + ": " + nickNames.get(player.getUniqueId()))) {
				player.sendMessage("popo");
				nickNames.remove(player.getUniqueId());
				playersRenamed.remove(player.getUniqueId());
				NameTagChanger.resetPlayerName(player);
			}
		}
	}

	public static void clearNameTagNamers() {
		List<Entity> size = new ArrayList<Entity>();
		for (org.bukkit.World worlds : pc.getServer().getWorlds()) {
			for (Entity entitys : worlds.getEntities()) {
				if (entitys instanceof ArmorStand) {
					if (entitys.getScoreboardTags().contains("PinkiMobNamer")) {
						size.add(entitys);
						entitys.remove();
					}
				}
			}

		}
		pc.log(size.size() + " name-creating entities have been removed");
		size.clear();
	}

}
