package com.github.kill05.architectstools.items.part.statistics;

import com.github.kill05.architectstools.ArchitectTools;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class PartStatistic<V extends Number> {

	public static final List<PartStatistic<?>> VALUES = new ArrayList<>();
	public static final PartStatistic<Integer> DURABILITY = new DurabilityStatistic("durability");
	public static final PartStatistic<Integer> MINING_LEVEL = new MiningLevelStatistic("mining_level");
	public static final PartStatistic<Float> MINING_SPEED = new FloatStatistic("mining_speed", "§<78a0cd>");
	public static final PartStatistic<Float> ENTITY_DAMAGE = new FloatStatistic("entity_damage", "§e");

	private final String id;
	private final String color;
	private final Class<V> valueClass;
	private final V minValue;

	public PartStatistic(String id, String color, Class<V> valueClass, V minValue) {
		if(VALUES.contains(this)) throw new IllegalStateException("Duplicate id: " + id);
		this.id = id;
		this.color = color;
		this.valueClass = valueClass;
		this.minValue = minValue;
		VALUES.add(this);
	}

	public abstract @NotNull V sumValue(@NotNull V value1, @NotNull V value2);

	public abstract @NotNull V multiplyValue(@NotNull V value, float mult);

	public abstract @NotNull V max(V value1, V value2);

	public abstract String formatToolValue(ItemStack itemStack, V value);

	public String formatPartValue(ItemStack itemStack, V value) {
		return formatToolValue(itemStack, value);
	}

	@SuppressWarnings("unchecked")
	public String formatToolValue(ItemStack itemStack, Object value) throws ClassCastException {
		return formatToolValue(itemStack, (V) value);
	}

	@SuppressWarnings("unchecked")
	public String formatPartValue(ItemStack itemStack, Object value) {
		return formatPartValue(itemStack, (V) value);
	}


	public String getTranslatedName() {
		return I18n.getInstance().translateNameKey("statistic." + ArchitectTools.MOD_ID + "." + id);
	}

	public String getId() {
		return id;
	}

	public String getColor() {
		return color;
	}

	public Class<V> getValueClass() {
		return valueClass;
	}

	public V getMinValue() {
		return minValue;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PartStatistic<?> that = (PartStatistic<?>) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
