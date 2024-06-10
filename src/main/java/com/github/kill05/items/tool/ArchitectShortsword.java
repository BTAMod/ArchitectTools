package com.github.kill05.items.tool;

import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.statistics.PartStatistic;

public class ArchitectShortsword extends ArchitectTool {

	public ArchitectShortsword(int id) {
		super(id, "shortsword");

		addPart(ArchitectPart.SWORD_BLADE, "shortsword/blade");
		addPart(ArchitectPart.SWORD_GUARD, "shortsword/guard");
		addPart(ArchitectPart.TOOL_ROD, "shortsword/handle");

		addValidStatistic(PartStatistic.ENTITY_DAMAGE, 1.1f);
	}


}
