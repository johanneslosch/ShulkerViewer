package de.jlosch.vanillaenhancements.client.shulker;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public final class ShulkerTint {
    private ShulkerTint() {
    }

    public static void draw(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y) {
        ShulkerConfig cfg = ShulkerConfig.get();
        if (!cfg.enableTint) {
            return;
        }
        ShulkerCategory category = ShulkerClassifier.classify(stack);
        if (category != null) {
            int alpha = Math.round(cfg.tintOpacityPercent * 255 / 100f);
            if (alpha > 0) {
                graphics.fill(x, y, x + 16, y + 16, (alpha << 24) | category.rgb);
            }
            if (cfg.tintOutline) {
                graphics.outline(x, y, 16, 16, 0xFF000000 | category.rgb);
            }
        }
    }
}
