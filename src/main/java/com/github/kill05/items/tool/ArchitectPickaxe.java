package com.github.kill05.items.tool;

import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.statistics.PartStatistic;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemToolPickaxe;

public class ArchitectPickaxe extends ArchitectTool {

	public ArchitectPickaxe() {
		super("pickaxe");
		addPart(ArchitectPart.PICKAXE_HEAD, "pickaxe/head", "pickaxe/broken_head");
		addPart(ArchitectPart.TOOL_BINDING, "pickaxe/binding").renderPriority(-1);
		addPart(ArchitectPart.TOOL_ROD, "pickaxe/handle");

		addValidStatistic(PartStatistic.MINING_LEVEL);
		addValidStatistic(PartStatistic.MINING_SPEED);
		addValidStatistic(PartStatistic.ENTITY_DAMAGE, 0.5f);

		addMineableTags(BlockTags.MINEABLE_BY_PICKAXE);
	}

	@Override
	public boolean canHarvestBlock(ItemStack itemStack, Block block) {
		Integer requiredLevel = ItemToolPickaxe.miningLevels.get(block);
		if(requiredLevel == null) return super.canHarvestBlock(itemStack, block);
		return getStatistic(itemStack, PartStatistic.MINING_LEVEL) >= requiredLevel;
	}

}
