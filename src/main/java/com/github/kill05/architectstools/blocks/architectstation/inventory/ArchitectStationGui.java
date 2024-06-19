package com.github.kill05.architectstools.blocks.architectstation.inventory;

import com.github.kill05.architectstools.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.architectstools.items.IArchitectItem;
import com.github.kill05.architectstools.blocks.architectstation.ArchitectTableButton;
import com.github.kill05.architectstools.inventory.button.ItemTexturedButton;
import com.github.kill05.architectstools.inventory.container.TileContainer;
import com.github.kill05.architectstools.inventory.gui.TileContainerGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

public abstract class ArchitectStationGui<T extends IArchitectItem> extends TileContainerGui {

	protected final EntityPlayer player;

	public ArchitectStationGui(TileContainer container, EntityPlayer player) {
		super(container);
		this.player = player;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	public void init() {
		super.init();

		ArchitectTableTileEntity tile = getTile();
		ArchitectStationContainer<T> container = getContainer();
		int i = 0;

		for (T item : container.getModeValues()) {
			int x = (i % 4) * 18 + 17;
			int y = (i / 4) * 18 + 18;
			i++;

			add(new ArchitectTableButton(tile, item, x, y));
		}

		GuiButton button = new ItemTexturedButton(new ItemStack(Item.ammoArrow), 0, 0);
		addAlignedButton(button, 7.5f, 6f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArchitectStationContainer<T> getContainer() {
		return (ArchitectStationContainer<T>) super.getContainer();
	}

	public ArchitectTableTileEntity getTile() {
		return getContainer().getTile();
	}
}
