package com.github.kill05.blocks.architectstation.part;

import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.inventory.container.TileContainer;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class PartModeContainer extends TileContainer {

	public PartModeContainer(ArchitectTableTileEntity tile, EntityPlayer player) {
		super(tile, tile.getPartInventory());

		addAlignedSlot(new PartModeInputSlot(tile.getPartInventory()), 5.5f, 2.5f);
		addAlignedSlot(new Slot(tile.getPartInventory(), 1, 0, 0), 5.5f, 4.5f);
		addAlignedSlot(new PartModeOutputSlot(tile), 7.5f, 3.5f);

		addPlayerInventory(player);
	}


	@Override
	public void handleHotbarSwap(int[] args, EntityPlayer player) {
		Slot slot = this.getSlot(args[0]);
		if(slot instanceof PartModeOutputSlot) return;

		super.handleHotbarSwap(args, player);
	}


	@Override
	public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
		return null;
	}

	@Override
	public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
		return null;
	}
}
