package com.github.kill05.architectstools.items.tool;

import com.github.kill05.architectstools.ArchitectTools;
import com.github.kill05.architectstools.config.ArchitectConfig;
import com.github.kill05.architectstools.items.IArchitectItem;
import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.items.part.PartType;
import com.github.kill05.architectstools.materials.ArchitectMaterial;
import com.github.kill05.architectstools.items.model.ArchitectToolModel;
import com.github.kill05.architectstools.items.part.statistics.PartStatistic;
import com.github.kill05.architectstools.items.part.statistics.PartStatistics;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import turniplabs.halplibe.helper.ItemBuilder;

import java.util.*;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class ArchitectTool extends Item implements IArchitectItem, ICustomDescription {

	public static final List<ArchitectTool> VALUES = new ArrayList<>();

	public static final ArchitectTool PICKAXE = toolItem(new ArchitectPickaxe());
	public static final ArchitectTool AXE = toolItem(new ArchitectAxe());
	public static final ArchitectTool SHOVEL = toolItem(new ArchitectShovel());
	public static final ArchitectTool MATTOCK = toolItem(new ArchitectMattock());
	public static final ArchitectTool SHORTSWORD = toolItem(new ArchitectShortsword());

	public static ArchitectTool toolItem(ArchitectTool tool) {
		return new ItemBuilder(ArchitectTools.MOD_ID)
			.setItemModel(item -> new ArchitectToolModel((ArchitectTool) item))
			.build(tool);
	}

	public static ArchitectTool fromIndex(int index) {
		if(index < 0 || index >= VALUES.size()) return null;
		return VALUES.get(index);
	}

	private final String toolId;
	private final int ordinal;
	private final Map<PartStatistic<?>, Float> validStatistics; //value is multiplier
	private final List<ToolPartInfo> partList;
	private final List<ToolPartInfo> renderOrder;
	private final Collection<Tag<Block>> mineableTags;

	public ArchitectTool(String toolId) {
		super(toolId + "_tool", ArchitectConfig.TOOL_ID++);
		this.validStatistics = new LinkedHashMap<>();
		this.partList = new ArrayList<>();
		this.renderOrder = new ArrayList<>();
		this.mineableTags = new HashSet<>();
		this.toolId = toolId;
		this.ordinal = VALUES.size();

		setMaxStackSize(1);
		addValidStatistic(PartStatistic.DURABILITY);
		//noinspection unchecked
		withTags(ItemTags.NOT_IN_CREATIVE_MENU);

		VALUES.add(this);
	}


	public static boolean isToolBroken(ItemStack item) {
		return item.getMetadata() >= item.getMaxDamage();
	}


	@Override
	public boolean canHarvestBlock(EntityLiving mob, ItemStack item, Block block) {
		if(isToolBroken(item)) return false;

		for (Tag<Block> mineableTag : mineableTags) {
			if(block.hasTag(mineableTag)) return true;
		}

		return false;
	}

	//todo: replace once 7.3 comes out (need itemstack argument)
	@Override
	public int getDamageVsEntity(Entity entity) {
		ArchitectTools.LOGGER.warn("getDamageVsEntity(Entity) was called on an ArchitectTool. getDamageVsEntity(Entity, Itemstack) should be used instead.");
		return super.getDamageVsEntity(entity);
	}

	//todo: replace once 7.3 comes out (need itemstack argument)
	@Override
	public int getMaxDamage() {
		ArchitectTools.LOGGER.warn("getMaxDamage() was called on an ArchitectTool. getMaxDamage(ItemStack) should be used instead.");
		return super.getMaxDamage();
	}

	@Override
	public float getStrVsBlock(ItemStack itemStack, Block block) {
		if(isToolBroken(itemStack)) return super.getStrVsBlock(itemStack, block);
		return canHarvestBlock(null, itemStack, block) ? getStatistic(itemStack, PartStatistic.MINING_SPEED) : super.getStrVsBlock(itemStack, block);
	}

	public int getDamageVsEntity(Entity entity, ItemStack itemStack) {
		if(isToolBroken(itemStack)) return super.getDamageVsEntity(entity);
		return getStatistic(itemStack, PartStatistic.ENTITY_DAMAGE).intValue();
	}

	public int getMaxDamage(ItemStack itemStack) {
		return getStatistic(itemStack, PartStatistic.DURABILITY);
	}


	@Override
	public boolean onBlockDestroyed(World world, ItemStack itemstack, int blockId, int x, int y, int z, Side side, EntityLiving entityliving) {
		Block block = Block.blocksList[blockId];
		if (block != null && (block.getHardness() > 0.0F || this.isSilkTouch())) {
			itemstack.damageItem(1, entityliving);
		}

		return true;
	}

	@Override
	public String getTranslatedName(ItemStack itemstack) {
		ArchitectMaterial part = ArchitectTools.getToolPart(itemstack, PartType.HEAD, 0);
		String material = part != null ? part.getTranslatedName() : "ERROR";
		return String.format(super.getTranslatedName(itemstack), material);
	}

	@Override
	public String getDescription(ItemStack itemStack) {
		StringBuilder builder = new StringBuilder();

		Iterator<PartStatistic<?>> iterator = getValidStatistics().keySet().iterator();
		while (iterator.hasNext()) {
			PartStatistic<?> stat = iterator.next();
			String formatted = stat.formatToolValue(itemStack, getStatistic(itemStack, stat));

			builder.append("§8");
			builder.append(stat.getTranslatedName());
			builder.append(": ");
			builder.append(formatted);
			if(iterator.hasNext()) builder.append('\n');
		}

		return builder.append('\n').toString();
	}


	protected void addValidStatistic(PartStatistic<?> type) {
		addValidStatistic(type, 1.0f);
	}

	protected void addValidStatistic(PartStatistic<?> type, float multiplier) {
		validStatistics.put(type, multiplier);
	}

	public <V extends Number> V getStatistic(ItemStack item, PartStatistic<V> statistic) {
		Float mult = validStatistics.get(statistic);
		if(mult == null) return statistic.getMinValue();

		V value = statistic.getMinValue();
		for (ToolPartInfo info : getPartList()) {
			ArchitectMaterial part = ArchitectTools.getToolPart(item, info);
			if(part == null) continue;

			PartStatistics statistics = part.getStatistics(info.type());
			if(statistics == null) continue;

			value = statistic.sumValue(value, statistics.getStatistic(statistic));
		}

		return statistic.max(statistic.multiplyValue(value, mult), statistic.getMinValue());
	}


	protected void sortRenderOrder() {
		renderOrder.sort((o1, o2) -> o2.renderPriority() - o1.renderPriority());
	}

	protected ToolPartInfo addPart(ToolPartInfo part) {
		if (partList.size() >= ArchitectTools.MAX_TOOL_PARTS)
			throw new IllegalStateException(String.format("Too many tool parts! (max is %s).", ArchitectTools.MAX_TOOL_PARTS));

		part.setTool(this);
		partList.add(part);
		renderOrder.add(part);
		sortRenderOrder();

		return part;
	}

	protected ToolPartInfo addPart(ArchitectPart part, PartType type, String texture, String brokenTexture) {
		return addPart(new ToolPartInfo(part, type, texture, brokenTexture));
	}

	protected ToolPartInfo addPart(ArchitectPart part, PartType type, String texture) {
		return addPart(part, type, texture, texture);
	}

	protected ToolPartInfo addPart(ArchitectPart part, String texture, String brokenTexture) {
		return addPart(part, part.getValidTypes().get(0), texture, brokenTexture);
	}

	protected ToolPartInfo addPart(ArchitectPart part, String texture) {
		return addPart(part, texture, texture);
	}

	@SafeVarargs
	protected final void addMineableTags(Tag<Block>... tags) {
		mineableTags.addAll(List.of(tags));
	}


	public List<ToolPartInfo> getPartList() {
		return partList;
	}

	public List<ToolPartInfo> getRenderOrder() {
		return renderOrder;
	}

	public Map<PartStatistic<?>, Float> getValidStatistics() {
		return validStatistics;
	}

	@Override
	public boolean renderPattern() {
		return false;
	}


	public String getToolId() {
		return toolId;
	}

	@Override
	public int ordinal() {
		return ordinal;
	}

}

