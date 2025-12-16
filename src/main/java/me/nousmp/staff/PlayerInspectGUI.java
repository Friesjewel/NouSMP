package me.nousmp.staff;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerInspectGUI {

    public static void open(Player inspector, Player target) {
        Inventory inv = Bukkit.createInventory(null, 43, "Inspect: " + target.getName());

        inv.setContents(target.getInventory().getContents());
        inspector.openInventory(inv);
    }
}
