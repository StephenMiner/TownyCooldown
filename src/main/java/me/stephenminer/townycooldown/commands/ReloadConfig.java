package me.stephenminer.townycooldown.commands;

import com.palmergames.bukkit.towny.object.Town;
import me.stephenminer.townycooldown.TownyCooldown;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadConfig implements CommandExecutor {
    private final TownyCooldown plugin;

    public ReloadConfig(){
        this.plugin = JavaPlugin.getPlugin(TownyCooldown.class);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (!player.hasPermission("tc.commands.reload")){
                player.sendMessage(ChatColor.RED + "No permission to use this command!");
                return false;
            }
        }
        plugin.cooldownFile.reloadConfig();
        plugin.settingsFile.reloadConfig();
        plugin.cooldownMap = plugin.readStringMap();
        sender.sendMessage(ChatColor.GREEN + "Reloaded plugin files");
        return true;
    }
}
