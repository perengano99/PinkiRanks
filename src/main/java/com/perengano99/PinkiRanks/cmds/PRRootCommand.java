package com.perengano99.PinkiRanks.cmds;

import org.bukkit.command.CommandSender;

import com.perengano99.PinkiLibraries.CommandApi.SenderType;
import com.perengano99.PinkiLibraries.CommandApi.executors.PLIBSubCommandRoot;
import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.files.ConfigFile;
import com.perengano99.PinkiRanks.util.helpUtil;

public class PRRootCommand extends PLIBSubCommandRoot {

	private static PC pc = PC.p;
	
	@Override
	public boolean execute(CommandSender sender, String Label) {
		// TODO Auto-generated method stub
		
		helpUtil.sendHelpMessage(sender);
		
		return false;
	}

	@Override
	public String[] setAliases() {
		// TODO Auto-generated method stub
		return new String[] {"pr"};
	}

	@Override
	public String setDescription() {
		// TODO Auto-generated method stub
		return ConfigFile.getConfig().getString("messages.help-pr");
	}

	@Override
	public String setNoSubcommandExistMessage() {
		// TODO Auto-generated method stub
		return pc.color(ConfigFile.getConfig().getString("messages.command-no-exist"));
	}

	@Override
	public String setPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setNotPermissionMessage() {
		// TODO Auto-generated method stub
		return null;
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
