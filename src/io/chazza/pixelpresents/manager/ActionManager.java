package io.chazza.pixelpresents.manager;

import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.api.ActionType;
import io.chazza.pixelpresents.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chazmondo
 */
public class ActionManager {

    private final Map<String, ActionType> actionTypes;
    private final PixelPresents core;

    private String nmsVer;
    public ActionManager(){
        this.actionTypes = new HashMap<>();
        this.core = (PixelPresents) JavaPlugin.getProvidingPlugin(PixelPresents.class);

        // Action Bar
        nmsVer = Bukkit.getServer().getClass().getPackage().getName();
        nmsVer = nmsVer.substring(nmsVer.lastIndexOf(".") + 1);

        if (nmsVer.equalsIgnoreCase("v1_8_R1") || nmsVer.startsWith("v1_7_")) {
        }
    }

    private ActionType getActionType(String prefix){
        for(Map.Entry<String, ActionType> actions : actionTypes.entrySet()){
            if(actions.getKey().equalsIgnoreCase(prefix))
                return actions.getValue();
        }
        return null;
    }

    public void addAction(String prefix, ActionType action){
        actionTypes.put(prefix, action);
    }

    public void removeAction(String prefix){
        actionTypes.remove(prefix);
    }

    public void execute(String reward, Player p){
        ActionType actionType = getActionType(reward.contains(" ") ? reward.split(" ", 2)[0] : reward);
        String rewardStr = reward.contains(" ") ? reward.split(" ", 2)[1] : "";

        UserManager um = new UserManager(p.getUniqueId());

        int total = core.getPresents().size();
        int found = um.getConfig().getStringList("found").size();
        int left = total - found;

        rewardStr = rewardStr.replace("%player%", p.getName())
            .replace("%found%", ""+found)
        .replace("%total%", ""+total)
        .replace("%left%", ""+left);

        rewardStr = ColorUtil.translate(rewardStr);

        if(actionType == null) return;

        switch(actionType){
            case CONSOLE_COMMAND:
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), rewardStr);
                break;

            case PLAYER_COMMAND:
                p.performCommand(rewardStr);
                break;

            case MESSAGE:
                p.sendMessage(rewardStr);
                break;

            case BROADCAST:
                Bukkit.broadcastMessage(rewardStr);
                break;

            case TITLE:
            	p.sendMessage(rewardStr);
                break;

            case ACTION_BAR:
            	p.sendMessage(rewardStr);
                break;

            case SOUND:
                p.playSound(p.getLocation(), Sound.valueOf(rewardStr), 3, 3);
                break;
        }
    }
}
