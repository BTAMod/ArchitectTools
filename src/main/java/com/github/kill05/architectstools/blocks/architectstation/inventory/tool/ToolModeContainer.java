package com.github.kill05.architectstools.blocks.architectstation.inventory.tool;

import com.github.kill05.architectstools.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.architectstools.blocks.architectstation.inventory.ArchitectStationContainer;
import com.github.kill05.architectstools.items.tool.ArchitectTool;
import com.github.kill05.architectstools.items.tool.ToolPartInfo;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class ToolModeContainer extends ArchitectStationContainer<ArchitectTool> {

	public ToolModeContainer(ArchitectTableTileEntity tile, EntityPlayer player) {
		super(tile, tile.getToolInventory());

		for(int i = 0; i < tile.getToolInventory().getSizeInventory(); i++) {
			addAlignedSlot(tile.getToolInventory(), i, 5.5f, 2.5f + i);
		}

		addAlignedSlot(new ToolModeOutputSlot(tile), 7.5f, 3.5f);
		addPlayerInventory(player);
	}

	@Override
	public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int j, EntityPlayer entityPlayer) {
		return null;
	}

	@Override
	public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int j, EntityPlayer entityPlayer) {
		if (slot.getInventory() == entityPlayer.inventory) {
			Item item = slot.getStack().getItem();
			ArchitectTool selectedTool = getTile().getSelectedTool();
			if(selectedTool == null) return null;

			List<ToolPartInfo> partList = selectedTool.getPartList();
			for (int i = 0; i < partList.size(); i++) {
				if(partList.get(i).part() == item) return List.of(i);
			}
		}

		return getSlots(4, 36, true);
	}

	@Override
	public List<ArchitectTool> getModeValues() {
		return ArchitectTool.VALUES;
	}

	@Override
	public void doSetSelected(ArchitectTool value) {
		getTile().setSelectedTool(value);
	}

	@Override
	public ArchitectTool getSelected() {
		return getTile().getSelectedTool();
	}
}
