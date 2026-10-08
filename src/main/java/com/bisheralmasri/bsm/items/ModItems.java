package com.bisheralmasri.bsm.items;

import com.bisheralmasri.bsm.BSM;
import com.bisheralmasri.bsm.blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(BSM.MODID);

    public static final DeferredItem<BlockItem> STORAGE =
            ITEMS.registerSimpleBlockItem(
                    "storage",
                    ModBlocks.STORAGE
            );
}