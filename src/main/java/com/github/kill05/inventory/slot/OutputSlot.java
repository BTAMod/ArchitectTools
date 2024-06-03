package com.github.kill05.inventory.slot;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.Nullable;

public abstract class OutputSlot extends Slot {

	public OutputSlot(int x, int y) {
		super(new Inventory(), 0, x, y);
		((Inventory) inventory).slot = this;
	}


	public abstract @Nullable ItemStack getOutput();


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


	private static class Inventory implements IInventory {

		private OutputSlot slot;

		@Override
		public int getSizeInventory() {
			return 1;
		}

		@Override
		public ItemStack getStackInSlot(int i) {
			return slot.getOutput();
		}

		@Override
		public ItemStack decrStackSize(int i, int j) {
			return slot.getOutput();
		}

		@Override
		public void setInventorySlotContents(int i, ItemStack itemStack) {

		}

		@Override
		public String getInvName() {
			return null;
		}

		@Override
		public int getInventoryStackLimit() {
			return 0;
		}

		@Override
		public void onInventoryChanged() {

		}

		@Override
		public boolean canInteractWith(EntityPlayer entityPlayer) {
			return true;
		}

		@Override
		public void sortInventory() {

		}
	}

}
