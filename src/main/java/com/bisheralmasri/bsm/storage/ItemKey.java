package com.bisheralmasri.bsm.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public record ItemKey<T>(T value, DataComponentMap components) {
    public ItemKey {
        Objects.requireNonNull(value, "Registry value cannot be null");
        Objects.requireNonNull(components, "Components map cannot be null");
    }

    public static ItemKey<Item> of(ItemStack stack) {
        return new ItemKey<>(stack.getItem(), stack.getComponents());
    }

    public ItemStack toStack(int count) {
        if (this.value instanceof Item itemType) {
            ItemStack stack = new ItemStack(itemType, count);
            stack.applyComponents(this.components);
            return stack;
        }
        throw new IllegalStateException("Cannot convert non-Item key type to an ItemStack!");
    }

    // least cursed java code
    public static <T> Codec<ItemKey<T>> codec(Codec<T> valueCodec) {
        return RecordCodecBuilder.create(instance ->
                instance.group(
                        valueCodec.fieldOf("value").forGetter(ItemKey::value),
                        DataComponentMap.CODEC.fieldOf("components").forGetter(ItemKey::components)
                ).apply(instance, ItemKey::new));
    }


}