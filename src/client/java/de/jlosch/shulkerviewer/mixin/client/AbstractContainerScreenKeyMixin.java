package de.jlosch.shulkerviewer.mixin.client;

import de.jlosch.shulkerviewer.client.ShulkerViewerClient;
import de.jlosch.shulkerviewer.client.shulker.PreviewOpener;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.inventory.Slot;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenKeyMixin {
    @Shadow
    protected @Nullable Slot hoveredSlot;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void shulkerviewer$preview(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
        if (hoveredSlot == null || self.getFocused() instanceof EditBox
                || !ShulkerViewerClient.PREVIEW.matches(event)
                || !PreviewOpener.open(self, hoveredSlot.getItem())) {
            return;
        }
        cir.setReturnValue(true);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void shulkerviewer$previewOnRightClick(MouseButtonEvent event, boolean doubleClick,
                                                         CallbackInfoReturnable<Boolean> cir) {
        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
        if (event.button() != InputConstants.MOUSE_BUTTON_RIGHT || hoveredSlot == null || !self.getMenu().getCarried().isEmpty()
                || !PreviewOpener.open(self, hoveredSlot.getItem())) {
            return;
        }
        cir.setReturnValue(true);
    }
}
