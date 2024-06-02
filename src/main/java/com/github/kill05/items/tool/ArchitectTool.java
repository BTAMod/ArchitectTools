package com.github.kill05.items.tool;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.ArchitectItem;
import com.github.kill05.items.model.ArchitectToolModel;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import com.github.kill05.items.part.info.HeadStatistics;
import com.github.kill05.materials.ArchitectMaterial;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class ArchitectTool extends Item implements ArchitectItem {

	public static final List<ArchitectTool> VALUES = new ArrayList<>();

	public static final ArchitectTool PICKAXE = toolItem(new ArchitectPickaxe(ArchitectTools.ITEM_ID++));
	public static final ArchitectTool AXE = toolItem(new ArchitectAxe(ArchitectTools.ITEM_ID++));

	public static ArchitectTool toolItem(ArchitectTool tool) {
		return new ItemBuilder(ArchitectTools.MOD_ID)
			.setItemModel(item -> new ArchitectToolModel((ArchitectTool) item))
			.build(tool);
	}

	private final String toolId;
	private final int ordinal;
	private final List<ToolPartInfo> partList;
	private final List<ToolPartInfo> renderOrder;

	public ArchitectTool(int id, String toolId) {
		super(toolId + "_tool", id);
		this.partList = new ArrayList<>();
		this.renderOrder = new ArrayList<>();
		this.toolId = toolId;
		this.ordinal = VALUES.size();
		VALUES.add(this);
	}


	@Override
	public float getStrVsBlock(ItemStack itemstack, Block block) {
		HeadStatistics headStats = getHeadStats(itemstack);
		return headStats != null ? headStats.getMiningSpeed() : 1f;
	}

	protected @Nullable HeadStatistics getHeadStats(ItemStack itemStack) {
		ArchitectMaterial toolPart = ArchitectTools.getToolPart(itemStack, PartType.HEAD, 0);
		if(toolPart == null) return null;
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

