package com.github.kill05.blocks.architectstation.tool;

import com.github.kill05.ArchitectTools;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.exceptions.InvalidArchitectItemException;
import com.github.kill05.inventory.OutputInventory;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class ToolModeOutputSlot extends Slot {

	private final IInventory inventory;

	public ToolModeOutputSlot(ArchitectTableTileEntity tile) {
		super(new OutputSlotInventory(tile), 0, 0, 0);
		this.inventory = tile.getToolInventory();
	}

	@Override
	public boolean canPutStackInSlot(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean allowItemInteraction() {
		return false;
	}

	@Override
	public boolean enableDragAndPickup() {
		return false;
	}

	@Override
	public void onPickupFromSlot(ItemStack itemstack) {
		for(int i = 0; i < inventory.getSizeInventory(); i++) {
			inventory.decrStackSize(i, 1);
		}
	}

	private static class OutputSlotInventory extends OutputInventory {

		private final ArchitectTableTileEntity tile;

		private OutputSlotInventory(ArchitectTableTileEntity tile) {
			this.tile = tile;
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
			} catch (InvalidArchitectItemException ex) {
				return null;
			}
		}
	}
}
