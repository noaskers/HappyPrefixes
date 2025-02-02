package nl.astrosmp.happyPrefixes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class PrefixTabCompleter implements TabCompleter {

    private final HappyPrefixes plugin;

    public PrefixTabCompleter(HappyPrefixes plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("equip");
            completions.add("unequip");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("equip")) {
            completions.addAll(plugin.getConfig().getConfigurationSection("prefix").getKeys(false));
        }

        return completions;
    }
}