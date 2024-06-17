package com.github.kill05.blocks.architectstation.inventory;

import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.inventory.container.TileContainer;
import com.github.kill05.items.IArchitectItem;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.player.inventory.IInventory;

import java.util.List;

public abstract class ArchitectStationContainer<T extends IArchitectItem> extends TileContainer {

	protected ArchitectStationContainer(TileEntity tile, IInventory inventory) {
		super(tile, inventory);
	}

	public abstract List<T> getModeValues();

	protected abstract void doSetSelected(T value);

	public abstract T getSelected();

	public void setSelected(T item) {
		boolean different = getSelected() != item;
		doSetSelected(different ? item : null);
	}

	@Override
	public ArchitectTableTileEntity getTile() {
		return (ArchitectTableTileEntity) super.getTile();
	}
}
