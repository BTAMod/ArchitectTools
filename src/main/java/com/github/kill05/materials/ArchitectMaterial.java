package com.github.kill05.materials;

import com.github.kill05.ArchitectTools;
import com.github.kill05.MiningLevel;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.info.ExtraStatistics;
import com.github.kill05.items.part.info.HeadStatistics;
import com.github.kill05.items.part.info.HandleStatistics;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.Registry;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ArchitectMaterial {

	public static final Registry<ArchitectMaterial> MATERIAL_REGISTRY = new Registry<>();

	public static final ArchitectMaterial WOOD = new ArchitectMaterial("wood", "#876627")
		.headStats(new HeadStatistics(64, MiningLevel.STONE, 2.0f))
		.handleStats(new HandleStatistics(30))
		.extraStats(new ExtraStatistics(10))
		.addItemGroup("minecraft:planks");

	public static final ArchitectMaterial FLINT = new ArchitectMaterial("flint", "#828282", MaterialType.CONTRAST)
		.headStats(new HeadStatistics(120, MiningLevel.IRON, 4.0f))
		.handleStats(new HandleStatistics(-30))
		.extraStats(new ExtraStatistics(30))
		.addItems(Item.flint);

	public static final ArchitectMaterial BONE = new ArchitectMaterial("bone", "#ffffff", MaterialType.BONE)
		.addItems(Item.bone);

	public static final ArchitectMaterial CACTUS = new ArchitectMaterial("cactus", "#ffffff", MaterialType.CACTUS)
		.headStats(new HeadStatistics(80, MiningLevel.STONE, 3.0f))
		.handleStats(new HandleStatistics(-10))
		.extraStats(new ExtraStatistics(-10))
		.addItems(Block.cactus);

	public static final ArchitectMaterial PAPER = new ArchitectMaterial("paper", "#ffffff", MaterialType.PAPER)
		.headStats(new HeadStatistics(20, MiningLevel.STONE, 1.5f))
		.handleStats(new HandleStatistics(-10))
		.extraStats(new ExtraStatistics(-10))
		.addItems(Item.paper);


	public static final ArchitectMaterial IRON = new ArchitectMaterial("iron", "#D8D8D8")
		.headStats(new HeadStatistics(300, MiningLevel.REDSTONE, 6.0f))
		.handleStats(new HandleStatistics(40))
		.extraStats(new ExtraStatistics(40))
		.addItems(Item.ingotIron);

	public static final ArchitectMaterial GOLD = new ArchitectMaterial("gold", "#FDF55F")
		.handleStats(new HandleStatistics(-50))
		.extraStats(new ExtraStatistics(75))
		.addItems(Item.ingotGold);

	public static final ArchitectMaterial STEEL = new ArchitectMaterial("steel", "#959595")
		.headStats(new HeadStatistics(900, MiningLevel.COBALT, 7.0f))
		.handleStats(new HandleStatistics(90))
		.extraStats(new ExtraStatistics(-20))
		.addItems(Item.ingotSteel);


	private final String id;
	private final Color color;
	private final MaterialType type;
	private final Collection<ItemStack> items;
	private HeadStatistics headStatistics;
	private HandleStatistics handleStatistics;
	private ExtraStatistics extraStatistics;

	public ArchitectMaterial(@NotNull String id, @NotNull String color, @NotNull MaterialType type) {
		this.id = id;
		this.color = Color.decode(color);
		this.type = type;
		this.items = new HashSet<>();

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


	public ArchitectMaterial headStats(HeadStatistics headStatistics) {
		this.headStatistics = headStatistics;
		return this;
	}

	public ArchitectMaterial handleStats(HandleStatistics handleStatistics) {
		this.handleStatistics = handleStatistics;
		return this;
	}

	public ArchitectMaterial extraStats(ExtraStatistics extraStatistics) {
		this.extraStatistics = extraStatistics;
		return this;
	}

	public HeadStatistics getHeadStatistics() {
		return headStatistics;
	}

	public HandleStatistics getToolRodStatistics() {
		return handleStatistics;
	}

	public ExtraStatistics getExtraStatistics() {
		return extraStatistics;
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
