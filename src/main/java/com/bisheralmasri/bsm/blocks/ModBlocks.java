package com.bisheralmasri.bsm.blocks;

import com.bisheralmasri.bsm.BSM;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(BSM.MODID);

    public static final DeferredBlock<ControllerBlock> CONTROLLER =
            BLOCKS.register(
                    "controller",
                    () -> new ControllerBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.METAL)
                                    .strength(2.5F)
                    )
            );
}