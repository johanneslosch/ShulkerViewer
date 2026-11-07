package de.jlosch.shulkerviewer.client.shulker;

import net.minecraft.client.gui.GuiGraphicsExtractor;
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
        super(Component.translatable("shulkerviewer.settings.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ShulkerConfig cfg = ShulkerConfig.get();
        int w = 200;
        int x = this.width / 2 - w / 2;
        int y = this.height / 6;

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
                .create(x, y + 28, w, 20, Component.translatable("shulkerviewer.settings.naming"),
                        (b, v) -> cfg.enableNaming = v));
        this.addRenderableWidget(CycleButton.onOffBuilder(cfg.enableTint)
                .create(x, y + 56, w, 20, Component.translatable("shulkerviewer.settings.tint"),
                        (b, v) -> cfg.enableTint = v));
        this.addRenderableWidget(new AbstractSliderButton(x, y + 84, w, 20, opacityLabel(cfg.tintOpacityPercent),
                cfg.tintOpacityPercent / 100.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(opacityLabel((int) Math.round(this.value * 100)));
            }

            @Override
            protected void applyValue() {
                cfg.tintOpacityPercent = (int) Math.round(this.value * 100);
            }
        });
        this.addRenderableWidget(CycleButton.onOffBuilder(cfg.tintOutline)
                .create(x, y + 112, w, 20, Component.translatable("shulkerviewer.settings.outline"),
                        (b, v) -> cfg.tintOutline = v));
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
                .bounds(x, y + 152, w, 20).build());
    }

    private static int toPercent(double v) {
        return 1 + (int) Math.round(v * 99);
    }

    private static Component label(int percent) {
        return Component.translatable("shulkerviewer.settings.threshold", percent);
    }

    private static Component opacityLabel(int percent) {
        return Component.translatable("shulkerviewer.settings.opacity", percent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.centeredText(this.font, this.title, this.width / 2, this.height / 6 - 16, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        ShulkerConfig.save();
        this.minecraft.gui.setScreen(this.parent);
    }
}
