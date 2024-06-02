package com.github.kill05.utils;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;

public final class InventoryUtils {

	public static ListTag writeInventory(IInventory inv) {
		return writeInventory(inv, new ListTag());
	}

	public static ListTag writeInventory(IInventory inv, ListTag list) {
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack item = inv.getStackInSlot(i);
			if (item == null) continue;

			CompoundTag itemTag = new CompoundTag();
			itemTag.putByte("Slot", (byte)i);
			item.writeToNBT(itemTag);

			list.addTag(itemTag);
		}

		return list;
	}

	@SuppressWarnings("UnusedReturnValue")
	public static IInventory loadInventory(IInventory inv, ListTag list) {
		for (Tag<?> tag : list) {
			CompoundTag itemTag = (CompoundTag) tag;
			int slot = itemTag.getByte("Slot") & 0xFF;
			if (slot >= inv.getSizeInventory()) continue;
			inv.setInventorySlotContents(slot, ItemStack.readItemStackFromNbt(itemTag));
		}

		return inv;
	}

}
