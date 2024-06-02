package com.github.kill05.inventory.container;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public abstract class TileContainer extends Container {

	public static final int SLOT_SIZE = 18;

	protected final TileEntity tile;
	protected final IInventory inventory;
	private final int alignOffsetX;
	private final int alignOffsetY;

	protected TileContainer(TileEntity tile, IInventory inventory, int alignOffsetX, int alignOffsetY) {
		this.tile = tile;
		this.inventory = inventory;
		this.alignOffsetX = alignOffsetX;
		this.alignOffsetY = alignOffsetY;
	}

	protected TileContainer(TileEntity tile, IInventory inventory) {
		this(tile, inventory, 0, 0);
	}


	@Override
	public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
		return inventory.canInteractWith(entityPlayer);
	}

	// Call this after creating all the gui slots, else hotkey inventory actions get messed up
	public void addPlayerInventory(EntityPlayer player) {
		addPlayerInventory(player, 140);
	}

	public void addPlayerInventory(EntityPlayer player, int offset) {
		IInventory playerInv = player.inventory;
		for (int slotY = 0; slotY < 3; ++slotY) {
			for (int slotX = 0; slotX < 9; ++slotX) {
				this.addSlot(new Slot(playerInv, slotY * 9 + slotX + 9, alignX(slotX), alignY(slotY) + offset));
			}
		}

		for (int slotX = 0; slotX < 9; ++slotX) {
			this.addSlot(new Slot(playerInv, slotX, alignX(slotX), alignY(3f) + 4 + offset));
		}
	}


	public void addAlignedSlot(IInventory inv, int id, float x, float y) {
		addSlot(new Slot(inv, id, alignX(x), alignY(y)));
	}

	public void addAlignedSlot(Slot slot, float x, float y) {
		slot.xDisplayPosition = alignX(x);
		slot.yDisplayPosition = alignY(y);
		addSlot(slot);
	}

	public int alignX(float x) {
		return (int) (x * SLOT_SIZE + 8) + alignOffsetX;
	}

	public int alignY(float y) {
		return (int) (y * SLOT_SIZE) + alignOffsetY;
	}

	public TileEntity getTile() {
		return tile;
	}

	public int getAlignOffsetX() {
		return alignOffsetX;
	}

	public int getAlignOffsetY() {
		return alignOffsetY;
	}

	public IInventory getInventory() {
		return inventory;
	}
}
