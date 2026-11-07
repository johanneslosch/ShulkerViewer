package de.jlosch.shulkerviewer.client.shulker;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;

/**
 * The client only learns the ender chest contents while its screen is open, so the last
 * seen contents are cached (per session, cleared when joining a world).
 */
public final class EnderChestCache {
    private static List<ItemStack> contents;

    private EnderChestCache() {
    }

    public static List<ItemStack> get() {
        return contents;
    }

    public static void clear() {
        contents = null;
    }

    public static void tick(Minecraft mc) {
        if (mc.gui.screen() instanceof ContainerScreen screen
                && screen.getTitle().getContents() instanceof TranslatableContents tc
                && tc.getKey().equals("container.enderchest")) {
            ChestMenu menu = screen.getMenu();
            List<ItemStack> copy = new ArrayList<>();
            for (int i = 0; i < menu.getRowCount() * 9; i++) {
                copy.add(menu.getContainer().getItem(i).copy());
            }
            contents = copy;
        }
    }
}
