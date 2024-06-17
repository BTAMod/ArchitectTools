package com.github.kill05.blocks.architectstation.inventory.tool;

import com.github.kill05.ArchitectTools;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.exceptions.ArchitectItemException;
import com.github.kill05.inventory.slot.OutputSlot;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import org.jetbrains.annotations.Nullable;

public class ToolModeOutputSlot extends OutputSlot {

	private final ArchitectTableTileEntity tile;

	public ToolModeOutputSlot(ArchitectTableTileEntity tile) {
		super(0, 0);
		this.tile = tile;
	}


	@Override
	public void onPickupFromSlot(ItemStack itemstack) {
		for(int i = 0; i < tile.getToolInventory().getSizeInventory(); i++) {
			tile.getToolInventory().decrStackSize(i, 1);
		}
	}

	@Override
	public @Nullable ItemStack getOutput() {
		if(tile.getSelectedTool() == null) return null;

		IInventory inventory = tile.getToolInventory();
		ItemStack[] parts = new ItemStack[inventory.getSizeInventory()];

		for(int i = 0; i < parts.length; i++) {
			parts[i] = inventory.getStackInSlot(i);
		}

		try {
			return ArchitectTools.createToolStack(tile.getSelectedTool(), parts);
		} catch (ArchitectItemException ex) {
			return null;
		}
	}
}
