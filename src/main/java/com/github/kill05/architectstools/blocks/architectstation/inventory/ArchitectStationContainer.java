package com.github.kill05.architectstools.blocks.architectstation.inventory;

import com.github.kill05.architectstools.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.architectstools.items.IArchitectItem;
import com.github.kill05.architectstools.inventory.container.TileContainer;
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
