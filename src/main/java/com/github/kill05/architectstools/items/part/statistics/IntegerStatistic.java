package com.github.kill05.architectstools.items.part.statistics;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IntegerStatistic extends PartStatistic<Integer> {

	public IntegerStatistic(String id, String color, int minValue) {
		super(id, color, Integer.class, minValue);
	}

	public IntegerStatistic(String id, String color) {
		this(id, color, 0);
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
	public @NotNull Integer max(Integer value1, Integer value2) {
		return Math.max(value1, value2);
	}

	@Override
	public String formatToolValue(ItemStack itemStack, Integer value) {
		return getColor() + value;
	}
}
