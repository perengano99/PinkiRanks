package com.perengano99.PinkiRanks;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.perengano99.PinkiLibraries.PLIB;
import com.perengano99.PinkiLibraries.CommandApi.CommandCreatorAPI;
import com.perengano99.PinkiLibraries.CommandApi.executors.PLIBSubCommand;
import com.perengano99.PinkiRanks.NametagAPI.NameTagChanger;
import com.perengano99.PinkiRanks.cmds.NIckCmd;
import com.perengano99.PinkiRanks.cmds.NickSubCmd;
import com.perengano99.PinkiRanks.cmds.PRRootCommand;
import com.perengano99.PinkiRanks.cmds.ReloadSubCmd;
import com.perengano99.PinkiRanks.cmds.RemoveNamersCmd;
import com.perengano99.PinkiRanks.files.FileManager;
import com.perengano99.PinkiRanks.listener.ChatListener;
import com.perengano99.PinkiRanks.nametag.CreateNametag;
import com.perengano99.PinkiRanks.nametag.NametagUtil;
import com.perengano99.PinkiRanks.util.updaterUtil;

public class PC extends JavaPlugin implements Listener {

	public static PC p;
	private static Plugin plugin;

	public static NameTagChanger tag;
	public static CommandCreatorAPI ccapi = PLIB.CommandCreatorAPI;

	public PC() {
		p = this;
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void onEnable() {

		PC.p.log("--------------------");
		PC.p.log("");
		PC.p.log("");
		PC.p.log(PC.p.getName());
		PC.p.log("Loading " + PC.p.getName());
		PC.p.log("");
		PC.p.log("");
		PC.p.log("--------------------");

		if (!getConfig().getBoolean("general.only-subcommands", true)) {
			ccapi.newCommand(this, "nick", new NIckCmd());
		}
		ccapi.newRootCommand(this, "pinkiranks", new PRRootCommand(), new PLIBSubCommand[]{new NickSubCmd("nick"), new RemoveNamersCmd("removenamers"), new ReloadSubCmd("reload")});

		FileManager.loadFiles();
		
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new updaterUtil(), this);
		Bukkit.getPluginManager().registerEvents(new CreateNametag(), this);
		
		updaterUtil.update();
		
		super.onEnable();

		PC.p.log("--------------------");
		PC.p.log("");
		PC.p.log(PC.p.getName() + " loaded");
		PC.p.log("");
		PC.p.log("--------------------");

	}

	@Override
	public void onDisable() {
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (NametagUtil.isRenamed(player) == true) {
			String nametag = NametagUtil.getNickName(player);
			NameTagChanger.changePlayerName(player, nametag);
			NametagUtil.playersRenamed.put(player.getUniqueId(), true);
		}

	}

	@EventHandler
	public void preventConsoleServerReload(ServerCommandEvent event) {

		if (event.getCommand().equalsIgnoreCase("reload")) {
			if (getConfig().getBoolean("force-restart-on-reload", true)) {
				log(Level.WARNING, this.getName() + " does not accept /reload command, restarting the server");
				event.setCommand("restart");
			} else {
				log(Level.WARNING, this.getName() + " does not accept /reload command, kicking players, it is recomended to restart the server!");
				for (Player players : getServer().getOnlinePlayers()) {
					players.kickPlayer(getConfig().getString("on-reload-kick-message"));
				}
			}

		}

	}

	@EventHandler
	public void prevetnPlayerServerReload(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equalsIgnoreCase("/reload")) {
			if (getConfig().getBoolean("force-restart-on-reload", true)) {
				log(Level.WARNING, this.getName() + " does not accept /reload command, restarting the server");
				event.setMessage("/restart");
			} else {
				log(Level.WARNING, this.getName() + " does not accept /reload command, kicking players, it is recomended to restart the server!");
				for (Player players : getServer().getOnlinePlayers()) {
					players.kickPlayer(getConfig().getString("on-reload-kick-message"));
				}
			}
		}
	}

	public void log(String msg) {
		Bukkit.getLogger().log(Level.INFO, msg);
	}

	public void log(Object invoke) {
		Bukkit.getLogger().log(Level.INFO, "", invoke);

	}

	public void log(Level level, String msg) {
		Bukkit.getLogger().log(level, "[" + this.getDescription().getFullName() + "] " + msg);
	}

	public String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public String nocolor(String string) {
		if (string.contains("&a")) {
			string = string.replace("&a", "");
		}
		if (string.contains("&b")) {
			string = string.replace("&b", "");
		}
		if (string.contains("&c")) {
			string = string.replace("&c", "");
		}
		if (string.contains("&d")) {
			string = string.replace("&d", "");
		}
		if (string.contains("&e")) {
			string = string.replace("&e", "");
		}
		if (string.contains("&f")) {
			string = string.replace("&f", "");
		}
		if (string.contains("&1")) {
			string = string.replace("&1", "");
		}
		if (string.contains("&2")) {
			string = string.replace("&2", "");
		}
		if (string.contains("&3")) {
			string = string.replace("&3", "");
		}
		if (string.contains("&4")) {
			string = string.replace("&4", "");
		}
		if (string.contains("&5")) {
			string = string.replace("&5", "");
		}
		if (string.contains("&6")) {
			string = string.replace("&6", "");
		}
		if (string.contains("&7")) {
			string = string.replace("&7", "");
		}
		if (string.contains("&8")) {
			string = string.replace("&8", "");
		}
		if (string.contains("&9")) {
			string = string.replace("&9", "");
		}
		if (string.contains("&0")) {
			string = string.replace("&0", "");
		}
		if (string.contains("&n")) {
			string = string.replace("&n", "");
		}
		if (string.contains("&o")) {
			string = string.replace("&o", "");
		}
		if (string.contains("&m")) {
			string = string.replace("&m", "");
		}
		if (string.contains("&k")) {
			string = string.replace("&k", "");
		}
		if (string.contains("&l")) {
			string = string.replace("&l", "");
		}

		string = ChatColor.stripColor(string);

		return string;
	}

}
