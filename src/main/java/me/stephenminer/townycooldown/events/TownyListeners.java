package me.stephenminer.townycooldown.events;

import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownPreAddResidentEvent;
import me.stephenminer.townycooldown.TownyCooldown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TownyListeners implements Listener {
    private final TownyCooldown plugin;
    public TownyListeners(){
        this.plugin = JavaPlugin.getPlugin(TownyCooldown.class);
    }
    //Event doesn't even call
    /*
    @EventHandler
    public void townPreJoin(TownPreAddResidentEvent event){
        Player player = event.getResident().getPlayer();
        System.out.println(2);
        if (player != null && plugin.onCooldown(player)){
            System.out.println(4);
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You have " + plugin.hoursRemaining(player) + " hours remaining until you can join another town");
        }
    }

     */

    @EventHandler
    public void callJoinCmd(PlayerCommandPreprocessEvent event){
        String msg = event.getMessage();
        int index = msg.indexOf(" ");
        System.out.println(msg);
        if (index > 0) msg = msg.substring(0, index);
        if (msg.equalsIgnoreCase("/accept") || msg.equalsIgnoreCase("/towny:accept")){
            Player player = event.getPlayer();
            if (plugin.onCooldown(player)){
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot join a new town! You have " + plugin.hoursRemaining(player) + " hours remaining until you can join another town");
            }
        }
    }
    @EventHandler
    public void townJoin(TownAddResidentEvent event){
        Player player = event.getResident().getPlayer();
        if (player == null) return;
        if (!plugin.onCooldown(player)){
            player.sendMessage(ChatColor.YELLOW + "Be warned, you will not be able to leave this town for 24 hours...");
            plugin.cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
            plugin.writeMap();
        }//else{
            //Unnecessary code, it is handled by CommandPreProcessEvent. If it is needed, something broke
            /*
            Bukkit.getScheduler().runTaskLater(plugin, ()->Bukkit.dispatchCommand(player,"town leave"), 10);
            Bukkit.getScheduler().runTaskLater(plugin,()->{
                try {
                    Bukkit.dispatchCommand(player,"confirm");
                    event.getTown().removeResident(event.getResident());
                    player.sendMessage(ChatColor.RED + "You cannot join a new town! You have " + plugin.hoursRemaining(player) + " hours remaining until you can join another town");
                }catch (Exception e){
                    plugin.getLogger().info("-------------");
                    plugin.getLogger().info("May be important:");
                    e.printStackTrace();
                    plugin.getLogger().info("-------------");
                }
            }, 15);

             */
       // }
    }
}
