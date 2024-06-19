package com.github.kill05.architectstools.items.part.statistics;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PartStatistics implements Iterable<Map.Entry<PartStatistic<?>, Object>> {

	private final Map<PartStatistic<?>, Object> statisticMap;

	public PartStatistics() {
		this.statisticMap = new HashMap<>();
	}

	public PartStatistics(int durability) {
		this();
		setStatistic(PartStatistic.DURABILITY, durability);
	}


	public <V extends Number> void setStatistic(@NotNull PartStatistic<V> statistic, @NotNull V value) {
		statisticMap.put(statistic, value);
	}

	@SuppressWarnings("unchecked")
	public <V extends Number> @NotNull V getStatistic(@NotNull PartStatistic<V> statistic) {
		Object o = statisticMap.get(statistic);
		return o != null ? (V) o : statistic.getMinValue();
	}

	public boolean hasStatistic(PartStatistic<?> statistic) {
		return statisticMap.containsKey(statistic);
	}


	@NotNull
	@Override
	public Iterator<Map.Entry<PartStatistic<?>, Object>> iterator() {
		return statisticMap.entrySet().iterator();
	}
}
