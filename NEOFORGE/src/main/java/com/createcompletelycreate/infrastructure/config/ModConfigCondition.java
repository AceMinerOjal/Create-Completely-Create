package com.createcompletelycreate.infrastructure.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;

public class ModConfigCondition implements ICondition {
	public static final MapCodec<ModConfigCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
					Codec.STRING.fieldOf("path").forGetter(ModConfigCondition::path)
			).apply(instance, ModConfigCondition::new));

	private final String path;

	public ModConfigCondition(String path) {
		this.path = path;
	}

	public String path() {
		return path;
	}

	@Override
	public boolean test(IContext context) {
		Boolean server = value(ModConfigs.serverSpec(), path);
		if (server != null) return server;
		Boolean startup = value(ModConfigs.startupSpec(), path);
		if (startup != null) return startup;
		return true;
	}

	private static Boolean value(ModConfigSpec spec, String path) {
		if (spec == null || !spec.isLoaded())
			return null;
		Object val = spec.getValues().get(path);
		// getValues() stores ConfigValue wrappers, not raw values
		if (val instanceof ModConfigSpec.ConfigValue<?> configValue && configValue.get() instanceof Boolean b)
			return b;
		return null;
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}
}
