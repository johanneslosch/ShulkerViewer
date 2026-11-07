package de.jlosch.shulkerviewer.client;

import com.mojang.blaze3d.platform.InputConstants;
import de.jlosch.shulkerviewer.client.shulker.EnderChestCache;
import de.jlosch.shulkerviewer.client.shulker.ShulkerConfig;
import de.jlosch.shulkerviewer.client.shulker.ShulkerSettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class ShulkerViewerClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("shulkerviewer", "main"));
    private static final KeyMapping OPEN_SETTINGS = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.shulkerviewer.settings", InputConstants.KEY_K, CATEGORY));

    public static final KeyMapping PREVIEW = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.shulkerviewer.preview", InputConstants.KEY_V, CATEGORY));

    @Override
    public void onInitializeClient() {
        ShulkerConfig.load();
        ClientTickEvents.END_CLIENT_TICK.register(EnderChestCache::tick);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, mc) -> EnderChestCache.clear());
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (OPEN_SETTINGS.consumeClick()) {
                mc.gui.setScreen(new ShulkerSettingsScreen(mc.gui.screen()));
            }
        });
    }
}
