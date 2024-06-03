package com.github.kill05.items.tool;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.ArchitectItem;
import com.github.kill05.items.model.ArchitectToolModel;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import com.github.kill05.items.part.statistics.PartStatistics;
import com.github.kill05.items.part.statistics.PartStatistic;
import com.github.kill05.materials.ArchitectMaterial;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import turniplabs.halplibe.helper.ItemBuilder;

import java.util.*;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class ArchitectTool extends Item implements ArchitectItem, ICustomDescription {

	public static final List<ArchitectTool> VALUES = new ArrayList<>();

	public static final ArchitectTool PICKAXE = toolItem(new ArchitectPickaxe(ArchitectTools.ITEM_ID++));
	public static final ArchitectTool AXE = toolItem(new ArchitectAxe(ArchitectTools.ITEM_ID++));
	public static final ArchitectTool SHOVEL = toolItem(new ArchitectShovel(ArchitectTools.ITEM_ID++));

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

	public ArchitectTool(int id, String toolId) {
		super(toolId + "_tool", id);
		this.validStatistics = new LinkedHashMap<>();
		this.partList = new ArrayList<>();
		this.renderOrder = new ArrayList<>();
		this.toolId = toolId;
		this.ordinal = VALUES.size();

		addValidStatistic(PartStatistic.DURABILITY);

		VALUES.add(this);
	}


	@Override
	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return getStatistic(itemstack, PartStatistic.MINING_SPEED);
	}

	//todo: replace once 7.2-pre2 comes out (need itemstack argument)
	@Override
	public int getDamageVsEntity(Entity entity) {
		return super.getDamageVsEntity(entity);
	}

	//todo: replace once 7.2-pre2 comes out (need itemstack argument)
	@Override
	public int getMaxDamage() {
		return super.getMaxDamage();
	}

	@Override
	public String getTranslatedName(ItemStack itemstack) {
		ArchitectMaterial part = ArchitectTools.getToolPart(itemstack, PartType.HEAD, 0);
		String name = part != null ? part.getTranslatedName() : "ERROR";
		return name + " " + I18n.getInstance().translateNameKey("tool." + ArchitectTools.MOD_ID + "." + getToolId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getDescription(ItemStack itemStack) {
		StringBuilder builder = new StringBuilder();

		Iterator<PartStatistic<?>> iterator = getValidStatistics().keySet().iterator();
		while (iterator.hasNext()) {
			PartStatistic<Number> stat = (PartStatistic<Number>) iterator.next();
			String formatted = stat.formatValue(itemStack, getStatistic(itemStack, stat));

			builder.append("§8");
			builder.append(stat.getTranslatedName());
			builder.append(": ");
			builder.append(formatted);
			if(iterator.hasNext()) builder.append("\n");
		}

		return builder.toString();
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


	protected @Nullable PartStatistics getHeadStats(ItemStack itemStack) {
		ArchitectMaterial toolPart = ArchitectTools.getToolPart(itemStack, PartType.HEAD, 0);
		if (toolPart == null) return null;
		return toolPart.getHeadStatistics();
	}

	public abstract boolean canHarvestBlock(ItemStack item, Block block);


	protected void addPart(ToolPartInfo part) {
		if (partList.size() >= ArchitectTools.MAX_TOOL_PARTS)
			throw new IllegalStateException(String.format("Too many tool parts! (max is %s).", ArchitectTools.MAX_TOOL_PARTS));

		part.setTool(this);
		partList.add(part);

		renderOrder.add(part);
		renderOrder.sort((o1, o2) -> o2.renderPriority() - o1.renderPriority());
	}

	protected void addPart(ArchitectPart part, PartType type, String texture) {
		addPart(new ToolPartInfo(part, type, texture));
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

