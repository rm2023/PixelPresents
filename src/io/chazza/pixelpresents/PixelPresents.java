package io.chazza.pixelpresents;

import io.chazza.pixelpresents.api.ActionType;
import io.chazza.pixelpresents.api.Present;
import io.chazza.pixelpresents.command.*;
import io.chazza.pixelpresents.event.BreakEvent;
import io.chazza.pixelpresents.event.InteractEvent;
import io.chazza.pixelpresents.hook.PAPIHook;
import io.chazza.pixelpresents.manager.ActionManager;
import io.chazza.pixelpresents.manager.MessageManager;
import io.chazza.pixelpresents.manager.PresentManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

/**
 * Created by Chazmondo
 */
public class PixelPresents extends JavaPlugin {

    private Map<UUID, Long> playersEditing;
    private List<Present> presents;

    public Map<UUID, Long> getPlayersEditing() {
        return playersEditing;
    }

    public List<Present> getPresents() {
        return presents;
    }

    private PresentManager presentManager;
    public PresentManager getPresentManager() {
        return presentManager;
    }

    private MessageManager messageManager;

    public MessageManager getMsgManager() {
        return messageManager;
    }

    private ActionManager actionManager;

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void debug(String msg){
        getLogger().info("[DEBUG] " + msg);
    }

    @Override
    public void onEnable(){
        saveDefaultConfig();
        playersEditing = new HashMap<>();
        presents = new ArrayList<>();

        presentManager = new PresentManager();
        messageManager = new MessageManager();
        actionManager = new ActionManager();
        actionManager.addAction("[Console]", ActionType.CONSOLE_COMMAND);
        actionManager.addAction("[Player]", ActionType.PLAYER_COMMAND);
        actionManager.addAction("[Message]", ActionType.MESSAGE);
        actionManager.addAction("[Broadcast]", ActionType.BROADCAST);
        actionManager.addAction("[Title]", ActionType.TITLE);
        actionManager.addAction("[Actionbar]", ActionType.ACTION_BAR);
        actionManager.addAction("[Sound]", ActionType.SOUND);

        // Commands
        this.getCommand("presentset").setExecutor(new SetCommand(this));

        // Events
        new BreakEvent(this);
        new InteractEvent(this);

        setupPresents();

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            debug("PlaceholderAPI has been found!");
            new PAPIHook(this).hook();
        }
    }

    @Override
    public void onDisable(){
        saveConfig();
    }

    public void setupPresents() {

        presents.clear();
        Location presentLoc;
        Present presentObj;

        if (getConfig().getConfigurationSection("present") != null) {
            for (String present : getConfig().getConfigurationSection("present").getKeys(false)) {
                presentLoc = new Location(Bukkit.getWorld(getConfig().getString("present." + present + ".location.world")),
                    getConfig().getInt("present." + present + ".location.x"),
                    getConfig().getInt("present." + present + ".location.y"),
                    getConfig().getInt("present." + present + ".location.z"));

                presentObj = new Present(UUID.fromString(present), presentLoc);
                presentObj.withRewards(getConfig().getStringList("present." + present + ".reward"))
                    .build();
            }
        }
    }
}
