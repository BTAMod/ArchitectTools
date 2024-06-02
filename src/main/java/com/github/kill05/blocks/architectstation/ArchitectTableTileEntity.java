package com.github.kill05.blocks.architectstation;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.items.tool.ArchitectTool;
import com.github.kill05.utils.InventoryUtils;
import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryBasic;

public class ArchitectTableTileEntity extends TileEntity {

	public static final String PART_MODE = "part_mode";
	public static final String TOOL_MODE = "tool_mode";

	private ArchitectPart selectedPart;
	private ArchitectTool selectedTool;
	private final IInventory partInventory;
	private final IInventory toolInventory;

	public ArchitectTableTileEntity() {
		this.partInventory = new InventoryBasic("Part", 2);
		this.toolInventory = new InventoryBasic("Tool", ArchitectTools.MAX_TOOL_PARTS);
	}


	@Override
	public void readFromNBT(CompoundTag tag) {
		super.readFromNBT(tag);

		InventoryUtils.loadInventory(partInventory, tag.getList(PART_MODE));
		InventoryUtils.loadInventory(toolInventory, tag.getList(TOOL_MODE));
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		super.writeToNBT(tag);

		tag.putList(PART_MODE, InventoryUtils.writeInventory(partInventory));
		tag.putList(TOOL_MODE, InventoryUtils.writeInventory(toolInventory));
	}


	public IInventory getPartInventory() {
		return partInventory;
	}

	public IInventory getToolInventory() {
		return toolInventory;
	}

	public ArchitectPart getSelectedPart() {
		return selectedPart;
	}

	public ArchitectTool getSelectedTool() {
		return selectedTool;
	}

	public void setSelectedPart(ArchitectPart selectedPart) {
		this.selectedPart = selectedPart;
	}

	public void setSelectedTool(ArchitectTool selectedTool) {
		this.selectedTool = selectedTool;
	}
}
