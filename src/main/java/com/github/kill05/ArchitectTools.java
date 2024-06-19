package com.github.kill05;

import com.github.kill05.blocks.architectstation.ArchitectTableBlock;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.config.ArchitectConfig;
import com.github.kill05.exceptions.ArchitectItemException;
import com.github.kill05.exceptions.InvalidMaterialException;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import com.github.kill05.items.tool.ArchitectTool;
import com.github.kill05.items.tool.ToolPartInfo;
import com.github.kill05.materials.ArchitectMaterial;
import com.github.kill05.materials.MaterialInfo;
import com.github.kill05.recipe.RecipeEntryRepairTool;
import com.github.kill05.utils.ClassUtils;
import com.github.kill05.utils.ItemUtils;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.StringTag;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;


public final class ArchitectTools implements ModInitializer, RecipeEntrypoint, ClientStartEntrypoint {

	public static final String MOD_ID = "architectstools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int MAX_TOOL_PARTS = 3;

	public static final RecipeNamespace RECIPE_NAMESPACE = new RecipeNamespace();
	public static final RecipeGroup<RecipeEntryCrafting<?, ?>> RECIPE_WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.workbench)));


	// Items and Blocks
	public static final Item BLANK_PATTERN = item("blank_pattern", "blank_pattern");

	public static final Block ARCHITECT_TABLE_BLOCK = new BlockBuilder(MOD_ID)
		.setTopTexture(MOD_ID + ":block/architect_table_top")
		.setSideTextures(MOD_ID + ":block/architect_table_side")
		.setBottomTexture("minecraft" + ":block/planks_oak")
		.setHardness(2.5F)
		.setResistance(5.0F)
		.setFlammability(10, 10)
		.setBlockSound(BlockSounds.WOOD)
		.setTags(BlockTags.MINEABLE_BY_AXE)
		.build(new ArchitectTableBlock());


	public static <T extends Item> T item(T item, String texture) {
		return new ItemBuilder(MOD_ID)
			.setIcon(MOD_ID + ":item/" + texture)
			.build(item);
	}

	public static Item item(String name, String texture) {
		return new ItemBuilder(MOD_ID)
			.setIcon(MOD_ID + ":item/" + texture)
			.build(new Item(name, ArchitectConfig.ITEM_ID++));
	}

	// Materials
	public static @Nullable ArchitectMaterial getMaterial(@Nullable String id) {
		if (id == null) return null;
		return ArchitectMaterial.MATERIAL_REGISTRY.getItem(id);
	}

	public static @NotNull MaterialInfo getMaterialInfo(@Nullable ItemStack item) {
		if (item == null) return new MaterialInfo(null, 0);
		for (ArchitectMaterial material : ArchitectMaterial.MATERIAL_REGISTRY) {
			Integer value = material.getMaterialValue(item);
			if (value != null) return new MaterialInfo(material, value);
		}

		return new MaterialInfo(null, 0);
	}


	// Tool Parts
	public static @NotNull ItemStack createPartStack(@NotNull ArchitectMaterial material, ArchitectPart part) throws ArchitectItemException {
		if (!material.isValidPart(part))
			throw new ArchitectItemException(String.format("Part '%s' can't be made out of material '%s'", part.getPartId(), material.id()));

		ItemStack item = new ItemStack(part);
		setPartMaterial(item, material);
		return item;
	}

	public static @NotNull ItemStack createPartStack(@NotNull String materialName, @NotNull ArchitectPart part) throws InvalidMaterialException, ArchitectItemException {
		ArchitectMaterial material = getMaterial(materialName);
		if (material == null) throw new InvalidMaterialException("Invalid material: " + materialName);
		return createPartStack(material, part);
	}

	public static @NotNull ItemStack createPartStack(@NotNull ItemStack materialItem, @NotNull ArchitectPart part) throws InvalidMaterialException, ArchitectItemException {
		MaterialInfo info = getMaterialInfo(materialItem);
		if (info.material() == null) throw new InvalidMaterialException("Invalid material: " + materialItem);
		if (info.value() * materialItem.stackSize < part.getMaterialCost()) throw new InvalidMaterialException("Not enough material.");
		return createPartStack(info.material(), part);
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

	public static Map<@NotNull ToolPartInfo, @Nullable ArchitectMaterial> getToolParts(@NotNull ItemStack item, @Nullable PartType type) {
		if (!(item.getItem() instanceof ArchitectTool tool))
			throw new ArchitectItemException("Item is not an ArchitectTool.");

		Map<ToolPartInfo, ArchitectMaterial> map = new LinkedHashMap<>();
		for (ToolPartInfo info : tool.getPartList()) {
			if (type != null && info.type() != type) continue;
			map.put(info, getToolPart(item, info));
		}

		return map;
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
		// Register materials and items
		ClassUtils.initializeClasses(
			ArchitectMaterial.class,
			ArchitectPart.class,
			ArchitectTool.class
		);

		ItemStack parent = BLANK_PATTERN.getDefaultStack();
		for (ArchitectMaterial material : ArchitectMaterial.MATERIAL_REGISTRY) {
			for (ArchitectPart part : ArchitectPart.VALUES) {
				try {
					CreativeHelper.setParent(ArchitectTools.createPartStack(material, part), parent);
				} catch (ArchitectItemException ignored) { // Some parts can't be made out of certain materials
				}
			}
		}

		EntityHelper.createTileEntity(ArchitectTableTileEntity.class, "architect_station");

		LOGGER.info("Architect's Tools initialized.");
	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {
		// This is awful, but required until 7.2-pre2 comes out
		LOGGER.info("Loading textures...");
		long start = System.currentTimeMillis();

		String path = String.format("%s/%s/%s", "/assets", MOD_ID, TextureRegistry.itemAtlas.directoryPath);
		URI uri;
		try {
			uri = DataLoader.class.getResource(path).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		FileSystem fileSystem = null;
		Path myPath;
		if (uri.getScheme().equals("jar")) {
			try {
				fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			myPath = fileSystem.getPath(path);
		} else {
			myPath = Paths.get(uri);
		}

		Stream<Path> walk;
		try {
			walk = Files.walk(myPath, Integer.MAX_VALUE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Iterator<Path> it = walk.iterator();

		while (it.hasNext()) {
			Path file = it.next();
			String name = file.getFileName().toString();
			if (name.endsWith(".png")) {
				String path1 = file.toString().replace(file.getFileSystem().getSeparator(), "/");
				String cutPath = path1.split(path)[1];
				cutPath = cutPath.substring(0, cutPath.length() - 4);
				TextureRegistry.getTexture(MOD_ID + ":item" + cutPath);
			}
		}

		walk.close();
		if (fileSystem != null) {
			try {
				fileSystem.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		try {
			TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.itemAtlas);
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException("Failed to load textures.", e);
		}

		Minecraft.getMinecraft(Minecraft.class).renderEngine.refreshTextures(new ArrayList<>());
		LOGGER.info(String.format("Loaded textures (took %sms).", System.currentTimeMillis() - start));
	}


	@Override
	public void onRecipesReady() {
		ArchitectMaterial.lock();
		for (ArchitectMaterial material : ArchitectMaterial.MATERIAL_REGISTRY) {
			material.initGroups();
		}

		RecipeBuilder.Shaped(MOD_ID, "xo", "ox")
			.addInput('x', Item.stick)
			.addInput('o', "minecraft:planks")
			.create("pattern", BLANK_PATTERN.getDefaultStack());

		RecipeBuilder.Shaped(MOD_ID, "pbp", "lwl", "pcp")
			.addInput('p', "minecraft:planks")
			.addInput('l', "minecraft:logs")
			.addInput('c', "minecraft:chests")
			.addInput('b', BLANK_PATTERN)
			.addInput('w', Block.workbench)
			.create("architect_table", ARCHITECT_TABLE_BLOCK.getDefaultStack());

		Registries.RECIPES.addCustomRecipe(MOD_ID + ":workbench/repair_tool", new RecipeEntryRepairTool());
	}

	@Override
	public void initNamespaces() {
		Registries.RECIPES.register(MOD_ID, RECIPE_NAMESPACE);
		RECIPE_NAMESPACE.register("workbench", RECIPE_WORKBENCH);
	}

}
