package com.bisheralmasri.bsm;

import com.bisheralmasri.bsm.blockentity.ModBlockEntities;
import com.bisheralmasri.bsm.blockentity.ControllerBlockEntity;
import com.bisheralmasri.bsm.blocks.ModBlocks;
import com.bisheralmasri.bsm.client.StorageScreen;
import com.bisheralmasri.bsm.items.ModItems;
import com.bisheralmasri.bsm.menu.ModMenus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


@Mod(BSM.MODID)
public class BSM {
    public static final String MODID = "bsm";
    public static final Logger LOGGER = LogUtils.getLogger();


    public BSM(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerScreens);
        modEventBus.addListener(ControllerBlockEntity::registerCapabilities);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(
                ModConfig.Type.COMMON,
                Config.SPEC
        );
    }
    private void commonSetup(FMLCommonSetupEvent event) {
//        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.CONTROLLER);
        }
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(
                ModMenus.STORAGE.get(),
                StorageScreen::new
        );
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
