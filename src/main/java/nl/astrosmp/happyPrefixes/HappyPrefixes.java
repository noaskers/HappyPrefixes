package nl.astrosmp.happyPrefixes;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public final class HappyPrefixes extends JavaPlugin {

    private File dataFile;
    private FileConfiguration dataConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        createDataFile();
        getCommand("prefix").setExecutor(new PrefixCommand(this));
        getCommand("prefixreload").setExecutor(new ReloadCommand(this));

        getCommand("prefix").setTabCompleter(new PrefixTabCompleter(this));
        getCommand("prefixreload").setTabCompleter(new PrefixTabCompleter(this));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PrefixPlaceholderExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveDataFile();
    }

    private void createDataFile() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public FileConfiguration getDataConfig() {
        return dataConfig;
    }

    public void saveDataFile() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    dataConfig.save(dataFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this);
    }

    public void reloadPluginConfig() {
        reloadConfig();
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }
}