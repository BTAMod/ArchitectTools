package com.github.kill05;

import net.minecraft.core.lang.I18n;
import turniplabs.halplibe.mixin.accessors.LanguageAccessor;

import java.util.Properties;

public final class MiningLevel {

	public static final int STONE = 0;
	public static final int IRON = 1;
	public static final int DIAMOND = 2;
	public static final int OBSIDIAN = 3;
	public static final int COBALT = 4;

	public static String getTranslatedMiningLevel(int miningLevel) {
		Properties entries = ((LanguageAccessor) I18n.getInstance().getCurrentLanguage()).getEntries();
		String translation = entries.get("mining_level." + ArchitectTools.MOD_ID + "." + miningLevel + ".name").toString();
		return translation != null ? translation : String.valueOf(miningLevel);
	}

}
