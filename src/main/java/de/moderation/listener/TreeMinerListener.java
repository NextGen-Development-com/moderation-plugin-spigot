package de.moderation.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class TreeMinerListener implements Listener {

    private static final int MAX_BLOCKS = 100;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!isAxe(item.getType())) return;

        Block start = event.getBlock();
        Material type = start.getType();

        if (isLog(type)) {
            event.setCancelled(true);
            breakTree(start, player, item);
        }
    }

    private boolean isAxe(Material mat) {
        return mat == Material.WOODEN_AXE
                || mat == Material.STONE_AXE
                || mat == Material.IRON_AXE
                || mat == Material.GOLDEN_AXE
                || mat == Material.DIAMOND_AXE
                || mat == Material.NETHERITE_AXE;
    }

    private boolean isLog(Material mat) {
        return mat == Material.OAK_LOG || mat == Material.BIRCH_LOG || mat == Material.SPRUCE_LOG
                || mat == Material.JUNGLE_LOG || mat == Material.ACACIA_LOG || mat == Material.DARK_OAK_LOG;
    }

    private boolean isLeaf(Material mat) {
        return mat == Material.OAK_LEAVES || mat == Material.BIRCH_LEAVES || mat == Material.SPRUCE_LEAVES
                || mat == Material.JUNGLE_LEAVES || mat == Material.ACACIA_LEAVES || mat == Material.DARK_OAK_LEAVES;
    }

    private void breakTree(Block start, Player player, ItemStack axe) {
        Set<Block> toBreak = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty() && toBreak.size() < MAX_BLOCKS) {
            Block current = queue.poll();
            if (toBreak.contains(current)) continue;

            Material mat = current.getType();
            if (isLog(mat) || isLeaf(mat)) {
                toBreak.add(current);

                for (BlockFace face : BlockFace.values()) {
                    Block relative = current.getRelative(face);
                    if (!toBreak.contains(relative) && relative.getWorld().equals(current.getWorld())) {
                        queue.add(relative);
                    }
                }
            }
        }

        for (Block b : toBreak) {
            b.breakNaturally(axe);
        }

        if (axe.getItemMeta() instanceof Damageable damageable) {
            int durability = damageable.getDamage();
            durability += toBreak.size();
            damageable.setDamage(durability);
            axe.setItemMeta(damageable);
            if (durability >= axe.getType().getMaxDurability()) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage("Your axe broke!");
            }
        }
    }
}