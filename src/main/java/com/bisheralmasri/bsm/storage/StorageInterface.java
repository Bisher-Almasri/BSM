package com.bisheralmasri.bsm.storage;

import net.minecraft.world.item.ItemStack;

public interface StorageInterface {
    long insert(ItemStack stack);

    long extract(ItemKey<?> key, long amount);

    long getStored(ItemKey<?> key);

    long getTotalStored();

    long getCapacity();
}
