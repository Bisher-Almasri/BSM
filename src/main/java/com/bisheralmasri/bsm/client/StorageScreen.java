package com.bisheralmasri.bsm.client;

import com.bisheralmasri.bsm.menu.StorageMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class StorageScreen extends AbstractContainerScreen<StorageMenu> {
    private static final int PANEL_COLOR = 0xFF20242B;
    private static final int SLOT_COLOR = 0xFF101318;
    private static final int BORDER_COLOR = 0xFF414956;
//    private static final int ACCENT_COLOR = 0xFF55B8A6;

    private static final int SLOT_SIZE = 18;
    private static final int SLOT_COLUMNS = 9;
    private static final int SLOT_ROWS = 3;
    private static final int SLOT_PADDING = 8;

    private static final int TITLE_HEIGHT = 18;
    private static final int SECTION_SPACING = 14;
    private static final int HOTBAR_SPACING = 4;
    private static final int BOTTOM_PADDING = 7;

    private static final int TITLE_X = SLOT_PADDING;
    private static final int TITLE_Y = 5;
    private static final int INVENTORY_LABEL_OFFSET = 10;

    private static final int PANEL_WIDTH = SLOT_PADDING * 2 + SLOT_COLUMNS * SLOT_SIZE;

    private final int storageStartY = TITLE_HEIGHT;
    private final int inventoryStartY;
    private final int hotbarStartY;

    public StorageScreen(
            StorageMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(menu, playerInventory, title);

        int storageRows = (menu.getStorageSlotCount() + SLOT_COLUMNS - 1) / SLOT_COLUMNS;

        this.inventoryStartY = storageStartY + storageRows * SLOT_SIZE + SECTION_SPACING;

        this.hotbarStartY = inventoryStartY + SLOT_ROWS * SLOT_SIZE + HOTBAR_SPACING;


        this.imageWidth = PANEL_WIDTH;
        this.imageHeight = hotbarStartY
                + SLOT_SIZE
                + BOTTOM_PADDING;

        this.inventoryLabelY =
                inventoryStartY - INVENTORY_LABEL_OFFSET;
    }

    @Override
    protected void renderBg(
            @NotNull GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        int x = leftPos;
        int y = topPos;

        int right = x+imageWidth;
        int bottom = y+imageHeight;

        graphics.fill(x,y, right, bottom, PANEL_COLOR);

        graphics.fill(x, y, right, y + 1, BORDER_COLOR);
        graphics.fill(x, bottom - 1, right, bottom, BORDER_COLOR);
        graphics.fill(x, y, x + 1, bottom, BORDER_COLOR);
        graphics.fill(right - 1, y, right, bottom, BORDER_COLOR);

//        graphics.fill(
//                x+8, y+16,
//                x+imageWidth - 8, y+17,
//                ACCENT_COLOR
//        );

        // storage
        for (int i = 0; i < menu.getStorageSlotCount(); i++) {
            int col = i % SLOT_COLUMNS;
            int row = i / SLOT_COLUMNS;

            drawSlot(
                    graphics,
                    x+SLOT_PADDING + col*SLOT_SIZE,
                    y+storageStartY + row*SLOT_SIZE
            );
        }

        // inventory
        for (int row = 0; row < SLOT_ROWS; row++) {
            for (int col = 0; col < SLOT_COLUMNS; col++) {
                drawSlot(
                        graphics,
                        x + SLOT_PADDING + col * SLOT_SIZE,
                        y + inventoryStartY + row * SLOT_SIZE
                );
            }
        }

        // hotbar
        for (int col = 0; col < 9; col++) {
            drawSlot(
                    graphics,
                    x + SLOT_PADDING + col * SLOT_SIZE,
                    y + hotbarStartY
            );
        }

    }

    private void drawSlot(GuiGraphics graphics, int x, int y) {
        int slotRight = x + SLOT_SIZE;
        int slotBottom = y + SLOT_SIZE;
        graphics.fill(x, y, slotRight, slotBottom, BORDER_COLOR);
        graphics.fill(x, y, slotRight-1, slotBottom-1, SLOT_COLOR);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(
                font,
                title,
                TITLE_X,
                TITLE_Y,
                0xFFFFFFFF,
                false
        );

        graphics.drawString(
                font,
                playerInventoryTitle,
                SLOT_PADDING,
                inventoryLabelY,
                0xFFB8C2D1,
                false
        );
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}