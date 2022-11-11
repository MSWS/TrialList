package xyz.msws.triallist;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import xyz.msws.triallist.api.TrialAPI;

public class TrialCommand implements CommandExecutor {
    private TrialAPI plugin;

    public TrialCommand(TrialAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return false;
        switch (args[0].toLowerCase()) {
            case "allow": {
                if (args.length != 2) {
                    sender.sendMessage("§cUsage: /trial allow <player>");
                    return true;
                }
                OfflinePlayer player;
                if (args[0].length() > 16) {
                    player = Bukkit.getOfflinePlayer(UUID.fromString(args[0]));
                } else {
                    player = Bukkit.getOfflinePlayer(args[0]);
                }
                plugin.getTimes().put(player.getUniqueId(), -1L);
                sender.sendMessage("Added " + player.getName() + " to the permanent whitelist.");
            }
            case "deny": {
                if (args.length != 2) {
                    sender.sendMessage("§cUsage: /trial deny <player>");
                    return true;
                }
                OfflinePlayer player;
                if (args[0].length() > 16) {
                    player = Bukkit.getOfflinePlayer(UUID.fromString(args[0]));
                } else {
                    player = Bukkit.getOfflinePlayer(args[0]);
                }
                plugin.getTimes().remove(player.getUniqueId());
                sender.sendMessage("Removed " + player.getName() + " from the permanent whitelist.");
            }
            case "list": {
                sender.sendMessage("Player Trials:");
                for (UUID uuid : plugin.getTimes().keySet()) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    // Convert the milliseconds left to hours and minutes
                    long t = plugin.getTimes().get(uuid);
                    if (t == -1) {
                        sender.sendMessage(player.getName() + " - Permanent");
                        continue;
                    }
                    long time = plugin.getTrialConfig().getTrialDuration()
                            - (System.currentTimeMillis() - plugin.getTimes().get(uuid));
                    long hours = time / 3600000;
                    long minutes = (time % 3600000) / 60000;
                    if (time < 0)
                        sender.sendMessage(player.getName() + " - Expired");
                    else
                        sender.sendMessage(player.getName() + " - " + hours + ":" + minutes);
                }
            }
        }
        return false;
    }
}
