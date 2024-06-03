package com.github.kill05.items.tool;

import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import com.github.kill05.items.part.statistics.PartStatistic;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemToolPickaxe;

public class ArchitectPickaxe extends ArchitectTool {

	public ArchitectPickaxe(int id) {
		super(id, "pickaxe");
		addPart(ArchitectPart.PICKAXE_HEAD, PartType.HEAD, "pickaxe/head");
		addPart(new ToolPartInfo(ArchitectPart.TOOL_BINDING, PartType.EXTRA, "pickaxe/binding").renderPriority(-1));
		addPart(ArchitectPart.TOOL_ROD, PartType.HANDLE, "pickaxe/handle");

		addValidStatistic(PartStatistic.MINING_LEVEL);
		addValidStatistic(PartStatistic.MINING_SPEED);
		addValidStatistic(PartStatistic.ENTITY_DAMAGE);
	}

	@Override
	public boolean canHarvestBlock(ItemStack itemStack, Block block) {
		Integer requiredLevel = ItemToolPickaxe.miningLevels.get(block);
		if(requiredLevel == null) return block.hasTag(BlockTags.MINEABLE_BY_PICKAXE);
		return getStatistic(itemStack, PartStatistic.MINING_LEVEL) >= requiredLevel;
	}

}
