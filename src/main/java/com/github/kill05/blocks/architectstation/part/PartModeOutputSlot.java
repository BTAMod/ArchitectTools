package com.github.kill05.blocks.architectstation.part;

import com.github.kill05.ArchitectTools;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.inventory.OutputInventory;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.materials.ArchitectMaterial;
import com.github.kill05.utils.ItemUtils;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class PartModeOutputSlot extends Slot {

	private final IInventory inv;

	public PartModeOutputSlot(ArchitectTableTileEntity tile) {
		super(new OutputSlotInventory(tile), 0, 0, 0);
		this.inv = tile.getPartInventory();
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
		inv.decrStackSize(0, 1);
		inv.decrStackSize(1, 1);
	}


	private static class OutputSlotInventory extends OutputInventory {

		private final ArchitectTableTileEntity tile;

		private OutputSlotInventory(ArchitectTableTileEntity tile) {
			this.tile = tile;
		}

		@Override
		public @Nullable ItemStack getOutput() {
			ArchitectPart part = tile.getSelectedPart();
			ItemStack materialItem = tile.getPartInventory().getStackInSlot(1);
			ArchitectMaterial material = materialItem != null ? ArchitectTools.getMaterial(materialItem) : null;

			boolean hasOutput =
				ItemUtils.compare(tile.getPartInventory().getStackInSlot(0), ArchitectTools.BLANK_PATTERN) &&
					part != null && material != null;

			return hasOutput ? material.createPart(part) : null;
		}
	}
}
