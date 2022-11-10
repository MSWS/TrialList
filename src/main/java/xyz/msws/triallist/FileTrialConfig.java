package xyz.msws.triallist;

import org.bukkit.configuration.file.FileConfiguration;
import xyz.msws.triallist.api.TrialConfig;

public class FileTrialConfig implements TrialConfig {
    private final long duration;
    private TrialAction action;
    private final String message;

    public FileTrialConfig(FileConfiguration config) {
        this.duration = config.getLong("duration");
        try {
            this.action = TrialAction.valueOf(config.getString("action"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.action = TrialAction.MESSAGE;
        }
        this.message = config.getString("Message", "The server owner has not set a message for this action, please contact them.");
    }

    @Override
    public long getTrialDuration() {
        return duration;
    }

    @Override
    public TrialAction getTrialAction() {
        return action;
    }

    @Override
    public String getTrialMessage() {
        return message;
    }
}
