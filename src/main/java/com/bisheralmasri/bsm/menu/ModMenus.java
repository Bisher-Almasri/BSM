package com.bisheralmasri.bsm.menu;

import com.bisheralmasri.bsm.BSM;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, BSM.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<StorageMenu>> STORAGE =
            MENUS.register(
                    "storage",
                    () -> new MenuType<>(
                            StorageMenu::new,
                            net.minecraft.world.flag.FeatureFlags.VANILLA_SET
                    )
            );
}