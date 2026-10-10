package com.bisheralmasri.bsm.storage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Storage implements StorageInterface {
    private final Map<ItemKey<?>, Long> storedItems = new LinkedHashMap<>();
    private final long capacity;
    private long totalStored = 0L;

    public Storage(long capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }

    @Override
    public long insert(ItemStack stack) {
        return insert(stack, false);
    }

    public long insert(ItemStack stack, boolean simulate) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }

        ItemKey<?> key = ItemKey.of(stack);
        long amountToAdd = stack.getCount();
        if (amountToAdd <= 0) return 0;

        long remainingCapacity = capacity - totalStored;
        if (remainingCapacity <= 0) {
            return 0;
        }
        long toAdd = Math.min(amountToAdd, remainingCapacity);

        if (!simulate) {
            storedItems.put(key, storedItems.getOrDefault(key, 0L) + toAdd);
            totalStored += toAdd;
        }
        return toAdd;
    }

    @Override
    public long extract(ItemKey<?> key, long amount) {
        return extract(key, amount, false);
    }

    public long extract(ItemKey<?> key, long amount, boolean simulate) {
        if (amount <= 0 || key == null) return 0;

        long currentlyStored = getStored(key);
        long toTake = Math.min(currentlyStored, amount);

        if (!simulate && toTake > 0) {
            long newAmount = currentlyStored - toTake;
            if (newAmount == 0) {
                storedItems.remove(key);
            } else {
                storedItems.put(key, newAmount);
            }
            totalStored -= toTake;
        }

        return toTake;
    }

    @Override
    public long getStored(ItemKey<?> key) {
        return this.storedItems.getOrDefault(key, 0L);
    }

    @Override
    public long getTotalStored() {
        return totalStored;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    public List<Map.Entry<ItemKey<?>, Long>> getEntries() {
        return new ArrayList<>(storedItems.entrySet());
    }

    public CompoundTag save(HolderLookup.Provider registries) {
        CompoundTag rootTag = new CompoundTag();
        ListTag listTag = new ListTag();

        for (Map.Entry<ItemKey<?>, Long> entry : storedItems.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            ItemStack dummyStack = entry.getKey().toStack(1);

            CompoundTag itemTag = (CompoundTag) dummyStack.save(registries);
            entryTag.put("Item", itemTag);
            entryTag.putLong("Amount", entry.getValue());
            listTag.add(entryTag);
        }

        rootTag.put("StoredItems", listTag);
        return rootTag;
    }


    public void load(CompoundTag rootTag, HolderLookup.Provider registries) {
        this.storedItems.clear();
        this.totalStored = 0L;

        if (!rootTag.contains("StoredItems", net.minecraft.nbt.Tag.TAG_LIST)) {
            return;
        }

        ListTag listTag = rootTag.getList("StoredItems", net.minecraft.nbt.Tag.TAG_COMPOUND);

        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag entryTag = listTag.getCompound(i);

            if (entryTag.contains("Item", net.minecraft.nbt.Tag.TAG_COMPOUND) && entryTag.contains("Amount")) {
                CompoundTag itemTag = entryTag.getCompound("Item");
                long amount = entryTag.getLong("Amount");

                java.util.Optional<ItemStack> parsedStack = ItemStack.parse(registries, itemTag);

                if (parsedStack.isPresent() && !parsedStack.get().isEmpty()) {
                    ItemKey<?> key = ItemKey.of(parsedStack.get());

                    this.storedItems.put(key, this.storedItems.getOrDefault(key, 0L) + amount);
                    this.totalStored += amount;
                }
            }
        }
    }
}