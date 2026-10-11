package com.bisheralmasri.bsm.blockentity;

import com.bisheralmasri.bsm.BSM;
import com.bisheralmasri.bsm.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    Registries.BLOCK_ENTITY_TYPE,
                    BSM.MODID
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ControllerBlockEntity>> STORAGE =
            BLOCK_ENTITIES.register(
                    "controller",
                    () -> BlockEntityType.Builder.of(
                            ControllerBlockEntity::new,
                            ModBlocks.CONTROLLER.get()
                    ).build(null)

            );
}
