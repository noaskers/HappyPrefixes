package nl.astrosmp.happyPrefixes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final HappyPrefixes plugin;

    public ReloadCommand(HappyPrefixes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadPluginConfig();
        sender.sendMessage("Configuration reloaded.");
        return true;
    }
}