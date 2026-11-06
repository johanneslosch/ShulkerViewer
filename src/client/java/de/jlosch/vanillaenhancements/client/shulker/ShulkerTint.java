package de.jlosch.vanillaenhancements.client.shulker;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public final class ShulkerTint {
    private ShulkerTint() {
    }

    public static void draw(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y) {
        if (!ShulkerConfig.get().enableTint) {
            return;
        }
        ShulkerCategory category = ShulkerClassifier.classify(stack);
        if (category != null) {
            graphics.fill(x, y, x + 16, y + 16, 0x70000000 | category.rgb);
            graphics.outline(x, y, 16, 16, 0xFF000000 | category.rgb);
        }
    }
}
