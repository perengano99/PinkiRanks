package com.perengano99.PinkiRanks.NametagAPI;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.NametagAPI.packets.ChannelPacketHandler;
import com.perengano99.PinkiRanks.NametagAPI.packets.GameProfileWrapper;
import com.perengano99.PinkiRanks.NametagAPI.packets.IPacketHandler;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.logging.Level;

public class NameTagChanger {

	public static final NameTagChanger INSTANCE = new NameTagChanger();

	public static boolean sendingPackets;
	private static IPacketHandler packetHandler;
	public static HashMap<UUID, GameProfileWrapper> gameProfiles = Maps.newHashMap();


	private static PC pc = PC.p;

	private Plugin plugin;

	public NameTagChanger() {
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			if (plugin.getClass().getProtectionDomain().getCodeSource()
					.equals(this.getClass().getProtectionDomain().getCodeSource())) {
				this.plugin = plugin;
			}
		}
		enable();
	}

	public static boolean checkPreConditions(Player player, String name) {

		if (player == null) {
			pc.log(Level.WARNING, "The player cannot be null!");
			return false;
		}

		if (name == null) {
			pc.log(Level.WARNING, "The new name cannot be null!");
			return false;
		}

		if (name.equalsIgnoreCase(player.getName())) {
			pc.log(Level.WARNING,
					"The new name cannot be the same as the player's! If you intended to reset the player's name, use resetPlayerName()!");
			return false;
		}

		for (Player players : pc.getServer().getOnlinePlayers()) {
			if (name.equalsIgnoreCase(players.getName())) {
				pc.log(Level.WARNING, "The new name cannot be the same as other players!");
				return false;
			}
		}

		for (OfflinePlayer players : pc.getServer().getOfflinePlayers()) {
			if (name.equalsIgnoreCase(players.getName())) {
				pc.log(Level.WARNING, "The new name cannot be the same as other players!");
				return false;
			}
		}

		return true;
	}

	public static void changePlayerName(Player player, String newName) {

		if (checkPreConditions(player, newName) == false) {
			return;
		}

		GameProfileWrapper profile = new GameProfileWrapper(player.getUniqueId(), pc.nocolor(newName));
		if (gameProfiles.containsKey(player.getUniqueId())) {
			profile.getProperties().putAll(gameProfiles.get(player.getUniqueId()).getProperties());
		} else {
			profile.getProperties().putAll(packetHandler.getDefaultPlayerProfile(player).getProperties());
		}
		gameProfiles.put(player.getUniqueId(), profile);
		updatePlayer(player, player.getName());
	}

	public static void resetPlayerName(Player player) {
		if (player == null || !gameProfiles.containsKey(player.getUniqueId())) {
			return;
		}
		GameProfileWrapper oldProfile = gameProfiles.get(player.getUniqueId());
		GameProfileWrapper newProfile = packetHandler.getDefaultPlayerProfile(player);
		newProfile.getProperties().removeAll("textures");
		if (oldProfile.getProperties().containsKey("textures")) {
			newProfile.getProperties().putAll("textures", oldProfile.getProperties().get("textures"));
		}
		gameProfiles.put(player.getUniqueId(), newProfile);
		updatePlayer(player, oldProfile.getName());
		checkForRemoval(player);
	}

	private static void checkForRemoval(Player player) {
		if (gameProfiles.get(player.getUniqueId()).equals(packetHandler.getDefaultPlayerProfile(player))) {
			gameProfiles.remove(player.getUniqueId());
		}
	}

	public static void updatePlayer(Player player) {
		updatePlayer(player, null);
	}

	private static void updatePlayer(Player player, String oldName) {
		GameProfileWrapper newProfile = gameProfiles.get(player.getUniqueId());
		if (newProfile == null) {
			newProfile = packetHandler.getDefaultPlayerProfile(player);
		}
		List<Team> scoreboardTeamsToUpdate = Lists.newArrayList();
		sendingPackets = true;
		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			if (otherPlayer.equals(player)) {
				if (otherPlayer.getScoreboard().getEntryTeam(player.getName()) != null) {
					scoreboardTeamsToUpdate.add(otherPlayer.getScoreboard().getEntryTeam(player.getName()));
				}
				continue;
			}
			if (otherPlayer.canSee(player)) {
				packetHandler.sendTabListRemovePacket(player, otherPlayer);
				packetHandler.sendTabListAddPacket(player, newProfile, otherPlayer);
				if (otherPlayer.getWorld().equals(player.getWorld())) {
					packetHandler.sendEntityDestroyPacket(player, otherPlayer);
					packetHandler.sendNamedEntitySpawnPacket(player, otherPlayer);
					packetHandler.sendEntityEquipmentPacket(player, otherPlayer);
				}
			}

			if (otherPlayer.getScoreboard().getEntryTeam(player.getName()) != null) {
				scoreboardTeamsToUpdate.add(otherPlayer.getScoreboard().getEntryTeam(player.getName()));
			}
		}
		if (oldName != null) {
			String newName = newProfile.getName();
			for (Team team : scoreboardTeamsToUpdate) {
				Bukkit.getOnlinePlayers().stream().filter(p -> p.getScoreboard() == team.getScoreboard()).forEach(p -> {
					packetHandler.sendScoreboardRemovePacket(oldName, p, team.getName());
					packetHandler.sendScoreboardAddPacket(newName, p, team.getName());
				});
			}
		}
		sendingPackets = false;
	}

	public static Map<UUID, String> getChangedPlayers() {
		Map<UUID, String> changedPlayers = Maps.newHashMap();
		for (Map.Entry<UUID, GameProfileWrapper> entry : gameProfiles.entrySet()) {
			changedPlayers.put(entry.getKey(), entry.getValue().getName());
		}
		return Collections.unmodifiableMap(changedPlayers);
	}

	public void disable() {
		for (UUID uuid : new ArrayList<>(gameProfiles.keySet())) {
			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				continue;
			}
			resetPlayerName(player);
		}
		gameProfiles.clear();
		packetHandler.shutdown();
		packetHandler = null;
	}

	public void enable() {
		if (plugin == null) {
			return;
		}

		packetHandler = new ChannelPacketHandler(plugin);

	}

}
