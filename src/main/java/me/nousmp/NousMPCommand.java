package me.nousmp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nousmp.staff.*;

public class NousMPCommand implements CommandExecutor {

    private final NousMP plugin;
    private final StaffMode staffMode;

    public NousMPCommand(NousMP plugin) {
        this.plugin = plugin;
        this.staffMode = new StaffMode();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        if (args.length == 0) {
            NousMPGUI.open(p, plugin);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {

            case "report":
                if (args.length < 3) {
                    p.sendMessage("Usage: /report <player> <reason>");
                    return true;
                }
                Player targetReport = Bukkit.getPlayer(args[1]);
                if (targetReport != null) {
                    Bukkit.getOnlinePlayers().forEach(pl -> {
                        if (pl.hasPermission("nousmp.staff")) {
                            pl.sendMessage("§eReport: " + targetReport.getName() + " Reason: " + args[2]);
                        }
                    });
                    p.sendMessage("§aReport sent to staff!");
                } else p.sendMessage("Player not found!");
                break;

            case "staffmode":
                staffMode.toggleStaffMode(p);
                break;

            case "vanish":
                staffMode.toggleVanish(p);
                break;

            case "inspect":
                if (args.length < 2) {
                    p.sendMessage("Usage: /inspect <player>");
                    return true;
                }
                Player targetInspect = Bukkit.getPlayer(args[1]);
                if (targetInspect != null) {
                    PlayerInspectGUI.open(p, targetInspect);
                } else p.sendMessage("Player not found!");
                break;

            default:
                p.sendMessage("Unknown subcommand.");
        }

        return true;
    }
}
