package xyz.msws.triallist.api;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public interface TrialConfig {
    /**
     * Milliseconds
     *
     * @return
     */
    long getTrialDuration();

    TrialAction getTrialAction();

    String getTrialMessage();

    default void execute(Player player) {
        String value = ChatColor.translateAlternateColorCodes('&', getTrialMessage());
        value = value.replace("%player%", player.getName()).replace("%uuid%", player.getUniqueId().toString());
        switch (getTrialAction()) {
            case KICK -> player.kickPlayer(value);
            case MESSAGE -> player.sendMessage(value);
            case COMMAND -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
        }
    }

    default void execute(PlayerLoginEvent event) {
        String value = ChatColor.translateAlternateColorCodes('&', getTrialMessage());
        value = value.replace("%player%", event.getPlayer().getName()).replace("%uuid%", event.getPlayer().getUniqueId().toString());
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, value);
    }

    default void execute(PlayerJoinEvent event) {
        if (getTrialAction() == TrialAction.KICK) { // This shouldn't happen, but just in case
            System.out.println("[WARN] Called execute(PlayerJoinEvent) but action is KICK");
            return;
        }
        this.execute(event.getPlayer());
    }

    default void execute(UUID uuid) {
        execute(Bukkit.getPlayer(uuid));
    }

    enum TrialAction {
        MESSAGE,
        COMMAND,
        KICK;
    }
}

