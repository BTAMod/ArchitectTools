package com.github.kill05.architectstools.items.tool;

import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.items.part.statistics.PartStatistic;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.tag.ItemTags;

public class ArchitectShortsword extends ArchitectTool {

	public ArchitectShortsword() {
		super("shortsword");

		addPart(ArchitectPart.SWORD_BLADE, "shortsword/blade", "shortsword/broken_blade");
		addPart(ArchitectPart.SWORD_GUARD, "shortsword/guard");
		addPart(ArchitectPart.TOOL_ROD, "shortsword/handle").renderPriority(1);

		addValidStatistic(PartStatistic.ENTITY_DAMAGE, 1.1f);
		addMineableTags(BlockTags.MINEABLE_BY_SWORD);
		//noinspection unchecked
		withTags(ItemTags.PREVENT_CREATIVE_MINING);
	}


}

