package me.nousmp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NousMP extends JavaPlugin implements Listener {

    public boolean endDisabled = false;
    public boolean netherDisabled = false;
    public boolean seasonStarted = false;
    public boolean maintenanceMode = false;

    @Override
    public void onEnable() {
        getCommand("nousmp").setExecutor(new NousMPCommand(this));
        Bukkit.getPluginManager().registerEvents(new NousMPListener(this), this);
        Bukkit.getPluginManager().registerEvents(this, this);

        // Apply blindness to everyone if season not started
        if (!seasonStarted) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 1, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 255, false, false, false));
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        // Maintenance check
        if (maintenanceMode && !p.isOp()) {
            p.kickPlayer("Server is under maintenance");
            return;
        }

        // Season effects
        if (!seasonStarted) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 1, false, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 255, false, false, false));
        }
    }
}
