package me.nousmp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.server.ServerListPingEvent;

public class NousMPListener implements Listener {

    private final NousMP plugin;

    public NousMPListener(NousMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        if (plugin.maintenanceMode) {
            e.setMotd("§cServer under maintenance!");
            e.setMaxPlayers(0); // Simuleer full server → groene balk rood
        } else {
            e.setMotd("§aServer online!");
            e.setMaxPlayers(100); // Gewone max players
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        if (plugin.netherDisabled && e.getTo() != null && e.getTo().getWorld().getEnvironment() == org.bukkit.World.Environment.NETHER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEnderEye(PlayerInteractEvent e) {
        if (plugin.endDisabled && e.getItem() != null && e.getItem().getType() == Material.ENDER_EYE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!plugin.seasonStarted) {
            if (!e.getFrom().toVector().equals(e.getTo().toVector())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;

        if (!e.getView().getTitle().equals("§6NousMP Menu")) return;

        e.setCancelled(true);

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String name = clicked.getItemMeta().getDisplayName();

        switch (name) {
            case "§cEnd Enabled", "§aEnd Disabled" -> {
                plugin.endDisabled = !plugin.endDisabled;
                p.sendMessage("End disabled: " + plugin.endDisabled);
            }
            case "§cNether Enabled", "§aNether Disabled" -> {
                plugin.netherDisabled = !plugin.netherDisabled;
                p.sendMessage("Nether disabled: " + plugin.netherDisabled);
            }
            case "§cSeason Not Started", "§aSeason Started" -> {
                plugin.seasonStarted = !plugin.seasonStarted;
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (plugin.seasonStarted) {
                        player.teleport(player.getWorld().getSpawnLocation());
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 255, false, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
                        player.sendTitle("§6Season Started!", "§eNether in 1 week, End in 2 weeks", 10, 70, 20);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                            player.removePotionEffect(PotionEffectType.SLOW);
                        }, 120);
                    } else {
                        player.teleport(player.getWorld().getSpawnLocation());
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 255));
                        player.setExp(0);
                        player.setLevel(0);
                        player.sendTitle("§cSeason Ended!", "", 10, 70, 20);
                    }
                });
            }
        }

        NousMPGUI.open(p, plugin);
    }
}
