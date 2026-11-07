package de.jlosch.shulkerviewer.client.shulker;

import net.minecraft.network.chat.Component;

public enum ShulkerCategory {
    REDSTONE(0xE03030),
    FOOD(0xE0A030),
    POTIONS(0xB040D0),
    ORES(0x40C0D0),
    ARMOR(0x6080E0),
    TOOLS(0x909090),
    BLOCKS(0x50B050);

    public final int rgb;

    ShulkerCategory(int rgb) {
        this.rgb = rgb;
    }

    public Component label() {
        return Component.translatable("shulkerviewer.category." + name().toLowerCase());
    }
}
