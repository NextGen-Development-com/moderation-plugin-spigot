package de.moderation.commands.Utility;

import de.moderation.ModerationPlugin;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand implements CommandExecutor, TabCompleter {

    private final String prefix;
    private final String noPermMsg;

    public EnchantCommand() {
        prefix = de.moderation.ModerationPlugin.getInstance().getConfig().getString("prefix", "§8[§cModeration§8] §r");
        noPermMsg = prefix + "§cYou do not have permission to use this command.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(prefix + "§cOnly players can use this command.");
            return true;
        }

        if (!p.hasPermission("moderation.enchant")) {
            p.sendMessage(noPermMsg);
            return true;
        }

        if (args.length != 2) {
            p.sendMessage(prefix + "§cUsage: /enchant <Enchantment> <Level>");
            return true;
        }

        ItemStack item = p.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            p.sendMessage(prefix + "§cYou must hold an item in your main hand.");
            return true;
        }

        Enchantment enchantment = Enchantment.getByName(args[0].toUpperCase());
        if (enchantment == null) {
            p.sendMessage(prefix + "§cInvalid enchantment name.");
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            p.sendMessage(prefix + "§cLevel must be a number.");
            return true;
        }

        item.addUnsafeEnchantment(enchantment, level);
        p.sendMessage(prefix + "§aEnchanted " + enchantment.getKey().getKey() + " " + level + " to your item.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            for (Enchantment ench : Enchantment.values()) {
                list.add(ench.getKey().getKey().toLowerCase());
            }
            return list;
        }
        if (args.length == 2) {
            return List.of("1", "10", "100", "255");
        }
        return List.of();
    }
}