package de.jlosch.shulkerviewer.client.shulker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

/** Client-side settings, stored in config/shulkerviewer.json. */
public final class ShulkerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("shulkerviewer.json");
    private static ShulkerConfig instance = new ShulkerConfig();

    /** Share of non-empty slots (in percent) one category needs to label the box. */
    public int thresholdPercent = 60;
    public boolean enableNaming = true;
    public boolean enableTint = true;
    public int tintOpacityPercent = 44;
    public boolean tintOutline = true;

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
                    instance.tintOpacityPercent = Math.max(0, Math.min(100, instance.tintOpacityPercent));
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
