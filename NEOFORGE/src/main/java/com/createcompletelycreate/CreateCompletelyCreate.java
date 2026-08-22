package com.createcompletelycreate;

import com.createcompletelycreate.components.extruder.andesite.ExtruderBlockEntity;
import com.createcompletelycreate.components.extruder.brass.BrassExtruderBlockEntity;
import com.createcompletelycreate.compat.qol.ModPaxels;
import com.createcompletelycreate.compat.qol.PaxelAbilityEvents;
import com.createcompletelycreate.foundation.register.ModRecipeRequirementTypes;
import com.createcompletelycreate.infrastructure.config.ModConfigs;
import com.createcompletelycreate.infrastructure.data.ModDataGen;
import com.createcompletelycreate.register.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

import static com.createcompletelycreate.ModConstants.MODID;

@Mod("createcompletelycreate")
public class CreateCompletelyCreate {
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(MODID).defaultCreativeTab(ModCreativeTabs.MAIN_TAB.getKey());

    static {
        REGISTRATE.setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
        );
    }

    public CreateCompletelyCreate(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        ModBlocks.register();
        ModDataComponents.register(modEventBus);
        ModBlockEntities.register();
        ModCreativeTabs.register(modEventBus);
        ModConfigs.register(modContainer);
        ModConfigs.registerConditionCodecs(modEventBus);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (minecraft, parent) -> new ConfigurationScreen(modContainer, parent));

        ModRecipes.register(modEventBus);
        ModRecipeRequirementTypes.init();

        if (ModList.get().isLoaded("createqol") && ModConfigs.startup().paxels.get()) {
            ModPaxels.register();
            PaxelAbilityEvents.register();
        }

        modEventBus.addListener(ModDataGen::gatherData);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::doClientStuff);

    }

    public void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        ExtruderBlockEntity.registerCapabilities(event);
        BrassExtruderBlockEntity.registerCapabilities(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ModPartials.init();
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
