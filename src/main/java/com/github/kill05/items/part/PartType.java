package com.github.kill05.items.part;

import com.github.kill05.ArchitectTools;
import net.minecraft.core.lang.I18n;

public enum PartType {

	HEAD("head"),
	HANDLE("handle"),
	EXTRA("extra");

	private final String id;

	PartType(String id) {
		this.id = id;
	}

	public String getTranslatedName() {
		return I18n.getInstance().translateNameKey("part_type." + ArchitectTools.MOD_ID + "." + id);
	}

	public String getId() {
		return id;
	}
}
