package com.createcompletelycreate.compat.qol.items;

import fr.iglee42.createqualityoflife.config.CreateQOLConfigs;
import fr.iglee42.createqualityoflife.registries.QOLDataComponents;

import java.util.List;

public final class PaxelAbilities {

    public static final PaxelItem.Ability DIGGING = new PaxelItem.Ability(
            QOLDataComponents.DIGGING,
            "createqol.ability.tool.digging",
            "Digging",
            List.of("Activate the 3x3x3 digging when mining a block"),
            () -> CreateQOLConfigs.server().equipments.tools.digging.get());

    public static final PaxelItem.Ability VEIN_MINE = new PaxelItem.Ability(
            QOLDataComponents.VEIN_MINE,
            "createqol.ability.tool.vein_mine",
            "Vein Mine",
            List.of("Should all the blocks of the same types be destroy when mining"),
            () -> CreateQOLConfigs.server().equipments.tools.veinMine.get());

    public static final PaxelItem.Ability TREE_DECAPITATION = new PaxelItem.Ability(
            QOLDataComponents.TREE_DECAPITATION,
            "createqol.ability.tool.tree_decapitation",
            "Tree Decapitation",
            List.of("Should destroy a tree when a log is broken like a mechanical saw"),
            () -> CreateQOLConfigs.server().equipments.tools.treeDecapitation.get());

    public static final PaxelItem.Ability CASINGIFIER = new PaxelItem.Ability(
            QOLDataComponents.CASINGIFIER,
            "createqol.ability.tool.casingifier",
            "Casingifier",
            List.of("When stripping a log transform it into casing if a valid casing ingredient is available in the off hand", "It also transform adjacent blocks"),
            () -> CreateQOLConfigs.server().equipments.tools.casingifier.get());

    public static final PaxelItem.Ability SMELTING = new PaxelItem.Ability(
            QOLDataComponents.SMELTING,
            "createqol.ability.tool.smelting",
            "Smelting",
            List.of("Should smelt the mined blocks"),
            () -> CreateQOLConfigs.server().equipments.tools.smelting.get());

    private PaxelAbilities() {
    }
}
