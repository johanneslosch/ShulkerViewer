package de.jlosch.vanillaenhancements.mixin.client;

import de.jlosch.vanillaenhancements.client.shulker.ShulkerTint;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HudMixin {
    @Inject(method = "extractSlot", at = @At("TAIL"))
    private void vanillaenhancements$tintShulker(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker,
                                                 Player player, ItemStack stack, int seed, CallbackInfo ci) {
        ShulkerTint.draw(graphics, stack, x, y);
    }
}
