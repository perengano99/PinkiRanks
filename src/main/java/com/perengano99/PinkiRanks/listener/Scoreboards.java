package com.perengano99.PinkiRanks.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Maps;
import com.perengano99.PinkiRanks.files.ConfigFile;

@SuppressWarnings("deprecation")
public class Scoreboards {

	public static HashMap<UUID, Scoreboard> boards = Maps.newHashMap();
	
	public static void loadScoreboardTeam(Scoreboard scoreboard, Player p) {
		Rank group = Rank.getRank(p);
		String id = "pr" + group.getTABWeight() + String.valueOf(p.getEntityId());
		String idno = "pr" + group.getTABWeightg() + String.valueOf(p.getEntityId());
		Team team = scoreboard.getTeam(id);
		Team teamno = scoreboard.getTeam(id);
		if (group.hasTablist()) {
			if (team == null) {
				team = scoreboard.registerNewTeam(id);
			}

			if (!(teamno == null)) {
				if (teamno.getPlayers().contains(p)) {
					teamno.removePlayer(p);
				}
			}

			if (!team.getPrefix().equalsIgnoreCase("")) {
				team.setPrefix("");
			}
			if (!team.getSuffix().equalsIgnoreCase("")) {
				team.setSuffix("");
			}
			
			team.setNameTagVisibility(NameTagVisibility.NEVER);
			team.addPlayer(p);

		} else {
			if (teamno == null) {
				teamno = scoreboard.registerNewTeam(idno);
			}

			if (team.getPlayers().contains(p)) {
				team.removePlayer(p);
			}

			if (!teamno.getPrefix().equalsIgnoreCase("")) {
				teamno.setPrefix("");
			}
			if (!teamno.getSuffix().equalsIgnoreCase("")) {
				teamno.setSuffix("");
			}

			if (ConfigFile.getConfig().getBoolean("general.use-nametags", true)) {
				if (!(teamno.getNameTagVisibility() == NameTagVisibility.NEVER)) {
					team.setNameTagVisibility(NameTagVisibility.NEVER);
				}
			} else {
				if (!(teamno.getNameTagVisibility() == NameTagVisibility.ALWAYS)) {
					teamno.setNameTagVisibility(NameTagVisibility.ALWAYS);
				}
			}

			if (!teamno.getPlayers().contains(p)) {
				teamno.addPlayer(p);
			}
		}
	}

	public static Scoreboard getScoreboard(Player p) {
		if (p.getScoreboard() != null) {
			return p.getScoreboard();
		}
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		return manager.getNewScoreboard();
	}
}
