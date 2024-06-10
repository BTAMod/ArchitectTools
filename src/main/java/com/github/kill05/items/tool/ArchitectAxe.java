package com.github.kill05.items.tool;

import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.statistics.PartStatistic;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;

public class ArchitectAxe extends ArchitectTool {

	public ArchitectAxe() {
		super("axe");
		addPart(ArchitectPart.AXE_HEAD, "axe/head");
		addPart(ArchitectPart.TOOL_BINDING, "axe/binding").renderPriority(-1);
		addPart(ArchitectPart.TOOL_ROD, "pickaxe/handle");

		addValidStatistic(PartStatistic.MINING_SPEED);
		addValidStatistic(PartStatistic.ENTITY_DAMAGE, 0.75f);

		addMineableTags(BlockTags.MINEABLE_BY_AXE);
	}

	@Override
	public boolean canHarvestBlock(ItemStack item, Block block) {
		return block.hasTag(BlockTags.MINEABLE_BY_AXE);
	}
}
