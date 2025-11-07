package com.zevent;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

public class LifesPlugin extends JavaPlugin {

    private File dataFile;
    private FileConfiguration data;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        getLogger().info("⚡ ZeventLifes activado!");

        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        data = YamlConfiguration.loadConfiguration(dataFile);

        scoreboardManager = new ScoreboardManager(this);
        getServer().getPluginManager().registerEvents(new LifesListener(this), this);
        getCommand("vidas").setExecutor(new LivesCommand(this));

        getServer().getOnlinePlayers().forEach(scoreboardManager::createScoreboard);
    }

    @Override
    public void onDisable() {
        saveData();
    }

    public int getLives(UUID uuid) {
        return data.getInt(uuid.toString(), 3);
    }

    public void setLives(UUID uuid, int lives) {
        data.set(uuid.toString(), lives);
        saveData();
    }

    public void saveData() {
        try {
            data.save(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
