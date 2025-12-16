package me.nousmp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NousMPGUI {

    public static void open(Player p, NousMP plugin) {
        Inventory inv = Bukkit.createInventory(null, 9, "§6NousMP Menu");

        ItemStack endItem = new ItemStack(Material.ENDER_EYE);
        ItemMeta endMeta = endItem.getItemMeta();
        endMeta.setDisplayName(plugin.endDisabled ? "§aEnd Disabled" : "§cEnd Enabled");
        endItem.setItemMeta(endMeta);
        inv.setItem(0, endItem);

        ItemStack netherItem = new ItemStack(Material.OBSIDIAN);
        ItemMeta netherMeta = netherItem.getItemMeta();
        netherMeta.setDisplayName(plugin.netherDisabled ? "§aNether Disabled" : "§cNether Enabled");
        netherItem.setItemMeta(netherMeta);
        inv.setItem(1, netherItem);

        ItemStack seasonItem = new ItemStack(Material.CLOCK);
        ItemMeta seasonMeta = seasonItem.getItemMeta();
        seasonMeta.setDisplayName(plugin.seasonStarted ? "§aSeason Started" : "§cSeason Not Started");
        seasonItem.setItemMeta(seasonMeta);
        inv.setItem(2, seasonItem);

        p.openInventory(inv);
    }
}
