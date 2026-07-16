package com.createcompletelycreate;

import net.minecraft.resources.ResourceLocation;

public class ModConstants {
    public static final String MODID = "createcompletelycreate";
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
