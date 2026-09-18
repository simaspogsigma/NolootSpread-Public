package org.simaspog.nolootSpread;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final NolootSpread plugin;

    public ReloadCommand(NolootSpread plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nolootspread.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }
        this.plugin.reloadPluginConfig();
        sender.sendMessage(ChatColor.GREEN + "NoLootSpread configuration reloaded!");
        return true;
    }
}
