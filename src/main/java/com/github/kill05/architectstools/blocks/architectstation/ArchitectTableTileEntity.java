package com.github.kill05.architectstools.blocks.architectstation;

import com.github.kill05.architectstools.ArchitectTools;
import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.items.tool.ArchitectTool;
import com.github.kill05.architectstools.utils.InventoryUtils;
import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryBasic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
		this.selectedPart = ArchitectPart.fromIndex(tag.getInteger("selected_part"));
		this.selectedTool = ArchitectTool.fromIndex(tag.getInteger("selected_tool"));
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		super.writeToNBT(tag);

		tag.putList(PART_MODE, InventoryUtils.writeInventory(partInventory));
		tag.putList(TOOL_MODE, InventoryUtils.writeInventory(toolInventory));
		tag.putInt("selected_part", selectedPart != null ? selectedPart.ordinal() : -1);
		tag.putInt("selected_tool", selectedTool != null ? selectedTool.ordinal() : -1);
	}

	@Override
	public Packet getDescriptionPacket() {
		return null; //new Packet140TileEntityData(this);
	}

	public @NotNull IInventory getPartInventory() {
		return partInventory;
	}

	public @NotNull IInventory getToolInventory() {
		return toolInventory;
	}

	public @Nullable ArchitectPart getSelectedPart() {
		return selectedPart;
	}

	public @Nullable ArchitectTool getSelectedTool() {
		return selectedTool;
	}

	public void setSelectedPart(ArchitectPart selectedPart) {
		this.selectedPart = selectedPart;
	}

	public void setSelectedTool(ArchitectTool selectedTool) {
		this.selectedTool = selectedTool;
	}
}
