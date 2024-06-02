package com.github.kill05.items.part.info;

public abstract class PartStatistics {

	private final int durability;

	protected PartStatistics(int durability) {
		this.durability = durability;
	}

	public int getDurability() {
		return durability;
	}
}
