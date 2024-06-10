package com.github.kill05.items.tool;

import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.statistics.PartStatistic;
import net.minecraft.core.block.tag.BlockTags;

public class ArchitectMattock extends ArchitectTool {

	public ArchitectMattock(int id) {
		super(id, "mattock");

		addPart(ArchitectPart.AXE_HEAD, "mattock/head");
		addPart(ArchitectPart.SHOVEL_HEAD, "mattock/back");
		addPart(ArchitectPart.TOOL_ROD, "mattock/handle").renderPriority(1);

		addValidStatistic(PartStatistic.MINING_SPEED, 0.4f);
		addValidStatistic(PartStatistic.ENTITY_DAMAGE, 0.25f);

		addMineableTags(BlockTags.MINEABLE_BY_AXE);
		addMineableTags(BlockTags.MINEABLE_BY_SHOVEL);
		addMineableTags(BlockTags.MINEABLE_BY_HOE);
	}
}
