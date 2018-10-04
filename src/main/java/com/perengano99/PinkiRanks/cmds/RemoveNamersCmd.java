package com.perengano99.PinkiRanks.cmds;

import org.bukkit.command.CommandSender;

import com.perengano99.PinkiLibraries.CommandApi.SenderType;
import com.perengano99.PinkiLibraries.CommandApi.executors.PLIBSubCommand;
import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.ConfigFile;
import com.perengano99.PinkiRanks.nametag.NametagUtil;
import com.perengano99.PinkiRanks.util.permissionsUtil;

public class RemoveNamersCmd extends PLIBSubCommand {

	private static PC pc = PC.p;
	
	public RemoveNamersCmd(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		sender.sendMessage(pc.color(ConfigFile.getConfig().getString("messages.remove-namers")));
		NametagUtil.clearNameTagNamers();
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
		return permissionsUtil.getPermission(permissionsUtil.PERMISSION_REMOVE_NAMERS);
	}

	@Override
	public String setNotPermissionMessage() {
		// TODO Auto-generated method stub
		return pc.color(ConfigFile.getConfig().getString("messages.no-permission"));
	}

	@Override
	public String setFewArgumentsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setTooManyArgumentsMessage() {
		// TODO Auto-generated method stub
		return pc.color(ConfigFile.getConfig().getString("messages.too-many-arguments"));
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
