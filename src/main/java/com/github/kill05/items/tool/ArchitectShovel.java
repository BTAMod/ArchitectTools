package com.github.kill05.items.tool;

import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;

public class ArchitectShovel extends ArchitectTool {

	public ArchitectShovel(int id) {
		super(id, "shovel");
		addPart(ArchitectPart.SHOVEL_HEAD, PartType.HEAD, "shovel/head");
		addPart(ArchitectPart.TOOL_ROD, PartType.HEAD, "shovel/handle");
		addPart(ArchitectPart.TOOL_BINDING, PartType.HEAD, "shovel/binding");
	}

	@Override
	public boolean canHarvestBlock(ItemStack item, Block block) {
		return block.hasTag(BlockTags.MINEABLE_BY_SHOVEL);
	}
}
