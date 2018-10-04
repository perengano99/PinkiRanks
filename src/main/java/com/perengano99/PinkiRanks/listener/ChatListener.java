package com.perengano99.PinkiRanks.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.TabCompleteEvent;

import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.ConfigFile;
import com.perengano99.PinkiRanks.nametag.NametagUtil;
import com.perengano99.PinkiRanks.placeholder.LegacyPlaceholders;
import com.perengano99.PinkiRanks.util.permissionsUtil;

public class ChatListener implements Listener {

	private PC pc = PC.p;

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {

		String message = event.getMessage();
		String chatcolor = Rank.getRank(event.getPlayer()) != null ? Rank.getRank(event.getPlayer()).getChatColor()
				: "";
		String mentionprefix = ConfigFile.getConfig().getString("mentions.prefix");

		int pitch = ConfigFile.getConfig().getInt("mentions.sound.pitch");

		if (ConfigFile.getConfig().getBoolean("chat.use-layout", true)) {
			String format = ConfigFile.getConfig().getString("chat.layout");
			if (ConfigFile.getConfig().getBoolean("chat.world-specific-layout.enabled", true)) {
				if (ConfigFile.getConfig()
						.contains("chat.world-specific-layout." + event.getPlayer().getWorld().getName())) {
					format = ConfigFile.getConfig()
							.getString("chat.world-specific-layout." + event.getPlayer().getWorld().getName());
				}
			} else if (ConfigFile.getConfig().getBoolean("chat.per-rank-layout", true)) {
				format = Rank.getRank(event.getPlayer()).getChatFormat();
			}
			while (format.contains("[unicode: ")) {
				String unicode = format.substring(format.indexOf("[") + 10, format.indexOf("]"));
				format = format.replace("[unicode: " + unicode + "]",
						String.valueOf((char) Integer.parseInt(unicode, 16)));
			}
			event.setFormat(pc.color(LegacyPlaceholders.applyVariables(event.getPlayer(), format)));
			message = pc.color(chatcolor + message);

			for (Player p : Bukkit.getOnlinePlayers()) {
				if (!ConfigFile.getConfig().getBoolean("mentions.self-mention", true)
						&& event.getPlayer().getName().equalsIgnoreCase(p.getName())) {
					continue;
				} else {

					if ((message.contains(p.getName()) || message.contains(p.getName().toLowerCase())) || (NametagUtil
							.isRenamed(p))
							&& ((message.contains((pc.nocolor(NametagUtil.getNickName(p))))
									|| message.contains((pc.nocolor(NametagUtil.getNickName(p))).toLowerCase())))) {

						if (ConfigFile.getConfig().getBoolean("mentions.enabled", true)) {

							if (ConfigFile.getConfig().getBoolean("mentions.highlight", true)) {
								message = message.replace(p.getName(),
										PC.p.color(mentionprefix + p.getName() + chatcolor));
								message = message.replace(p.getName().toLowerCase(),
										PC.p.color(mentionprefix + p.getName() + chatcolor));

								if (NametagUtil.isRenamed(p)) {
									message = message.replace(pc.nocolor(NametagUtil.getNickName(p)), PC.p.color(
											mentionprefix + (pc.nocolor(NametagUtil.getNickName(p))) + chatcolor));
									message = message.replace(pc.nocolor(NametagUtil.getNickName(p)).toLowerCase(), PC.p.color(
											mentionprefix + (pc.nocolor(NametagUtil.getNickName(p))) + chatcolor));
								}
							}
							if (ConfigFile.getConfig().getBoolean("mentions.sound.enabled", true)) {
								p.playSound(p.getLocation(),
										Sound.valueOf(ConfigFile.getConfig().getString("mentions.sound.sound")),
										SoundCategory.MASTER, 2.0F, pitch);
							}
							if (ConfigFile.getConfig().getBoolean("mentions.notify.enabled", true)) {
								if (!NametagUtil.isRenamed(p)) {
									p.sendMessage(pc.color(ConfigFile.getConfig().getString("mentions.notify.message")
											.replace("{SENDER}", event.getPlayer().getName())));
								} else {
									p.sendMessage(pc.color(ConfigFile.getConfig().getString("mentions.notify.message")
											.replace("{SENDER}", pc.nocolor(NametagUtil.getNickName(p)))));
								}

							}
						}
					}

				}
			}

			if (permissionsUtil.havePermission(event.getPlayer(), permissionsUtil.PERMISSION_CHAT_COLOR)) {
				message = (pc.color(LegacyPlaceholders.applyReplaces(event.getPlayer(), message)));
			}

			event.setMessage(LegacyPlaceholders.applyReplaces(event.getPlayer(), message));

		}
	}

	@EventHandler
	public void addTabCompleter(TabCompleteEvent event) {
		if (event.getSender() instanceof Player) {
			for (Player player : pc.getServer().getOnlinePlayers()) {

				if (NametagUtil.isRenamed(player)) {
					if (event.getCompletions().contains(player.getName())) {
						event.getCompletions().remove(player.getName());
						event.getCompletions().add(pc.nocolor(NametagUtil.getNickName(player)));
					}
				}
			}
		}
	}

	@EventHandler
	public void replaceCommnadSender(PlayerCommandPreprocessEvent event) {
		for (Player player : pc.getServer().getOnlinePlayers()) {

			if (NametagUtil.isRenamed(player)) {
				String command = event.getMessage().replace(pc.nocolor(NametagUtil.getNickName(player)),
						player.getName());
				command = command.replace(pc.nocolor(NametagUtil.getNickName(player)).toLowerCase(), player.getName());
				event.setMessage(command);
			}
		}
	}

	@EventHandler
	public void replaceCommnadSender(ServerCommandEvent event) {
		for (Player player : pc.getServer().getOnlinePlayers()) {

			if (NametagUtil.isRenamed(player)) {
				String command = event.getCommand().replace(pc.nocolor(NametagUtil.getNickName(player)),
						player.getName());
				command = command.replace(pc.nocolor(NametagUtil.getNickName(player)).toLowerCase(), player.getName());
				event.setCommand(command);
			}
		}
	}

}
