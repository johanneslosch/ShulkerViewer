package de.jlosch.vanillaenhancements.client;

import com.mojang.blaze3d.platform.InputConstants;
import de.jlosch.vanillaenhancements.client.shulker.ShulkerConfig;
import de.jlosch.vanillaenhancements.client.shulker.ShulkerSettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class VanillaenhancementsClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("vanillaenhancements", "main"));
    private static final KeyMapping OPEN_SETTINGS = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.vanillaenhancements.settings", InputConstants.KEY_K, CATEGORY));

    public static final KeyMapping PREVIEW = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.vanillaenhancements.preview", InputConstants.KEY_V, CATEGORY));

    @Override
    public void onInitializeClient() {
        ShulkerConfig.load();
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (OPEN_SETTINGS.consumeClick()) {
                mc.gui.setScreen(new ShulkerSettingsScreen(mc.gui.screen()));
            }
        });
    }
}
