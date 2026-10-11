package com.bisheralmasri.bsm.menu;

import com.bisheralmasri.bsm.blockentity.ModBlockEntities;
import com.bisheralmasri.bsm.storage.Storage;
import com.bisheralmasri.bsm.storage.StorageItemHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class StorageMenu extends AbstractContainerMenu {
    private final StorageItemHandler itemHandler;
    private final ContainerLevelAccess access;

    private final int storageSlotCount;

    public StorageMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(
                containerId,
                playerInventory,
                new StorageItemHandler(new Storage(Long.MAX_VALUE), buf.readInt()),
                ContainerLevelAccess.NULL
        );
    }

    /** Fallback no-buf constructor (used in tests / creative menu opening). */
    public StorageMenu(int containerId, Inventory playerInventory) {
        this(
                containerId,
                playerInventory,
                new StorageItemHandler(new Storage(Long.MAX_VALUE)),
                ContainerLevelAccess.NULL
        );
    }

    public StorageMenu(
            int containerId,
            Inventory playerInventory,
            StorageItemHandler itemHandler,
            ContainerLevelAccess access
    ) {
        super(ModMenus.STORAGE.get(), containerId);

        this.itemHandler = itemHandler;
        this.access = access;

        int handlerSlots = itemHandler.getSlots();
        this.storageSlotCount = handlerSlots;

        for (int i = 0; i < handlerSlots; i++) {
            int col = i % 9;
            int row = i / 9;
            addSlot(new SlotItemHandler(itemHandler, i, 8 + col * 18, 18 + row * 18) {
                @Override
                public int getMaxStackSize() {
                    return Integer.MAX_VALUE;
                }

                @Override
                public int getMaxStackSize(@NotNull ItemStack stack) {
                    return Integer.MAX_VALUE;
                }
            });
        }

        int playerInvY = 18 + ((handlerSlots + 8) / 9) * 18 + 14;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        playerInvY + row * 18
                ));
            }
        }

        int hotbarY = playerInvY + 3 * 18 + 4;
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(
                    playerInventory,
                    col,
                    8 + col * 18,
                    hotbarY
            ));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (index < 0 || index>=slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack originalStack = sourceStack.copy();

        int playerStart = storageSlotCount;
        int playerEnd = storageSlotCount + 36;

        if (index < playerStart) {
            // storage -> player
            ItemStack transferStack = sourceStack.copy();

            if (!moveItemStackTo(transferStack, playerStart, playerEnd, true)) {
                return ItemStack.EMPTY;
            }

            // might do 64 not all, all if press space (ae2 like), but for now il keep it a is to avoid complexity
            int moved = sourceStack.getCount() - transferStack.getCount();
            if (moved <= 0) {
                return ItemStack.EMPTY;
            }

            ItemStack extracted = itemHandler.extractItem(index, moved, false);

            if (extracted.getCount() != moved) {
                throw new IllegalStateException(
                        "Storage extraction mismatch: expected: " + moved + ", got "  + extracted.getCount()
                );
            }

            sourceSlot.setChanged();
        } else {
            ItemStack remainder = itemHandler.insertItem(
                    0,
                    sourceStack,
                    false
            );

            sourceSlot.set(remainder);

            if (remainder.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }
        }

        return originalStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, ModBlockEntities.STORAGE.get().getValidBlocks().stream()
                .findFirst().orElseThrow());
    }

    public StorageItemHandler getItemHandler() {
        return itemHandler;
    }

    public int getStorageSlotCount() {
        return storageSlotCount;
    }
}
