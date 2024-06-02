package com.github.kill05.items.part;

public enum PartType {

	HEAD("head"),
	HANDLE("handle"),
	EXTRA("extra");

	private final String id;

	PartType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
