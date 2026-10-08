package com.bisheralmasri.bsm.menu;

import com.bisheralmasri.bsm.blockentity.ModBlockEntities;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StorageMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerLevelAccess access;

    public StorageMenu(int containerId, Inventory playerInventory) {
        this(
                containerId,
                playerInventory,
                new SimpleContainer(27),
                ContainerLevelAccess.NULL
        );
    }

    public StorageMenu(
            int containerId,
            Inventory playerInventory,
            Container container,
            ContainerLevelAccess access
    ) {
        super(ModMenus.STORAGE.get(), containerId);

        checkContainerSize(container, 27);

        this.container = container;
        this.access = access;

        container.startOpen(playerInventory.player);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(
                        container,
                        col + row * 9,
                        8 + col * 18,
                        18 + row * 18
                ));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(
                    playerInventory,
                    col,
                    8 + col * 18,
                    142
            ));
        }
    }

        @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack res = ItemStack.EMPTY;
        Slot slot = slots.get(i);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            res = stack.copy();
            if (i < 27) {
                if (!moveItemStackTo(stack, 27, 63, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, 0, 27, false)) {
                return ItemStack.EMPTY;
            }


            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return res;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlockEntities.STORAGE.get().getValidBlocks().stream()
                .findFirst().orElseThrow());
    }
}