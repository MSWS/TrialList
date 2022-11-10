package xyz.msws.triallist;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.msws.triallist.api.TrialAPI;
import xyz.msws.triallist.api.TrialConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrialPlugin extends JavaPlugin implements TrialAPI {
    private final Map<UUID, Long> times = new HashMap<>();
    private File dataFile;
    private YamlConfiguration data;
    private TrialConfig config;

    @Override
    public void onEnable() {
        getCommand("trial").setExecutor(new TrialCommand());

        config = new FileTrialConfig(getConfig());
        dataFile = new File(getDataFolder(), "data.yml");
    }

    @Override
    public void onDisable() {
        data = new YamlConfiguration();
        for (Map.Entry<UUID, Long> entry : times.entrySet())
            data.set(entry.getKey().toString(), entry.getValue());
        try {
            data.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() {
        this.data = YamlConfiguration.loadConfiguration(dataFile);
        for (String key : data.getKeys(false)) {
            times.put(UUID.fromString(key), data.getLong(key));
        }
    }

    @Override
    public Map<UUID, Long> getTimes() {
        return times;
    }

    @Override
    public TrialConfig getTrialConfig() {
        return config;
    }
}
