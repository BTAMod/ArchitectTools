package com.github.kill05.items.part.info;

import com.github.kill05.ArchitectTools;
import net.minecraft.core.lang.I18n;
import turniplabs.halplibe.mixin.accessors.LanguageAccessor;

import java.util.Properties;

public class HeadStatistics extends PartStatistics {

	private final int miningLevel;
	private final float miningSpeed;

	public HeadStatistics(int durability, int miningLevel, float miningSpeed) {
		super(durability);
		if(miningLevel < 0) throw new IllegalArgumentException("Mining level must be positive.");

		this.miningLevel = miningLevel;
		this.miningSpeed = miningSpeed;
	}

	public String getTranslatedMiningLevel() {
		Properties entries = ((LanguageAccessor) I18n.getInstance().getCurrentLanguage()).getEntries();
		String translation = entries.get("mining_level." + ArchitectTools.MOD_ID + "." + miningLevel + ".name").toString();
		return translation != null ? translation : String.valueOf(miningLevel);
	}

	public int getMiningLevel() {
		return miningLevel;
	}

	public float getMiningSpeed() {
		return miningSpeed;
	}
}
