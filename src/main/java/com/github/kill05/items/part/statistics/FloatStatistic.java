package com.github.kill05.items.part.statistics;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class FloatStatistic extends PartStatistic<Float> {

	public static final DecimalFormat FORMAT = new DecimalFormat();

	public FloatStatistic(String id, String color) {
		super(id, color, Float.class, 0f);
	}

	@Override
	public @NotNull Float sumValue(@NotNull Float value1, @NotNull Float value2) {
		return value1 + value2;
	}

	@Override
	public @NotNull Float multiplyValue(@NotNull Float value, float mult) {
		return value * mult;
	}

	@Override
	public String formatValue(ItemStack itemStack, Float value) {
		return getColor() + FORMAT.format(value);
	}


	static {
		FORMAT.setMaximumFractionDigits(2);
	}
}
