package com.github.kill05;

import com.github.kill05.blocks.architectstation.ArchitectTableBlock;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.exceptions.ArchitectItemException;
import com.github.kill05.exceptions.InvalidMaterialException;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import com.github.kill05.items.tool.ArchitectTool;
import com.github.kill05.items.tool.ToolPartInfo;
import com.github.kill05.materials.ArchitectMaterial;
import com.github.kill05.utils.ItemUtils;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.StringTag;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;


public final class ArchitectTools implements ModInitializer, RecipeEntrypoint, ClientStartEntrypoint {

	public static final String MOD_ID = "architectstools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int MAX_TOOL_PARTS = 3;
	public static int ITEM_ID = 20000;
	public static int BLOCK_ID = 1500;

	static {
		new Registries();
	}


	// Items and Blocks
	public static final Item BLANK_PATTERN = item("blank_pattern", "blank_pattern");

	public static final Block ARCHITECT_STATION = new BlockBuilder(MOD_ID)
		.build(new ArchitectTableBlock(BLOCK_ID++));


	public static <T extends Item> T item(T item, String texture) {
		return new ItemBuilder(MOD_ID)
			.setIcon(MOD_ID + ":item/" + texture)
			.build(item);
	}

	public static Item item(String name, String texture) {
		return new ItemBuilder(MOD_ID)
			.setIcon(MOD_ID + ":item/" + texture)
			.build(new Item(name, ITEM_ID++));
	}


	// Materials
	public static @Nullable ArchitectMaterial getMaterial(@Nullable String id) {
		if (id == null) return null;
		return ArchitectMaterial.MATERIAL_REGISTRY.getItem(id);
	}

	public static @Nullable ArchitectMaterial getMaterial(@Nullable ItemStack item) {
		if (item == null) return null;
		for (ArchitectMaterial material : ArchitectMaterial.MATERIAL_REGISTRY) {
			if (material.isThisMaterial(item)) return material;
		}

		return null;
	}


	// Tool Parts
	public static @NotNull ItemStack createPartStack(@NotNull ArchitectMaterial material, ArchitectPart type) {
		return material.createPart(type);
	}

	public static @NotNull ItemStack createPartStack(@NotNull String material, @NotNull ArchitectPart part) throws InvalidMaterialException {
		ArchitectMaterial architectMaterial = getMaterial(material);
		if (architectMaterial == null) throw new InvalidMaterialException("Invalid material: " + material);
		return architectMaterial.createPart(part);
	}

	public static @NotNull ItemStack createPartStack(@NotNull ItemStack material, @NotNull ArchitectPart part) throws InvalidMaterialException {
		ArchitectMaterial architectMaterial = getMaterial(material);
		if (architectMaterial == null) throw new InvalidMaterialException("Invalid material: " + material);
		return architectMaterial.createPart(part);
	}

	public static @Nullable ArchitectMaterial getPartMaterial(@NotNull ItemStack item) {
		return getMaterial(ItemUtils.getArchitectCompound(item).getString("material"));
	}

	public static void setPartMaterial(@NotNull ItemStack item, @Nullable ArchitectMaterial material) {
		ItemUtils.getArchitectCompound(item).putString("material", material != null ? material.id() : null);
	}


	// Tools
	public static ItemStack createToolStack(@NotNull ArchitectTool tool, ItemStack... itemParts) throws ArchitectItemException {
		List<ToolPartInfo> expectedInfo = tool.getPartList();
		int expectedAmount = expectedInfo.size();
		if (itemParts.length != expectedAmount)
			throw new ArchitectItemException(String.format("Wrong amount of parts (expected %s, found %s).", expectedAmount, itemParts.length));

		ArchitectMaterial[] materials = new ArchitectMaterial[expectedAmount];
		for (int i = 0; i < expectedAmount; i++) {
			ArchitectPart expectedPart = expectedInfo.get(i).part();
			ItemStack partItem = itemParts[i];

			if (partItem == null) throw new ArchitectItemException("Tool parts can't be null.");
			if (!(partItem.getItem() instanceof ArchitectPart part))
				throw new ArchitectItemException(String.format("Tool parts must be ArchitectPart (%s).", partItem.getItem()));
			if (expectedPart != part)
				throw new ArchitectItemException(String.format("Invalid ArchitectPart (expected %s, found %s).", expectedPart, part));

			materials[i] = getPartMaterial(partItem);
		}

		return createToolStack(tool, materials);
	}

