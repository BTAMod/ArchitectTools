package com.github.kill05.materials;

import com.github.kill05.ArchitectTools;
import com.github.kill05.MiningLevel;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import com.github.kill05.items.part.statistics.PartStatistics;
import com.github.kill05.items.part.statistics.PartStatistic;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.Registry;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ArchitectMaterial {

	public static final Registry<ArchitectMaterial> MATERIAL_REGISTRY = new Registry<>();

	public static final ArchitectMaterial WOOD = new ArchitectMaterial("wood", "#876627")
		.headStats(64, MiningLevel.STONE, 2.0f, 5f)
		.handleStats(15)
		.extraStats(-10)
		.addItemGroup("minecraft:planks");

	public static final ArchitectMaterial STONE = new ArchitectMaterial("stone", "#B1AFAD")
		.headStats(30, MiningLevel.STONE, 2.5f, 4f)
		.handleStats(-20)
		.extraStats(-20)
		.addItemGroup("minecraft:cobblestones");

	public static final ArchitectMaterial FLINT = new ArchitectMaterial("flint", "#828282", MaterialType.CONTRAST)
		.headStats(120, MiningLevel.IRON, 4.0f, 6f)
		.handleStats(-30)
		.extraStats(30)
		.addItems(Item.flint);

	public static final ArchitectMaterial BONE = new ArchitectMaterial("bone", "#E8E5D2", MaterialType.BONE)
		.addItems(Item.bone);

	public static final ArchitectMaterial CACTUS = new ArchitectMaterial("cactus", "#ffffff", MaterialType.CACTUS)
		.headStats(80, MiningLevel.STONE, 3.0f, 6f)
		.handleStats(-10)
		.extraStats(-10)
		.addItems(Block.cactus);

	public static final ArchitectMaterial PAPER = new ArchitectMaterial("paper", "#ffffff", MaterialType.PAPER)
		.headStats(20, MiningLevel.STONE, 1.5f, 2.5f)
		.handleStats(-10)
		.extraStats(-10)
		.addItems(Item.paper);


	public static final ArchitectMaterial OBSIDIAN = new ArchitectMaterial("obsidian", "#3B2754")
		.headStats(650, MiningLevel.OBSIDIAN, 4.5f, 6.5f)
		.handleStats(-50)
		.extraStats(50)
		.addItems(Block.obsidian);

	public static final ArchitectMaterial IRON = new ArchitectMaterial("iron", "#D8D8D8")
		.headStats(300, MiningLevel.DIAMOND, 6.0f, 7f)
		.handleStats(40)
		.extraStats(40)
		.addItems(Item.ingotIron);

	public static final ArchitectMaterial GOLD = new ArchitectMaterial("gold", "#FDF55F")
		.handleStats(-50)
		.extraStats(75)
		.addItems(Item.ingotGold);

	public static final ArchitectMaterial STEEL = new ArchitectMaterial("steel", "#505050")
		.headStats(900, MiningLevel.COBALT, 7.0f, 9f)
		.handleStats(90)
		.extraStats(-20)
		.addItems(Item.ingotSteel);


	private final String id;
	private final Color color;
	private final MaterialType type;
	private final Collection<ItemStack> items;
	private final Map<PartType, PartStatistics> statisticsMap;

	public ArchitectMaterial(@NotNull String id, @NotNull String color, @NotNull MaterialType type) {
		this.id = id;
		this.color = Color.decode(color);
		this.type = type;
		this.items = new HashSet<>();
		this.statisticsMap = new LinkedHashMap<>();

		MATERIAL_REGISTRY.register(id(), this);
	}

	public ArchitectMaterial(String id, String color) {
		this(id, color, MaterialType.NORMAL);
	}


	public @NotNull ItemStack createPart(ArchitectPart part) {
		ItemStack item = new ItemStack(part);
		ArchitectTools.setPartMaterial(item, this);
		return item;
	}


	public ArchitectMaterial addItems(IItemConvertible... items) {
		for (IItemConvertible item : items) {
			this.items.add(item.asItem().getDefaultStack());
		}

		return this;
	}

	public ArchitectMaterial addItems(ItemStack... itemStacks) {
		items.addAll(List.of(itemStacks));
		return this;
	}

	public ArchitectMaterial addItemGroup(String group) {
		List<ItemStack> list = Registries.ITEM_GROUPS.getItem(group);
		if(list == null) throw new IllegalArgumentException("Invalid group: " + group);

		items.addAll(list);
		return this;
	}

	public boolean isThisMaterial(ItemStack itemStack) {
		for (ItemStack item : items) {
			if(item.itemID == itemStack.itemID && item.getMetadata() == itemStack.getMetadata()) return true;
		}

		return false;
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

	public Collection<ItemStack> items() {
		return items;
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
