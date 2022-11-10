package xyz.msws.triallist.api;

import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public interface TrialAPI extends Plugin {
    Map<UUID, Long> getTimes();
    TrialConfig getTrialConfig();
}
