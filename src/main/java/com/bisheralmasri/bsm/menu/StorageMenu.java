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
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (!slot.hasItem()) return result;
        ItemStack stack = slot.getItem();
        result = stack.copy();

        int playerStart = storageSlotCount;
        int playerEnd = storageSlotCount + 36;
        if (index < storageSlotCount) {
            if (!moveItemStackTo(stack, playerStart, playerEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!moveItemStackTo(stack, 0, storageSlotCount, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
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
