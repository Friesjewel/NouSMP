package me.nousmp.staff;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import java.util.HashMap;
import java.util.Map;

public class StaffMode {

    private final Map<Player, ItemStack[]> savedInventory = new HashMap<>();
    private final Map<Player, PotionEffect[]> savedEffects = new HashMap<>();
    private final Map<Player, Boolean> vanished = new HashMap<>();

    public void toggleStaffMode(Player p) {
        if (savedInventory.containsKey(p)) {
            // Restore inventory and effects
            p.getInventory().setContents(savedInventory.get(p));
            p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
            for (PotionEffect effect : savedEffects.get(p)) p.addPotionEffect(effect);
            savedInventory.remove(p);
            savedEffects.remove(p);
        } else {
            // Save inventory and effects
            savedInventory.put(p, p.getInventory().getContents());
            savedEffects.put(p, p.getActivePotionEffects().toArray(new PotionEffect[0]));

            p.getInventory().clear();
            p.sendMessage("§aStaff Mode enabled: you now have investigative tools.");
        }
    }

    public void toggleVanish(Player p) {
        boolean nowVanished = !vanished.getOrDefault(p, false);
        vanished.put(p, nowVanished);

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.hasPermission("nousmp.staff")) {
                if (nowVanished) other.hidePlayer(p);
                else other.showPlayer(p);
            }
        }

        p.sendMessage("§6Vanish " + (nowVanished ? "enabled" : "disabled"));
    }
}
