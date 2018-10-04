package com.perengano99.PinkiRanks.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Maps;
import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.NametagAPI.NameTagChanger;
import com.perengano99.PinkiRanks.files.ConfigFile;
import com.perengano99.PinkiRanks.files.RanksFile;
import com.perengano99.PinkiRanks.listener.Rank;
import com.perengano99.PinkiRanks.listener.Scoreboards;
import com.perengano99.PinkiRanks.nametag.CreateNametag;
import com.perengano99.PinkiRanks.nametag.NametagUtil;
import com.perengano99.PinkiRanks.placeholder.LegacyPlaceholders;

public class updaterUtil implements Listener {

	private static int idCU = -1;
	private static int idTLU = -1;
	private static int idGEN = -1;
	private static int idANI = -1;

	private static PC pc = PC.p;

	private static HashMap<Player, String> cancel = Maps.newHashMap();
	private static HashMap<Player, String> cancel2 = Maps.newHashMap();

	public static void stop() {

		Bukkit.getScheduler().cancelTask(idTLU);
		idTLU = -1;

		Bukkit.getScheduler().cancelTask(idCU);
		idCU = -1;

		Bukkit.getScheduler().cancelTask(idGEN);
		idGEN = -1;
		
		Bukkit.getScheduler().cancelTask(idANI);
		idANI = -1;

		CreateNametag.stopNamer();

	}

	public static void update() {
		NameUpdater();
		ChatUpdater();
		TablistUpdater();
		CreateNametag.loadNamer();
		NametagUtil.loadNames();
		NameTagUpdater();
		updateAnimations();

	}

	public static void NameUpdater() {
		idGEN = Bukkit.getScheduler().scheduleSyncRepeatingTask(PC.p, new Runnable() {

			public void run() {

				for (Player p : pc.getServer().getOnlinePlayers()) {
					if (!cancel.containsKey(p)) {
						updateCustomName(p);
						cancel.put(p, "cancel");

					}

				}
			}
		}, 0, ConfigFile.getConfig().getInt("general.update-delay"));
	}

	public static void ChatUpdater() {
		idCU = Bukkit.getScheduler().scheduleSyncRepeatingTask(pc, new Runnable() {

			public void run() {

				for (Player p : pc.getServer().getOnlinePlayers()) {
					updateChat(p);

					if (!ConfigFile.getConfig().getBoolean("chat.use-layout", true)) {
						updateChat(p);
						Bukkit.getScheduler().cancelTask(idCU);
						idCU = -1;
					}
				}
			}
		}, 0, ConfigFile.getConfig().getInt("general.update-delay"));
	}

	public static void TablistUpdater() {
		idTLU = Bukkit.getScheduler().scheduleSyncRepeatingTask(pc, new Runnable() {

			public void run() {

				for (Player p : pc.getServer().getOnlinePlayers()) {
					updateTablist(p);

					if (!ConfigFile.getConfig().getBoolean("general.use-tablist", true)) {
						updateTablist(p);
						Bukkit.getScheduler().cancelTask(idTLU);
						idTLU = -1;
					}
				}
			}
		}, 0, ConfigFile.getConfig().getInt("general.update-delay"));
	}

	public static void NameTagUpdater() {
		idGEN = Bukkit.getScheduler().scheduleSyncRepeatingTask(pc, new Runnable() {

			public void run() {

				for (Player p : pc.getServer().getOnlinePlayers()) {
					updateNameTags(p);

					if (!ConfigFile.getConfig().getBoolean("general.use-nametags", true)) {
						updateNameTags(p);
					}
				}

			}
		}, 0, ConfigFile.getConfig().getInt("general.update-delay"));
	}

	public static void updateCustomName(Player p) {
		if (NametagUtil.isRenamed(p)) {
			String nametag = NametagUtil.getNickName(p);
			NameTagChanger.changePlayerName(p, nametag);

		}
	}

	public static void updateChat(Player p) {
		if (NametagUtil.isRenamed(p)) {
			p.setDisplayName(pc.color(NametagUtil.getNickName(p)));
		} else {
			p.setDisplayName(p.getName());
		}
	}

	public static void updateTablist(Player p) {

		Rank rank = Rank.getRank(p);

		String format = pc
				.color(LegacyPlaceholders.applyVariables(p, rank.getTBPrefix() + p.getName() + rank.getTBSuffix()));

		String formatNick = pc.color(LegacyPlaceholders.applyVariables(p,
				rank.getTBPrefix() + NametagUtil.getNickName(p) + rank.getTBSuffix()));

		String aniformat = pc.color(LegacyPlaceholders.apllyAnimations(
				LegacyPlaceholders.applyVariables(p, rank.getTBPrefix() + p.getName() + rank.getTBSuffix())));

		String aniformatNick = pc.color(LegacyPlaceholders.apllyAnimations(LegacyPlaceholders.applyVariables(p,
				rank.getTBPrefix() + NametagUtil.getNickName(p) + rank.getTBSuffix())));

		if (PC.p.getConfig().getBoolean("general.use-tablist", true)) {
			if (rank.hasTablist()) {
				if (!NametagUtil.isRenamed(p)) {
					if (!RanksFile.getConfig().getBoolean(rank.getName() + ".tablist.animated", true)) {
						p.setPlayerListName(format);
					} else {
						p.setPlayerListName(aniformat);
					}
				} else {
					if (!RanksFile.getConfig().getBoolean(rank.getName() + ".tablist.animated", true)) {
						p.setPlayerListName(formatNick);
					} else {
						p.setPlayerListName(aniformatNick);
					}
				}

			} else {
				p.setPlayerListName(p.getName());
			}
		} else {
			p.setPlayerListName(p.getName());
		}
	}

	@SuppressWarnings("deprecation")
	public static void updateAnimations() {

		idANI = Bukkit.getScheduler().scheduleAsyncRepeatingTask(PC.p, new Runnable() {

			@Override
			public void run() {
				animationsUtil.upanim();
			}
		}, 0, ConfigFile.getConfig().getInt("general.update-delay"));
	}

	public static void removeTeams() {
		for (Player p : pc.getServer().getOnlinePlayers()) {

			Scoreboard scoreboard = Scoreboards.getScoreboard(p);
			Team team = scoreboard.getTeam(p.getName());
			Team team2 = scoreboard.getTeam(NametagUtil.getNickName(p));
			team.unregister();
			team2.unregister();
		}
	}

	public static void updateNameTags(Player p) {

		if (!cancel2.containsKey(p)) {
			Scoreboards.loadScoreboardTeam(Scoreboards.getScoreboard(p), p);
			cancel2.put(p, "cancel");
		}
		if (NametagUtil.updatePlayer.contains(p)) {

			p.setScoreboard(Scoreboards.getScoreboard(p));
			NametagUtil.updatePlayer.remove(p);
		}
	}

	public static void removeCancel() {
		for (Player p : pc.getServer().getOnlinePlayers()) {
			if (cancel.containsKey(p)) {
				cancel.remove(p);
			}
			if (cancel2.containsKey(p)) {
				cancel2.remove(p);
			}
		}
	}

	@EventHandler
	public void removeCancel(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (cancel.containsKey(p)) {
			cancel.remove(p);
		}
		if (cancel2.containsKey(p)) {
			cancel2.remove(p);
		}
	}

}
