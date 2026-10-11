package com.bisheralmasri.bsm.storage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;

// java needs typedef, im tired of writing Map.Entry<ItemKey<?>, Long> over and over and over

public class StorageItemHandler implements IItemHandlerModifiable {
    public final Storage storage;
    private final int fixedSlots;
    private Runnable onChanged = () -> {};

    public StorageItemHandler(Storage storage, int fixedSlots) {
        this.storage = storage;
        this.fixedSlots = fixedSlots;
    }

    public StorageItemHandler(Storage storage) {
        this(storage, -1);
    }

    public void setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    private List<Map.Entry<ItemKey<?>, Long>> entries() {
        return storage.getEntries();
    }

    @Override
    public int getSlots() {
        if (fixedSlots != -1) {
            return fixedSlots;
        }

        return (entries().size() / 9 + 1) * 9;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        List<Map.Entry<ItemKey<?>, Long>> snapshot = entries();
        if (slot < 0 || slot >= snapshot.size()) return ItemStack.EMPTY;

        Map.Entry<ItemKey<?>, Long> entry = snapshot.get(slot);
        int displayCount = (int) Math.min(entry.getValue(), Integer.MAX_VALUE);
        return entry.getKey().toStack(displayCount);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        List<Map.Entry<ItemKey<?>, Long>> snapshot = entries();
        if (slot >= 0 && slot < snapshot.size()) {
            @SuppressWarnings("unchecked")
            ItemKey<Item> key = (ItemKey<Item>) snapshot.get(slot).getKey();
            storage.extract(key, Long.MAX_VALUE, false);
        }
        if (!stack.isEmpty()) {
            storage.insert(stack, false);
        }
        onChanged.run();
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        long inserted = storage.insert(stack, simulate);
        int remainder = (int) (stack.getCount() - inserted);

        if (!simulate && inserted > 0) onChanged.run();

        if (remainder <= 0) return ItemStack.EMPTY;
        ItemStack leftover = stack.copy();
        leftover.setCount(remainder);
        return leftover;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0) return ItemStack.EMPTY;

        List<Map.Entry<ItemKey<?>, Long>> snapshot = entries();
        if (slot < 0 || slot >= snapshot.size()) return ItemStack.EMPTY;

        @SuppressWarnings("unchecked")
        ItemKey<Item> key = (ItemKey<Item>) snapshot.get(slot).getKey();

        long extracted = storage.extract(key, amount, simulate);
        if (extracted <= 0) return ItemStack.EMPTY;

        if (!simulate) onChanged.run();

        return key.toStack((int) extracted);
    }

    @Override
    public int getSlotLimit(int slot) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    public Storage getStorage() {
        return storage;
    }
}
