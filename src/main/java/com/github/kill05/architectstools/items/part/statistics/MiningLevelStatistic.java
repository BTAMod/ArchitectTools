package com.github.kill05.architectstools.items.part.statistics;

import com.github.kill05.architectstools.MiningLevel;
import net.minecraft.core.item.ItemStack;

public class MiningLevelStatistic extends IntegerStatistic {

	public MiningLevelStatistic(String id) {
		super(id, "§2");
	}

	@Override
	public String formatToolValue(ItemStack itemStack, Integer value) {
		return getColor() + MiningLevel.getTranslatedMiningLevel(value);
	}
}
