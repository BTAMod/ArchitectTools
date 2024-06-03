package com.github.kill05.items.part.statistics;

import net.minecraft.core.item.ItemStack;

public class DurabilityStatistic extends IntegerStatistic {

	public DurabilityStatistic(String id) {
		super(id, null);
	}

	@Override
	public String formatValue(ItemStack itemStack, Integer value) {
		return "§5" + (value - itemStack.getMetadata()) + "§8/§5" + value + "§r";
	}
}
