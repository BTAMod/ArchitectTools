package com.github.kill05.architectstools.items.tool;

import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.items.part.statistics.PartStatistic;
import net.minecraft.core.block.tag.BlockTags;

public class ArchitectAxe extends ArchitectTool {

	public ArchitectAxe() {
		super("axe");
		addPart(ArchitectPart.AXE_HEAD, "axe/head", "axe/broken_head");
		addPart(ArchitectPart.TOOL_BINDING, "axe/binding").renderPriority(-1);
		addPart(ArchitectPart.TOOL_ROD, "pickaxe/handle");

		addValidStatistic(PartStatistic.MINING_SPEED);
		addValidStatistic(PartStatistic.ENTITY_DAMAGE, 0.75f);

		addMineableTags(BlockTags.MINEABLE_BY_AXE);
	}
}
