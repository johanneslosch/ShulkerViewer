package de.jlosch.shulkerviewer.mixin.client;

import de.jlosch.shulkerviewer.client.shulker.ShulkerCategory;
import de.jlosch.shulkerviewer.client.shulker.ShulkerClassifier;
import de.jlosch.shulkerviewer.client.shulker.ShulkerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "getHoverName", at = @At("RETURN"), cancellable = true)
    private void shulkerviewer$nameShulker(CallbackInfoReturnable<Component> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (!ShulkerConfig.get().enableNaming || self.getCustomName() != null) {
            return;
        }
        ShulkerCategory category = ShulkerClassifier.classify(self);
        if (category != null) {
            cir.setReturnValue(Component.empty().append(cir.getReturnValue()).append(" (").append(category.label()).append(")"));
        }
    }
}
