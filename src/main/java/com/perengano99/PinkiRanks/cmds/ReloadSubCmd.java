package com.perengano99.PinkiRanks.cmds;

import org.bukkit.command.CommandSender;

import com.perengano99.PinkiLibraries.CommandApi.SenderType;
import com.perengano99.PinkiLibraries.CommandApi.executors.PLIBSubCommand;
import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.ConfigFile;
import com.perengano99.PinkiRanks.files.FileManager;
import com.perengano99.PinkiRanks.listener.Animations;
import com.perengano99.PinkiRanks.listener.Emojis;
import com.perengano99.PinkiRanks.listener.Rank;
import com.perengano99.PinkiRanks.util.permissionsUtil;
import com.perengano99.PinkiRanks.util.updaterUtil;

public class ReloadSubCmd extends PLIBSubCommand {

	public ReloadSubCmd(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		updaterUtil.stop();
		updaterUtil.removeCancel();
		FileManager.loadFiles();
		Rank.reload();
		Animations.reload();
		Emojis.reload();
		updaterUtil.update();
		sender.sendMessage(PC.p.color(ConfigFile.getConfig().getString("messages.reload")));

		return true;
	}

	@Override
	public int setMaxArgs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setMinArgs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String setPermission() {
		// TODO Auto-generated method stub
		return permissionsUtil.getPermission(permissionsUtil.PERMISSION_RELOAD);
	}

	@Override
	public String setNotPermissionMessage() {
		// TODO Auto-generated method stub
		return PC.p.color(ConfigFile.getConfig().getString("messages.no-permissions"));
	}

	@Override
	public String setFewArgumentsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setTooManyArgumentsMessage() {
		// TODO Auto-generated method stub
		return PC.p.color(ConfigFile.getConfig().getString("messages.too-many-arguments"));
	}

	@Override
	public SenderType onlyFor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setOnlyForMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
