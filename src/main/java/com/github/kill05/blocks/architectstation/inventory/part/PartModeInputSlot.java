package com.github.kill05.blocks.architectstation.inventory.part;

import com.github.kill05.ArchitectTools;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class PartModeInputSlot extends Slot {

	public PartModeInputSlot(IInventory patternInv) {
		super(patternInv, 0, 0, 0);
	}

	@Override
	public boolean canPutStackInSlot(ItemStack itemstack) {
		return itemstack.itemID == ArchitectTools.BLANK_PATTERN.id;
	}


}
