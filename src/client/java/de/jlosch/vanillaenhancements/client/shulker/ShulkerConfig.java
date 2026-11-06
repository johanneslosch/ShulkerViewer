package de.jlosch.vanillaenhancements.client.shulker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

/** Client-side settings, stored in config/vanillaenhancements.json. */
public final class ShulkerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("vanillaenhancements.json");
    private static ShulkerConfig instance = new ShulkerConfig();

    /** Share of non-empty slots (in percent) one category needs to label the box. */
    public int thresholdPercent = 60;
    public boolean enableNaming = true;
    public boolean enableTint = true;

    public static ShulkerConfig get() {
        return instance;
    }

    public static void load() {
        if (Files.exists(FILE)) {
            try {
                ShulkerConfig loaded = GSON.fromJson(Files.readString(FILE), ShulkerConfig.class);
                if (loaded != null) {
                    instance = loaded;
                    instance.thresholdPercent = Math.max(1, Math.min(100, instance.thresholdPercent));
                }
            } catch (IOException | RuntimeException ignored) {
                // keep defaults on unreadable config
            }
        }
    }

    public static void save() {
        ShulkerClassifier.clearCache();
        try {
            Files.writeString(FILE, GSON.toJson(instance));
        } catch (IOException ignored) {
        }
    }
}
