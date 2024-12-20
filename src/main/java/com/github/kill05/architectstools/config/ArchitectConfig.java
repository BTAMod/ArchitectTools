package com.github.kill05.architectstools.config;

import com.github.kill05.architectstools.ArchitectTools;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

public final class ArchitectConfig {

	public static final TomlConfigHandler CONFIG;

	public static int PART_ID;
	public static int TOOL_ID;
	public static int ITEM_ID;

	public static int BLOCK_ID;

	static {
		Toml toml = new Toml();

		// Id
		toml.addCategory("Set from which ID each category of blocks/items should start registering.", "IDs");
		toml.addEntry("IDs.Parts", 23000);
		toml.addEntry("IDs.Tools", 23100);
		toml.addEntry("IDs.Items", 23200);
		toml.addEntry("IDs.Blocks", 1500);

		// Gameplay
		toml.addCategory("Change how the game and the mod are played.", "Gameplay");
		toml.addEntry("Gameplay.DisableVanillaTools", "Set to true to disable vanilla tools so players can only use tools from Architect's Tools.", false);

		CONFIG = new TomlConfigHandler(ArchitectTools.MOD_ID, toml);

		PART_ID = CONFIG.getInt("IDs.Parts");
		TOOL_ID = CONFIG.getInt("IDs.Tools");
		ITEM_ID = CONFIG.getInt("IDs.Items");
		BLOCK_ID = CONFIG.getInt("IDs.Blocks");
	}

	private ArchitectConfig() {

	}

	public static boolean getDisableVanillaTools() {
		return CONFIG.getBoolean("Gameplay.DisableVanillaTools");
	}

}
