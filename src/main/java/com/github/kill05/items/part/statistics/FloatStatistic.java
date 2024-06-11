package com.github.kill05.items.part.statistics;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FloatStatistic extends PartStatistic<Float> {

	public static final DecimalFormat FORMAT = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

	public FloatStatistic(String id, String color, float minValue) {
		super(id, color, Float.class, minValue);
	}

	public FloatStatistic(String id, String color) {
		this(id, color, 0f);
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
	public @NotNull Float max(Float value1, Float value2) {
		return Math.max(value1, value2);
	}

	@Override
	public String formatToolValue(ItemStack itemStack, Float value) {
		return getColor() + FORMAT.format(value);
	}

}
