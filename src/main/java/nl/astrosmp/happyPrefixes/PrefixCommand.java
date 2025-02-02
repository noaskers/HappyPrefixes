package nl.astrosmp.happyPrefixes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PrefixCommand implements CommandExecutor {

    private final HappyPrefixes plugin;

    public PrefixCommand(HappyPrefixes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            player.performCommand(plugin.getConfig().getString("command"));
            return true;
        }

        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("equip")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /prefix equip <prefix>");
                return true;
            }
            String prefix = args[1];
            if (!plugin.getConfig().getConfigurationSection("prefix").contains(prefix)) {
                player.sendMessage(ChatColor.RED + "Invalid prefix.");
                return true;
            }

            String permission = "happyprefixs." + prefix;
            if (!player.hasPermission(permission)) {
                player.sendMessage(plugin.getConfig().getString("messages.no-permission-message"));
                return true;
            }

            String coloredPrefix = translateHexColorCodes(ChatColor.translateAlternateColorCodes('&', prefix));
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getDataConfig().set(player.getUniqueId().toString(), coloredPrefix);
                    plugin.saveDataFile();
                }
            }.runTaskAsynchronously(plugin);
            player.sendMessage(String.format(plugin.getConfig().getString("messages.equiped"), coloredPrefix));
        } else if (subcommand.equalsIgnoreCase("unequip")) {
            String currentPrefix = plugin.getDataConfig().getString(player.getUniqueId().toString(), "");
            player.sendMessage(String.format(plugin.getConfig().getString("messages.unequiped"), currentPrefix));
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getDataConfig().set(player.getUniqueId().toString(), null);
                    plugin.saveDataFile();
                }
            }.runTaskAsynchronously(plugin);
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /prefix <equip|unequip> [prefix]");
        }
        return true;
    }

    private String translateHexColorCodes(String message) {
        return message.replaceAll("&#([A-Fa-f0-9]{6})", "§x§$1§$2§$3§$4§$5§$6");
    }
}