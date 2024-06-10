package com.github.kill05.items.tool;

import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.statistics.PartStatistic;
import net.minecraft.core.block.tag.BlockTags;

public class ArchitectShovel extends ArchitectTool {

	public ArchitectShovel(int id) {
		super(id, "shovel");
		addPart(ArchitectPart.SHOVEL_HEAD, "shovel/head");
		addPart(ArchitectPart.TOOL_ROD, "shovel/handle");
		addPart(ArchitectPart.TOOL_BINDING, "shovel/binding");

		addValidStatistic(PartStatistic.MINING_SPEED);
		addValidStatistic(PartStatistic.ENTITY_DAMAGE, 0.5f);

		addMineableTags(BlockTags.MINEABLE_BY_SHOVEL);
	}
}
