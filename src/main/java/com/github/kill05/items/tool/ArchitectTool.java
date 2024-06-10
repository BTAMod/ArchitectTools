package com.github.kill05.items.tool;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.ArchitectItem;
import com.github.kill05.items.model.ArchitectToolModel;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import com.github.kill05.items.part.statistics.PartStatistic;
import com.github.kill05.items.part.statistics.PartStatistics;
import com.github.kill05.materials.ArchitectMaterial;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import turniplabs.halplibe.helper.ItemBuilder;

import java.util.*;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class ArchitectTool extends Item implements ArchitectItem, ICustomDescription {

	public static final List<ArchitectTool> VALUES = new ArrayList<>();

	public static final ArchitectTool PICKAXE = toolItem(new ArchitectPickaxe(ArchitectTools.ITEM_ID++));
	public static final ArchitectTool AXE = toolItem(new ArchitectAxe(ArchitectTools.ITEM_ID++));
	public static final ArchitectTool SHOVEL = toolItem(new ArchitectShovel(ArchitectTools.ITEM_ID++));
	public static final ArchitectTool MATTOCK = toolItem(new ArchitectMattock(ArchitectTools.ITEM_ID++));
	public static final ArchitectTool SHORTSWORD = toolItem(new ArchitectShortsword(ArchitectTools.ITEM_ID++));

	public static ArchitectTool toolItem(ArchitectTool tool) {
		return new ItemBuilder(ArchitectTools.MOD_ID)
			.setItemModel(item -> new ArchitectToolModel((ArchitectTool) item))
			.build(tool);
	}

	private final String toolId;
	private final int ordinal;
	private final Map<PartStatistic<?>, Float> validStatistics; //value is multiplier
	private final List<ToolPartInfo> partList;
	private final List<ToolPartInfo> renderOrder;
	private final Collection<Tag<Block>> mineableTags;

	public ArchitectTool(int id, String toolId) {
		super(toolId + "_tool", id);
		this.validStatistics = new LinkedHashMap<>();
		this.partList = new ArrayList<>();
		this.renderOrder = new ArrayList<>();
		this.mineableTags = new HashSet<>();
		this.toolId = toolId;
		this.ordinal = VALUES.size();

		addValidStatistic(PartStatistic.DURABILITY);

		VALUES.add(this);
	}


	//todo: remove once 7.2-pre2 comes out (need itemstack argument)
	@Override
	public boolean canHarvestBlock(Block block) {
		throw new UnsupportedOperationException("Use canHarvestBlock(ItemStack, Block) instead.");
	}

	//todo: replace once 7.2-pre2 comes out (need itemstack argument)
	@Override
	public int getDamageVsEntity(Entity entity) {
		throw new UnsupportedOperationException("use getDamageVsEntity(Entity, ItemStack) instead.");
	}

	//todo: replace once 7.2-pre2 comes out (need itemstack argument)
	@Override
	public int getMaxDamage() {
		throw new UnsupportedOperationException("Use getMaxDamage(ItemStack) instead.");
	}

	@Override
	public float getStrVsBlock(ItemStack itemStack, Block block) {
		return canHarvestBlock(itemStack, block) ? getStatistic(itemStack, PartStatistic.MINING_SPEED) : super.getStrVsBlock(itemStack, block);
	}

	//todo: replace once 7.2-pre2 comes out (need itemstack argument)
	public boolean canHarvestBlock(ItemStack itemStack, Block block) {
		for (Tag<Block> mineableTag : mineableTags) {
			if(block.hasTag(mineableTag)) return true;
		}

		return false;
	}

	public int getDamageVsEntity(Entity entity, ItemStack itemStack) {
		return getStatistic(itemStack, PartStatistic.ENTITY_DAMAGE).intValue();
	}

	public int getMaxDamage(ItemStack itemStack) {
		return getStatistic(itemStack, PartStatistic.DURABILITY);
	}



	@Override
	public boolean onBlockDestroyed(World world, ItemStack itemstack, int blockId, int x, int y, int z, EntityLiving entityliving) {
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
		if (validStatistics.containsKey(type))
			throw new IllegalStateException(String.format("State '%s' has already been added!", type.getId()));

		validStatistics.put(type, multiplier);
	}

	public <V extends Number> V getStatistic(ItemStack item, PartStatistic<V> statistic) {
		Float mult = validStatistics.get(statistic);
		if(mult == null) return statistic.getNoneValue();

		V value = statistic.getNoneValue();
		for (ToolPartInfo info : getPartList()) {
			ArchitectMaterial part = ArchitectTools.getToolPart(item, info);
			if(part == null) continue;

			PartStatistics statistics = part.getStatistics(info.type());
			if(statistics == null) continue;

			value = statistic.sumValue(value, statistics.getStatistic(statistic));
		}

		return statistic.multiplyValue(value, mult);
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

	protected ToolPartInfo addPart(ArchitectPart part, PartType type, String texture) {
		return addPart(new ToolPartInfo(part, type, texture));
	}

	protected ToolPartInfo addPart(ArchitectPart part, String texture) {
		return addPart(part, part.getValidTypes().get(0), texture);
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

	public int ordinal() {
		return ordinal;
	}

}

