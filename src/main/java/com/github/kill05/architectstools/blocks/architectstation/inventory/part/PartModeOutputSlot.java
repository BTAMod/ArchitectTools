package com.github.kill05.architectstools.blocks.architectstation.inventory.part;

import com.github.kill05.architectstools.ArchitectTools;
import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.architectstools.exceptions.ArchitectItemException;
import com.github.kill05.architectstools.exceptions.InvalidMaterialException;
import com.github.kill05.architectstools.inventory.slot.OutputSlot;
import com.github.kill05.architectstools.materials.MaterialInfo;
import com.github.kill05.architectstools.utils.ItemUtils;
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
		IInventory inv = tile.getPartInventory();
		ArchitectPart part = tile.getSelectedPart();
		if(part == null) return;

		// Remove 1 pattern
		if(part != ArchitectPart.REPAIR_KIT) inv.decrStackSize(0, 1);

		// Remove the correct amount of material
		MaterialInfo info = ArchitectTools.getMaterialInfo(inv.getStackInSlot(1));
		inv.decrStackSize(1, part.getMaterialCost() / info.value());
	}

	@Override
	public @Nullable ItemStack getOutput() {
		ArchitectPart part = tile.getSelectedPart();
		ItemStack patternStack = tile.getPartInventory().getStackInSlot(0);
		if(part == null) return null;
		if(part != ArchitectPart.REPAIR_KIT && !ItemUtils.compare(patternStack, ArchitectTools.BLANK_PATTERN)) return null;

		try {
			ItemStack materialItem = tile.getPartInventory().getStackInSlot(1);
			return ArchitectTools.createPartStack(materialItem, part);
		} catch (InvalidMaterialException | ArchitectItemException e) {
			return null;
		}
	}
}