	public static ItemStack createToolStack(@NotNull ArchitectTool tool, ArchitectMaterial... materials) throws ArchitectItemException {
		int size = tool.getPartList().size();
		if (materials.length != size)
			throw new ArchitectItemException(String.format("Invalid material amount (expected %s, found %s).", size, materials.length));

		ItemStack item = new ItemStack(tool);

		for (int i = 0; i < materials.length; i++) {
			ArchitectMaterial material = materials[i];
			setToolPart(item, i, material);
		}

		return item;
	}

	public static void setToolPart(@NotNull ItemStack item, int index, @Nullable ArchitectMaterial material) {
		if (!(item.getItem() instanceof ArchitectTool tool))
			throw new IllegalArgumentException("Item must be ArchitectTool.");

		ToolPartInfo info = tool.getPartList().get(index);
		setToolPart(item, info.type(), info.partIndex(), material);
	}

	public static void setToolPart(@NotNull ItemStack item, @NotNull PartType part, @Nullable ArchitectMaterial material) {
		setToolPart(item, part, 0, material);
	}

	public static void setToolPart(@NotNull ItemStack item, @NotNull PartType part, int partIndex, @Nullable ArchitectMaterial material) {
		ListTag componentList = ItemUtils.getToolComponents(item, part);
		while (componentList.tagCount() <= partIndex) {
			componentList.addTag(new StringTag());
		}

		StringTag materialTag = (StringTag) componentList.tagAt(partIndex);
		materialTag.setValue(material != null ? material.id() : "");
	}

	public static @Nullable ArchitectMaterial getToolPart(@NotNull ItemStack item, @NotNull ToolPartInfo info) {
		return getToolPart(item, info.type(), info.partIndex());
	}

	public static @Nullable ArchitectMaterial getToolPart(@NotNull ItemStack item, @NotNull PartType part, int partIndex) {
		ListTag components = ItemUtils.getToolComponents(item, part);
		if (partIndex >= components.tagCount()) return null;

		StringTag materialTag = (StringTag) components.tagAt(partIndex);
		return getMaterial(materialTag.getValue());
	}

	public static List<Pair<@NotNull ToolPartInfo, @Nullable ArchitectMaterial>> getToolParts(@NotNull ItemStack item, @Nullable PartType type) {
		if (!(item.getItem() instanceof ArchitectTool tool))
			throw new ArchitectItemException("Item is not an ArchitectTool.");

		ArrayList<Pair<ToolPartInfo, ArchitectMaterial>> list = new ArrayList<>();
		for (ToolPartInfo info : tool.getPartList()) {
			if (type != null && info.type() != type) continue;
			list.add(Pair.of(info, getToolPart(item, info)));
		}

		return list;
	}

	public static void iterateToolParts(@NotNull ItemStack item, boolean renderOrder, @NotNull BiConsumer<@NotNull ToolPartInfo, @Nullable ArchitectMaterial> consumer) {
		if (!(item.getItem() instanceof ArchitectTool tool))
			throw new ArchitectItemException("Item is not an ArchitectTool.");

		for (ToolPartInfo info : (renderOrder ? tool.getRenderOrder() : tool.getPartList())) {
			consumer.accept(info, getToolPart(item, info.type(), info.index()));
		}
	}


	// Code
	@Override
	public void onInitialize() {
		EntityHelper.createTileEntity(ArchitectTableTileEntity.class, "architect_station");

		LOGGER.info("Architect's Tools initialized.");
	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {
		// Ungodly string fuckery to get textures to load
		LOGGER.info("Loading textures...");
		long start = System.currentTimeMillis();

		try {
			String path = "/assets/" + MOD_ID + "/textures/";
			URL resource = getClass().getResource(path + "item");
			if (resource == null) throw new IllegalStateException("Failed to find textures.");

			File file = Paths.get(resource.toURI()).toFile();
			Iterator<File> iterator = FileUtils.iterateFiles(file, null, true);
			while (iterator.hasNext()) {
				String path1 = iterator.next().getPath().replace('\\', '/');
				String cutPath = path1.split(path)[1];
				cutPath = cutPath.substring(0, cutPath.length() - 4);
				TextureRegistry.getTexture(MOD_ID + ":" + cutPath);
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		Minecraft.getMinecraft(Minecraft.class).renderEngine.refreshTextures(new ArrayList<>());
		LOGGER.info(String.format("Loaded textures (took %sms).", System.currentTimeMillis() - start));
	}


	@Override
	public void onRecipesReady() {
		RecipeBuilder.Shaped(MOD_ID, "xo", "ox")
			.addInput('x', Item.stick)
			.addInput('o', "minecraft:planks")
			.create("pattern", BLANK_PATTERN.getDefaultStack());
	}

	@Override
	public void initNamespaces() {

	}


}
