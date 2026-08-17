package com.createcompletelycreate.infrastructure.config;

import com.createcompletelycreate.ModConstants;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.api.stress.BlockStressValues;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, ModConstants.MODID);

    static {
        CONDITION_CODECS.register("config_enabled", () -> ModConfigCondition.CODEC);
    }

    private static ModConfigServer server;
    private static ModConfigSpec serverSpec;
    private static ModConfigStartup startup;
    private static ModConfigSpec startupSpec;
    private static ModStress stressValues;

    public static ModConfigServer server() {
        return server;
    }

    public static ModConfigSpec serverSpec() {
        return serverSpec;
    }

    public static ModConfigStartup startup() {
        return startup;
    }

    public static ModConfigSpec startupSpec() {
        return startupSpec;
    }

    public static void register(ModContainer container) {
        Pair<ModConfigServer, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(ModConfigServer::new);
        server = pair.getLeft();
        serverSpec = pair.getRight();

        Pair<ModConfigStartup, ModConfigSpec> startupPair = new ModConfigSpec.Builder().configure(ModConfigStartup::new);
        startup = startupPair.getLeft();
        startupSpec = startupPair.getRight();

        container.registerConfig(ModConfig.Type.SERVER, serverSpec);
        container.registerConfig(ModConfig.Type.STARTUP, startupSpec);

        stressValues = new ModStress(server.mechanicalExtruder);
        BlockStressValues.IMPACTS.registerProvider(stressValues::getImpact);
    }

    public static void registerConditionCodecs(IEventBus eventBus) {
        CONDITION_CODECS.register(eventBus);
    }
}
