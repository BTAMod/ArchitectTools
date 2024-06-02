package com.github.kill05.inventory;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import org.jetbrains.annotations.Nullable;

public abstract class OutputInventory implements IInventory {


	public abstract @Nullable ItemStack getOutput();


	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return getOutput();
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return getStackInSlot(i);
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
		return 1;
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
