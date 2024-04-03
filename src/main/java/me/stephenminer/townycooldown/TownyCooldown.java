package me.stephenminer.townycooldown;

import me.stephenminer.townycooldown.commands.ReloadConfig;
import me.stephenminer.townycooldown.commands.SetCooldownTime;
import me.stephenminer.townycooldown.events.TownyListeners;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class TownyCooldown extends JavaPlugin {
    public ConfigFile cooldownFile;
    public ConfigFile settingsFile;
    public HashMap<UUID, Long> cooldownMap;
    @Override
    public void onEnable() {
        this.cooldownFile = new ConfigFile(this, "cooldowns");
        this.settingsFile = new ConfigFile(this, "settings");
        this.cooldownMap = readStringMap();
        registerEvents();
        addCommands();

    }

    @Override
    public void onDisable() {
        writeMap();
    }

    /*
    old + cooldown <= current
    current - old >= cooldown
     */

    public boolean onCooldown(Player player){
        return hoursElapsed(player) < readCooldownVal();
    }

    public long hoursElapsed(Player player){
        UUID uuid = player.getUniqueId();
        if (!cooldownMap.containsKey(uuid)) return System.currentTimeMillis()/1000/60/60 + readCooldownVal();
        else{
            long old = cooldownMap.get(uuid);
            long current = System.currentTimeMillis();
            long dif = current - old;

            long hours = (((dif / 1000) / 60) / 60);
           // long hours = dif / 1000;
            return hours;
        }
    }

    public long hoursRemaining(Player player){
        return readCooldownVal() - hoursElapsed(player);
    }


    private void registerEvents(){
        this.getServer().getPluginManager().registerEvents(new TownyListeners(),this);
    }
    private void addCommands(){
        getCommand("townCooldownReload").setExecutor(new ReloadConfig());
        getCommand("townSetCooldown").setExecutor(new SetCooldownTime());
    }
    public void writeMap(){
        List<String> entries = new ArrayList<>();
        Set<Map.Entry<UUID, Long>> entrySet = cooldownMap.entrySet();
        for (Map.Entry<UUID, Long> entry : entrySet){
            entries.add(writeMapObject(entry));
        }
        this.cooldownFile.getConfig().set("cooldowns",entries);
        this.cooldownFile.saveConfig();
    }

    private String writeMapObject(Map.Entry<UUID, Long> mapEntry){
        return mapEntry.getKey() + "," + mapEntry.getValue();
    }

    private void readStringData(HashMap<UUID, Long> map, String str){
        String[] split = str.split(",");
        UUID uuid = UUID.fromString(split[0]);
        long timeLeft = Long.parseLong(split[1]);
        map.put(uuid, timeLeft);
    }

    public HashMap<UUID, Long> readStringMap(){
        HashMap<UUID, Long> cooldownMap = new HashMap<>();
        List<String> entries = this.cooldownFile.getConfig().getStringList("cooldowns");
        for (String entry : entries){
            readStringData(cooldownMap, entry);
        }
        return cooldownMap;
    }

    private int readCooldownVal(){
        return this.settingsFile.getConfig().getInt("cooldown");
    }
}
