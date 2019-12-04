package io.chazza.pixelpresents.command;

import io.chazza.pixelpresents.PixelPresents;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Chazmondo
 */
public class SetCommand implements CommandExecutor {

    private final PixelPresents core;
    public SetCommand(PixelPresents core){
        this.core = core;
    }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
	        if(sender.hasPermission("pixelpresents.admin")) {
	            sender.sendMessage(core.getMsgManager().getMessage("create"));
	            core.getPlayersEditing().put(p.getUniqueId(), System.currentTimeMillis());
	            return true;
	        } else {
	            sender.sendMessage(core.getMsgManager().getMessage("permission"));
	            return false;
	        }
		}
		return false;
	}
}
