package nl.astrosmp.happyPrefixes;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;

public class PrefixPlaceholderExpansion extends PlaceholderExpansion {

    private final HappyPrefixes plugin;
    private final ConcurrentHashMap<String, String> prefixCache = new ConcurrentHashMap<>();

    public PrefixPlaceholderExpansion(HappyPrefixes plugin) {
        this.plugin = plugin;
        startUpdatingPrefixes();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "happyprefix";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("prefix")) {
            return prefixCache.getOrDefault(player.getUniqueId().toString(), "false");
        }

        return null;
    }

    private void startUpdatingPrefixes() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    String prefixKey = plugin.getDataConfig().getString(player.getUniqueId().toString(), plugin.getConfig().getString("no-prefix"));
                    String prefixValue = plugin.getConfig().getString("prefix." + prefixKey, "");
                    prefixCache.put(player.getUniqueId().toString(), translateHexColorCodes(ChatColor.translateAlternateColorCodes('&', prefixValue)));
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L); // Update every second (20 ticks)
    }

    private String translateHexColorCodes(String message) {
        return message.replaceAll("&#([A-Fa-f0-9]{6})", "§x§$1§$2§$3§$4§$5§$6");
    }
}