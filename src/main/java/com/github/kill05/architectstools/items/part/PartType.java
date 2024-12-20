package com.github.kill05.architectstools.items.part;

import com.github.kill05.architectstools.ArchitectTools;
import com.github.kill05.architectstools.items.part.statistics.PartStatistics;
import com.github.kill05.architectstools.materials.ArchitectMaterial;
import net.minecraft.core.lang.I18n;

public enum PartType {

	HEAD("head"),
	HANDLE("handle"),
	EXTRA("extra");

	private final String id;

	PartType(String id) {
		this.id = id;
	}


	public PartStatistics getStatistics(ArchitectMaterial material) {
		return material.getStatistics(this);
	}


	public String getTranslatedName() {
		return I18n.getInstance().translateNameKey("part_type." + ArchitectTools.MOD_ID + "." + id);
	}

	public String getId() {
		return id;
	}
}
