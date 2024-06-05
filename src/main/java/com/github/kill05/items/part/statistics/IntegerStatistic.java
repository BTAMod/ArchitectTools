package com.github.kill05.items.part.statistics;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IntegerStatistic extends PartStatistic<Integer> {

	public IntegerStatistic(String id, String color) {
		super(id, color, Integer.class, 0);
	}

	@Override
	public @NotNull Integer sumValue(@NotNull Integer value1, @NotNull Integer value2) {
		return value1 + value2;
	}

	@Override
	public @NotNull Integer multiplyValue(@NotNull Integer input, float mult) {
		return (int) (input * mult);
	}

	@Override
	public String formatToolValue(ItemStack itemStack, Integer value) {
		return getColor() + value;
	}
}
