package com.github.kill05.architectstools;

import com.github.kill05.architectstools.blocks.architectstation.ArchitectTableBlock;
import com.github.kill05.architectstools.config.ArchitectConfig;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.*;

public final class ArchitectItems {
	public static <T extends Item> T item(T item, String texture) {
		return new ItemBuilder(ArchitectTools.MOD_ID)
			.setIcon(ArchitectTools.MOD_ID + ":item/" + texture)
			.build(item);
	}

	public static Item item(String name, String texture) {
		return new ItemBuilder(ArchitectTools.MOD_ID)
			.setIcon(ArchitectTools.MOD_ID + ":item/" + texture)
			.build(new Item(name, ArchitectConfig.ITEM_ID++));
	}

    public static void init() {
        ArchitectTools.BLANK_PATTERN = item("blank_pattern", "blank_pattern");
	    ArchitectTools.ARCHITECT_TABLE_BLOCK = new BlockBuilder(ArchitectTools.MOD_ID)
            .setTopTexture(ArchitectTools.MOD_ID + ":block/architect_table_top")
            .setSideTextures(ArchitectTools.MOD_ID + ":block/architect_table_side")
            .setBottomTexture("minecraft:block/planks_oak")
            .setHardness(2.5F)
            .setResistance(5.0F)
            .setFlammability(10, 10)
            .setBlockSound(BlockSounds.WOOD)
            .setTags(BlockTags.MINEABLE_BY_AXE)
            .build(new ArchitectTableBlock());
    }
}
