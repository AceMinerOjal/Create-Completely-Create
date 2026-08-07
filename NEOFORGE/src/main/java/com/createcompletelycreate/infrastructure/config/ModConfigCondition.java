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
		ModConfigSpec spec = ModConfigs.serverSpec();
		if (spec == null || !spec.isLoaded())
			return true;
		return spec.getValues().getOrElse(path, true);
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}
}
