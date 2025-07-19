package de.moderation.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;

public class VeinMinerListener implements Listener {

    private final Set<Material> veinOres = Set.of(
            Material.COAL_ORE,
            Material.IRON_ORE,
            Material.COPPER_ORE,
            Material.GOLD_ORE,
            Material.REDSTONE_ORE,
            Material.LAPIS_ORE,
            Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.NETHER_QUARTZ_ORE,
            Material.NETHER_GOLD_ORE,
            Material.ANCIENT_DEBRIS,
            Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.DEEPSLATE_EMERALD_ORE
    );

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!veinOres.contains(block.getType())) return;

        if (player.isSneaking()) {
            block.breakNaturally(player.getInventory().getItemInMainHand());
            return;
        }

        Set<Block> visited = new HashSet<>();
        mineVein(block, visited, player);
    }

    private void mineVein(Block block, Set<Block> visited, Player player) {
        if (visited.contains(block)) return;
        if (!veinOres.contains(block.getType())) return;

        visited.add(block);
        block.breakNaturally(player.getInventory().getItemInMainHand());

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    Block relative = block.getRelative(x, y, z);
                    mineVein(relative, visited, player);
                }
            }
        }
    }
}