package me.stephenminer.townycooldown.commands;

import me.stephenminer.townycooldown.TownyCooldown;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetCooldownTime implements CommandExecutor {
    private final TownyCooldown plugin;

    public SetCooldownTime(){
        this.plugin = JavaPlugin.getPlugin(TownyCooldown.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (!player.hasPermission("tc.commands.settime")){
                player.sendMessage(ChatColor.RED + "No permission for this command!");
                return false;
            }
        }
        if (args.length < 1){
            sender.sendMessage(ChatColor.RED + "You need to specify how many hours you want the cooldown to be.");
            return false;
        }
        int hours;
        try {
            hours = Integer.parseInt(args[0]);
        }catch (Exception e){
            sender.sendMessage(ChatColor.RED + args[0] + " needs to be a whole number!");
            return false;
        }
        writeHours(hours);
        return true;
    }

    private void writeHours(int hours){
        plugin.settingsFile.getConfig().set("cooldown",hours);
        plugin.settingsFile.saveConfig();
    }
}
