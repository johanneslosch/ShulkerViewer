package de.jlosch.shulkerviewer.client.shulker;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public final class ShulkerClassifier {
    private static final Set<String> REDSTONE = Set.of(
            "redstone", "redstone_torch", "redstone_block", "redstone_lamp", "repeater", "comparator",
            "piston", "sticky_piston", "observer", "hopper", "dropper", "dispenser", "lever", "target",
            "tripwire_hook", "daylight_detector", "note_block", "tnt", "slime_block", "honey_block",
            "powered_rail", "detector_rail", "activator_rail", "sculk_sensor", "calibrated_sculk_sensor",
            "lightning_rod", "crafter", "copper_bulb", "heavy_core", "bell");
    private static final String[] ORE_PARTS = {
            "ingot", "_ore", "nugget", "raw_", "diamond", "emerald", "coal", "lapis", "amethyst_shard",
            "netherite_scrap", "quartz", "redstone_ore"};

    private static final Map<ItemContainerContents, Optional<ShulkerCategory>> CACHE =
            new LinkedHashMap<>(64, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<ItemContainerContents, Optional<ShulkerCategory>> e) {
                    return size() > 512;
                }
            };

    private ShulkerClassifier() {
    }

    public static void clearCache() {
        CACHE.clear();
    }

    public static boolean isShulker(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof ShulkerBoxBlock;
    }

    /** Dominant category of a shulker box item, or null if none reaches the threshold. */
    public static ShulkerCategory classify(ItemStack shulker) {
        if (!isShulker(shulker)) {
            return null;
        }
        ItemContainerContents contents = shulker.get(DataComponents.CONTAINER);
        if (contents == null) {
            return null;
        }
        return CACHE.computeIfAbsent(contents, ShulkerClassifier::compute).orElse(null);
    }

    private static Optional<ShulkerCategory> compute(ItemContainerContents contents) {
        EnumMap<ShulkerCategory, Integer> counts = new EnumMap<>(ShulkerCategory.class);
        int total = 0;
        for (ItemStackTemplate template : contents.nonEmptyItems()) {
            total++;
            ShulkerCategory c = categorize(template.create());
            if (c != null) {
                counts.merge(c, 1, Integer::sum);
            }
        }
        if (total == 0) {
            return Optional.empty();
        }
        double threshold = ShulkerConfig.get().thresholdPercent / 100.0;
        ShulkerCategory best = null;
        int bestCount = 0;
        for (Map.Entry<ShulkerCategory, Integer> e : counts.entrySet()) {
            if (e.getValue() > bestCount) {
                best = e.getKey();
                bestCount = e.getValue();
            }
        }
        return best != null && bestCount >= threshold * total ? Optional.of(best) : Optional.empty();
    }

    private static ShulkerCategory categorize(ItemStack s) {
        String path = BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
        if (REDSTONE.contains(path) || path.endsWith("_button") || path.endsWith("_pressure_plate")) {
            return ShulkerCategory.REDSTONE;
        }
        if (s.has(DataComponents.FOOD)) {
            return ShulkerCategory.FOOD;
        }
        if (s.has(DataComponents.POTION_CONTENTS)) {
            return ShulkerCategory.POTIONS;
        }
        for (String part : ORE_PARTS) {
            if (path.contains(part)) {
                return ShulkerCategory.ORES;
            }
        }
        if (s.has(DataComponents.EQUIPPABLE) && s.has(DataComponents.MAX_DAMAGE)) {
            return ShulkerCategory.ARMOR;
        }
        if (s.has(DataComponents.TOOL) || s.has(DataComponents.WEAPON)) {
            return ShulkerCategory.TOOLS;
        }
        if (s.getItem() instanceof BlockItem) {
            return ShulkerCategory.BLOCKS;
        }
        return null;
    }
}
