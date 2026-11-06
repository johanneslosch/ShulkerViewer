package de.jlosch.vanillaenhancements.client.shulker;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PreviewOpener {
    private PreviewOpener() {
    }

    /** Opens a preview for the stack if it is previewable; returns whether it did. */
    public static boolean open(Screen parent, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        if (ShulkerClassifier.isShulker(stack)) {
            mc.gui.setScreen(new ShulkerPreviewScreen(parent, stack));
            return true;
        }
        if (!stack.isEmpty() && stack.getItem() == Items.ENDER_CHEST) {
            List<ItemStack> cached = EnderChestCache.get();
            Component hint = cached == null ? Component.translatable("vanillaenhancements.enderchest.unknown") : null;
            mc.gui.setScreen(new ShulkerPreviewScreen(parent, stack.getHoverName(),
                    cached == null ? List.of() : cached, hint));
            return true;
        }
        return false;
    }
}
