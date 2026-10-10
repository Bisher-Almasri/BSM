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
    private static final int ACCENT_COLOR = 0xFF55B8A6;

    private final int storageRows;

    public StorageScreen(
            StorageMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(menu, playerInventory, title);

        this.storageRows = (menu.getStorageSlotCount() + 8) / 9;


        this.imageWidth = 176;
        this.imageHeight = 18 + storageRows * 18 + 18 + 3 * 18 + 4 + 18 + 7;

        int playerInvY = 18 + storageRows * 18 + 14;
        this.inventoryLabelY = playerInvY - 10;
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

        graphics.fill(
                x,y,
                x+imageWidth, y+imageHeight,
                PANEL_COLOR
        );

        graphics.fill(x, y, x + imageWidth, y + 1, BORDER_COLOR);
        graphics.fill(x, y + imageHeight - 1, x + imageWidth, y + imageHeight, BORDER_COLOR);
        graphics.fill(x, y, x + 1, y + imageHeight, BORDER_COLOR);
        graphics.fill(x + imageWidth - 1, y, x + imageWidth, y + imageHeight, BORDER_COLOR);

        graphics.fill(
                x+8, y+16,
                x+imageWidth - 8, y+17,
                ACCENT_COLOR
        );

        for (int i = 0; i < menu.getStorageSlotCount(); i++) {
            int col = i % 9;
            int row = i / 9;

            drawSlot(
                    graphics,
                    x+8 + col*18,
                    y+18 + row*18
            );
        }

        int playerInvY = 18 + storageRows * 18 + 14;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlot(
                        graphics,
                        x + 8 + col * 18,
                        y + playerInvY + row * 18
                );
            }
        }

        int hotbarY = playerInvY + 3 * 18 + 4;

        for (int col = 0; col < 9; col++) {
            drawSlot(
                    graphics,
                    x + 8 + col * 18,
                    y + hotbarY
            );
        }

    }

    private void drawSlot(GuiGraphics graphics, int x, int y) {
        graphics.fill(x, y, x+18, y+18, BORDER_COLOR);
        graphics.fill(x, y, x+17, y+17, SLOT_COLOR);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(
                font,
                title,
                8,
                5,
                0xFFFFFFFF,
                false
        );

        graphics.drawString(
                font,
                playerInventoryTitle,
                8,
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