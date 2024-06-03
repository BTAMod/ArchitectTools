package com.github.kill05.items.part.statistics;

import com.github.kill05.MiningLevel;
import net.minecraft.core.item.ItemStack;

public class MiningLevelStatistic extends IntegerStatistic {

	public MiningLevelStatistic(String id) {
		super(id, null);
	}

	@Override
	public String formatValue(ItemStack itemStack, Integer value) {
		return MiningLevel.getTranslatedMiningLevel(value);
	}
}
