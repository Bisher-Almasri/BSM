package com.bisheralmasri.bsm.blockentity;

import com.bisheralmasri.bsm.menu.StorageMenu;
import com.bisheralmasri.bsm.storage.Storage;
import com.bisheralmasri.bsm.storage.StorageItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

public class ControllerBlockEntity extends BlockEntity implements MenuProvider {
    private static final long DEFAULT_CAPACITY = (long) Integer.MAX_VALUE * 27;

    private final StorageItemHandler itemHandler = new StorageItemHandler(new Storage(DEFAULT_CAPACITY));

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE.get(), pos, state);
        itemHandler.setOnChanged(this::setChanged);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.STORAGE.get(),
                (be, side) -> be.itemHandler
        );
    }

    public StorageItemHandler getItemHandler() {
        return itemHandler;
    }

    public Storage getStorage() {
        return itemHandler.getStorage();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Controller");
    }

    @Override
    public AbstractContainerMenu createMenu(
            int containerId,
            @NotNull Inventory playerInventory,
            @NotNull Player player
    ) {
        assert level != null;
        return new StorageMenu(
                containerId,
                playerInventory,
                itemHandler,
                ContainerLevelAccess.create(level, worldPosition)
        );
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Storage", getStorage().save(registries));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Storage")) {
            getStorage().load(tag.getCompound("Storage"), registries);
        }
    }
}