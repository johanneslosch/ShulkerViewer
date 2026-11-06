package de.jlosch.vanillaenhancements.client.shulker;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import de.jlosch.vanillaenhancements.client.VanillaenhancementsClient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/** Read-only view of a shulker box's contents. */
public class ShulkerPreviewScreen extends Screen {
    private static final int COLS = 9;
    private static final int ROWS = 3;
    private static final int CELL = 18;

    private final Screen parent;
    private final List<ItemStack> items;

    public ShulkerPreviewScreen(Screen parent, ItemStack shulker) {
        super(shulker.getHoverName());
        this.parent = parent;
        ItemContainerContents contents = shulker.get(DataComponents.CONTAINER);
        this.items = contents == null ? List.of() : contents.itemCopies().toList();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        int left = this.width / 2 - COLS * CELL / 2;
        int top = this.height / 2 - ROWS * CELL / 2;
        graphics.fill(left - 6, top - 20, left + COLS * CELL + 6, top + ROWS * CELL + 6, 0xE0101010);
        graphics.text(this.font, this.title, left, top - 14, 0xFFFFFFFF, false);
        ItemStack hovered = ItemStack.EMPTY;
        for (int i = 0; i < COLS * ROWS; i++) {
            int x = left + (i % COLS) * CELL;
            int y = top + (i / COLS) * CELL;
            graphics.fill(x, y, x + CELL - 1, y + CELL - 1, 0xFF373737);
            ItemStack stack = i < items.size() ? items.get(i) : ItemStack.EMPTY;
            if (!stack.isEmpty()) {
                graphics.item(stack, x + 1, y + 1);
                graphics.itemDecorations(this.font, stack, x + 1, y + 1);
                if (mouseX >= x && mouseX < x + CELL - 1 && mouseY >= y && mouseY < y + CELL - 1) {
                    hovered = stack;
                }
            }
        }
        if (!hovered.isEmpty()) {
            graphics.setTooltipForNextFrame(this.font, hovered, mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.minecraft.options.keyInventory.matches(event) || VanillaenhancementsClient.PREVIEW.matches(event)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int left = this.width / 2 - COLS * CELL / 2;
        int top = this.height / 2 - ROWS * CELL / 2;
        boolean inside = event.x() >= left - 6 && event.x() < left + COLS * CELL + 6
                && event.y() >= top - 20 && event.y() < top + ROWS * CELL + 6;
        if (!inside || event.button() == InputConstants.MOUSE_BUTTON_RIGHT) {
            this.onClose();
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }
}
