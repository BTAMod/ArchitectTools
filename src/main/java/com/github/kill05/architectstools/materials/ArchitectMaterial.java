package com.github.kill05.architectstools.materials;

import com.github.kill05.architectstools.ArchitectTools;
import com.github.kill05.architectstools.MiningLevel;
import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.items.part.PartType;
import com.github.kill05.architectstools.items.part.statistics.PartStatistic;
import com.github.kill05.architectstools.items.part.statistics.PartStatistics;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.Registry;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ArchitectMaterial {
	// Used to avoid floating point error.
	// Each value is a prime number raised to the 4th power
	public static final int MATERIAL_UNIT_VALUE = 16 * 81 * 625;
	public static final Registry<ArchitectMaterial> MATERIAL_REGISTRY = new Registry<>();

	@ApiStatus.Internal
	private static boolean locked;

	public static final ArchitectMaterial WOOD = new ArchitectMaterial(ArchitectTools.MOD_ID, "wood", "#876627")
		.headStats(64, MiningLevel.STONE, 2.0f, 5f)
		.handleStats(15)
		.extraStats(-10)
		.addGroupValue(1, "minecraft:planks")
		.addItemValue(0.5f, Item.stick);

	public static final ArchitectMaterial STONE = new ArchitectMaterial(ArchitectTools.MOD_ID, "stone", "#B1AFAD")
		.headStats(30, MiningLevel.STONE, 2.5f, 4f)
		.handleStats(-20)
		.extraStats(-20)
		.addGroupValue(1, "minecraft:cobblestones");

	public static final ArchitectMaterial FLINT = new ArchitectMaterial(ArchitectTools.MOD_ID, "flint", "#828282", MaterialType.CONTRAST)
		.headStats(120, MiningLevel.IRON, 4.0f, 5.5f)
		.handleStats(-30)
		.extraStats(30)
		.addItemValue(1, Item.flint);

	public static final ArchitectMaterial BONE = new ArchitectMaterial(ArchitectTools.MOD_ID, "bone", "#E8E5D2", MaterialType.BONE)
		.headStats(150, MiningLevel.STONE, 2.0f, 6.5f)
		.handleStats(10)
		.extraStats(-25)
		.addItemValue(1, Item.bone)
		.addItemValue(1/3f, new ItemStack(Item.dye, 1, 15));

	public static final ArchitectMaterial CACTUS = new ArchitectMaterial(ArchitectTools.MOD_ID, "cactus", "#ffffff", MaterialType.CACTUS)
		.headStats(80, MiningLevel.STONE, 3.0f, 6f)
		.handleStats(-10)
		.extraStats(-10)
		.addItemValue(1, Block.cactus);

	public static final ArchitectMaterial PAPER = new ArchitectMaterial(ArchitectTools.MOD_ID, "paper", "#ffffff", MaterialType.PAPER)
		.headStats(20, MiningLevel.STONE, 1.5f, 3.5f)
		.handleStats(-10)
		.extraStats(-10)
		.addItemValue(1, Item.paper);


	public static final ArchitectMaterial OBSIDIAN = new ArchitectMaterial(ArchitectTools.MOD_ID, "obsidian", "#3B2754")
		.headStats(650, MiningLevel.OBSIDIAN, 4.5f, 6.5f)
		.handleStats(-50)
		.extraStats(50)
		.addItemValue(1, Block.obsidian);

	public static final ArchitectMaterial IRON = new ArchitectMaterial(ArchitectTools.MOD_ID, "iron", "#D8D8D8")
		.headStats(300, MiningLevel.DIAMOND, 6.0f, 7f)
		.handleStats(40)
		.extraStats(40)
		.addItemValue(1, Item.ingotIron);

	public static final ArchitectMaterial GOLD = new ArchitectMaterial(ArchitectTools.MOD_ID, "gold", "#FDF55F")
		.handleStats(-50)
		.extraStats(75)
		.addItemValue(1, Item.ingotGold);

	public static final ArchitectMaterial STEEL = new ArchitectMaterial(ArchitectTools.MOD_ID, "steel", "#505050")
		.headStats(900, MiningLevel.COBALT, 7.0f, 9f)
		.handleStats(90)
		.extraStats(-20)
		.addItemValue(1, Item.ingotSteel);


	@ApiStatus.Internal
	public static void lock() {
		if(locked) throw new IllegalStateException("Already locked!");
		locked = true;
	}


	public final String mod_id;
	private final String id;
	private final Color color;
	private final MaterialType type;
	private final Map<ItemStack, Integer> itemMaterialValue;
	private final Map<String, Float> groupMaterialValue;
	private final Map<PartType, PartStatistics> statisticsMap;

	public ArchitectMaterial(@NotNull String mod_id, @NotNull String id, @NotNull String color, @NotNull MaterialType type) {
		if(locked) throw new IllegalStateException("Locked!");
		this.mod_id = mod_id;
		this.id = id;
		this.color = Color.decode(color);
		this.type = type;
		this.itemMaterialValue = new HashMap<>();
		this.groupMaterialValue = new HashMap<>();
		this.statisticsMap = new LinkedHashMap<>();

		MATERIAL_REGISTRY.register(id(), this);
	}

	public ArchitectMaterial(String mod_id, String id, String color) {
		this(mod_id, id, color, MaterialType.NORMAL);
	}


	public static int getActualMaterialValue(float displayValue) {
		return Math.round(displayValue * MATERIAL_UNIT_VALUE);
	}

	public static float getDisplayMaterialValue(int actualValue) {
		return Math.round((float) actualValue / MATERIAL_UNIT_VALUE * 100f) / 100f;
	}


	public ArchitectMaterial addItemValue(float value, IItemConvertible... items) {
		int actualValue = getActualMaterialValue(value);
		for (IItemConvertible item : items) {
			this.itemMaterialValue.put(item.asItem().getDefaultStack(), actualValue);
		}

		return this;
	}

	public ArchitectMaterial addItemValue(float value, ItemStack... itemStacks) {
		int actualValue = getActualMaterialValue(value);
		for (ItemStack item : itemStacks) {
			this.itemMaterialValue.put(item, actualValue);
		}

		return this;
	}

	public ArchitectMaterial addGroupValue(float value, String group) {
		groupMaterialValue.put(group, value);
		return this;
	}

	public Integer getMaterialValue(ItemStack itemStack) {
		for (Map.Entry<ItemStack, Integer> entry : itemMaterialValue.entrySet()) {
			ItemStack item = entry.getKey();
			if(item.itemID == itemStack.itemID && item.getMetadata() == itemStack.getMetadata()) return entry.getValue();
		}

		return null;
	}

	public void initGroups() {
		for (Map.Entry<String, Float> entry : groupMaterialValue.entrySet()) {
			List<ItemStack> list = Registries.ITEM_GROUPS.getItem(entry.getKey());
			if(list == null) throw new IllegalArgumentException("Invalid group: " + entry.getKey());

			addItemValue(entry.getValue(), list.toArray(new ItemStack[0]));
		}
	}


	protected ArchitectMaterial headStats(int durability, int miningLevel, float miningSpeed, float entityDamage) {
		PartStatistics statistics = new PartStatistics(durability);
		statistics.setStatistic(PartStatistic.MINING_LEVEL, miningLevel);
		statistics.setStatistic(PartStatistic.MINING_SPEED, miningSpeed);
		statistics.setStatistic(PartStatistic.ENTITY_DAMAGE, entityDamage);
		statisticsMap.put(PartType.HEAD, statistics);
		return this;
	}

	protected ArchitectMaterial handleStats(int durability) {
		PartStatistics statistics = new PartStatistics(durability);
		statisticsMap.put(PartType.HANDLE, statistics);
		return this;
	}

	protected ArchitectMaterial extraStats(int durability) {
		PartStatistics statistics = new PartStatistics(durability);
		statisticsMap.put(PartType.EXTRA, statistics);
		return this;
	}

	public PartStatistics getStatistics(PartType type) {
		return statisticsMap.get(type);
	}

	public PartStatistics getHeadStatistics() {
		return getStatistics(PartType.HEAD);
	}

	public PartStatistics getHandleStatistics() {
		return getStatistics(PartType.HANDLE);
	}

	public PartStatistics getExtraStatistics() {
		return getStatistics(PartType.EXTRA);
	}

	public boolean isValidPart(ArchitectPart part) {
		if(part == ArchitectPart.REPAIR_KIT) return true;

		for (PartType type : part.getValidTypes()) {
			if(getStatistics(type) != null) return true;
		}

		return false;
	}


	public String getTranslatedName() {
		return I18n.getInstance().translateNameKey("material." + ArchitectTools.MOD_ID + "." + id());
	}


	public String id() {
		return id;
	}

	public Color color() {
		return color;
	}

	public MaterialType type() {
		return type;
	}

	public Map<ItemStack, Integer> getItemMaterialValue() {
		return itemMaterialValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ArchitectMaterial material = (ArchitectMaterial) o;
		return Objects.equals(id, material.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
