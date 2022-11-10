package xyz.msws.triallist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.msws.triallist.api.TrialAPI;

import java.util.UUID;

public class TrialListener implements Listener {
    private final TrialAPI plugin;

    public TrialListener(TrialAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.getTimes().containsKey(uuid))
            plugin.getTimes().put(uuid, System.currentTimeMillis());
        if (plugin.getTimes().get(uuid) == -1)
            return;

        long dt = plugin.getTimes().getOrDefault(uuid, System.currentTimeMillis()) - System.currentTimeMillis();
        if (dt > plugin.getTrialConfig().getTrialDuration()) {
            plugin.getTrialConfig().execute(event);
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (allow(uuid))
                    return;
                plugin.getTrialConfig().execute(uuid);
            }
        }.runTaskLater(plugin, plugin.getTrialConfig().getTrialDuration() / 50 + 5);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (allow(event.getPlayer().getUniqueId()))
            return;
        plugin.getTrialConfig().execute(event);
    }

    private boolean allow(UUID uuid) {
        if (!plugin.getTimes().containsKey(uuid))
            plugin.getTimes().put(uuid, System.currentTimeMillis());
        if (plugin.getTimes().get(uuid) == -1)
            return false;
        long dt = plugin.getTimes().getOrDefault(uuid, System.currentTimeMillis()) - System.currentTimeMillis();
        return plugin.getTrialConfig().getTrialDuration() > dt;
    }
}
