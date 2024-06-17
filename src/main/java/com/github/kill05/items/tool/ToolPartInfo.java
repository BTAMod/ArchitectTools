package com.github.kill05.items.tool;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.part.PartType;
import com.github.kill05.materials.ArchitectMaterial;
import com.github.kill05.materials.MaterialType;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.item.ItemStack;

import java.util.Objects;

public class ToolPartInfo {

	private final ArchitectPart part;
	private final PartType type;
	private final String texture;
	private final String brokenTexture;
	private int renderPriority;
	private int renderOffsetX;
	private int renderOffsetY;

	private ArchitectTool tool;
	private int index;
	private int partIndex;

	public ToolPartInfo(ArchitectPart part, PartType type, String texture, String brokenTexture) {
		if(!part.getValidTypes().contains(type))
			throw new IllegalArgumentException(String.format("Invalid type '%s' for part '%s'.", type, part.id));

		this.part = part;
		this.type = type;
		this.texture = texture;
		this.brokenTexture = brokenTexture;
		this.index = -1;
		this.partIndex = -1;
	}

	public ToolPartInfo(ArchitectPart part, PartType type, String texture) {
		this(part, type, texture, texture);
	}

	public void setTool(ArchitectTool tool) {
		if(this.tool != null) throw new IllegalStateException("Part already has a tool!");

		this.index = tool.getPartList().size();
		this.tool = tool;
		this.partIndex = 0;

		for (ToolPartInfo info : tool.getPartList()) {
			if(info.tool == tool) partIndex++;
		}

		ArchitectTools.LOGGER.debug(String.format("Added PartInfo to '%s' with part '%s', index '%s', part index '%s'.", tool.getToolId(), part.getPartId(), index, partIndex));
	}

	public IconCoordinate getIcon(ItemStack itemStack) {
		ArchitectMaterial material = ArchitectTools.getToolPart(itemStack, this);
		return MaterialType.getTexture(ArchitectTool.isToolBroken(itemStack) ? brokenTexture : texture, material);
	}

	public ArchitectPart part() {
		return part;
	}

	public PartType type() {
		return type;
	}

	public int index() {
		return index;
	}

	public int partIndex() {
		return partIndex;
	}


	public ToolPartInfo renderOffset(int x, int y) {
		this.renderOffsetX = x;
		this.renderOffsetY = y;
		return this;
	}

	public ToolPartInfo renderPriority(int priority) {
		this.renderPriority = priority;
		if(tool != null) tool.sortRenderOrder();
		return this;
	}

	public int renderOffsetX() {
		return renderOffsetX;
	}

	public int renderOffsetY() {
		return renderOffsetY;
	}

	public int renderPriority() {
		return renderPriority;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ToolPartInfo info = (ToolPartInfo) o;
		return index == info.index && Objects.equals(part, info.part);
	}

	@Override
	public int hashCode() {
		return Objects.hash(part, index);
	}


	@Override
	public String toString() {
		return "PartInfo[" +
			"part=" + part + ", " +
			"index=" + index + ", " +
			"offsetX=" + renderOffsetX + ", " +
			"offsetY=" + renderOffsetY + ']';
	}

}
