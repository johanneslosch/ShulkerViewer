package de.jlosch.vanillaenhancements.client.shulker;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ShulkerSettingsScreen extends Screen {
    private final Screen parent;

    public ShulkerSettingsScreen(Screen parent) {
        super(Component.translatable("vanillaenhancements.settings.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ShulkerConfig cfg = ShulkerConfig.get();
        int w = 200;
        int x = this.width / 2 - w / 2;
        int y = this.height / 4;

        this.addRenderableWidget(new AbstractSliderButton(x, y, w, 20, label(cfg.thresholdPercent),
                (cfg.thresholdPercent - 1) / 99.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(label(toPercent(this.value)));
            }

            @Override
            protected void applyValue() {
                cfg.thresholdPercent = toPercent(this.value);
            }
        });
        this.addRenderableWidget(CycleButton.onOffBuilder(cfg.enableNaming)
                .create(x, y + 28, w, 20, Component.translatable("vanillaenhancements.settings.naming"),
                        (b, v) -> cfg.enableNaming = v));
        this.addRenderableWidget(CycleButton.onOffBuilder(cfg.enableTint)
                .create(x, y + 56, w, 20, Component.translatable("vanillaenhancements.settings.tint"),
                        (b, v) -> cfg.enableTint = v));
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
                .bounds(x, y + 96, w, 20).build());
    }

    private static int toPercent(double v) {
        return 1 + (int) Math.round(v * 99);
    }

    private static Component label(int percent) {
        return Component.translatable("vanillaenhancements.settings.threshold", percent);
    }

    @Override
    public void onClose() {
        ShulkerConfig.save();
        this.minecraft.gui.setScreen(this.parent);
    }
}
