package com.github.kill05.blocks.architectstation.inventory.part;

import com.github.kill05.ArchitectTools;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.exceptions.ArchitectItemException;
import com.github.kill05.exceptions.InvalidMaterialException;
import com.github.kill05.inventory.slot.OutputSlot;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.materials.MaterialInfo;
import com.github.kill05.utils.ItemUtils;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import org.jetbrains.annotations.Nullable;

public class PartModeOutputSlot extends OutputSlot {

	private final ArchitectTableTileEntity tile;

	public PartModeOutputSlot(ArchitectTableTileEntity tile) {
		super(0, 0);
		this.tile = tile;
	}

	@Override
	public void onPickupFromSlot(ItemStack itemStack) {
		// Remove 1 pattern
		IInventory inv = tile.getPartInventory();
		inv.decrStackSize(0, 1);

		// Remove the correct amount of material
		MaterialInfo info = ArchitectTools.getMaterialInfo(inv.getStackInSlot(1));
		ArchitectPart part = (ArchitectPart) itemStack.getItem();
		inv.decrStackSize(1, part.getMaterialCost() / info.value());
	}

	@Override
	public @Nullable ItemStack getOutput() {
		if(tile.getSelectedPart() == null) return null;
		if(!ItemUtils.compare(tile.getPartInventory().getStackInSlot(0), ArchitectTools.BLANK_PATTERN)) return null;

		try {
			ItemStack materialItem = tile.getPartInventory().getStackInSlot(1);
			return ArchitectTools.createPartStack(materialItem, tile.getSelectedPart());
		} catch (InvalidMaterialException | ArchitectItemException e) {
			return null;
		}
	}
}
