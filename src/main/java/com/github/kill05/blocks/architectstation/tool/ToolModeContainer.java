package com.github.kill05.blocks.architectstation.tool;

import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.inventory.container.TileContainer;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class ToolModeContainer extends TileContainer {

	protected ToolModeContainer(ArchitectTableTileEntity tile, EntityPlayer player) {
		super(tile, tile.getToolInventory());

		for(int i = 0; i < tile.getToolInventory().getSizeInventory(); i++) {
			addAlignedSlot(tile.getToolInventory(), i, 5.5f, 2.5f + i);
		}

		addAlignedSlot(new ToolModeOutputSlot(tile), 7.5f, 3.5f);
		addPlayerInventory(player);
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
