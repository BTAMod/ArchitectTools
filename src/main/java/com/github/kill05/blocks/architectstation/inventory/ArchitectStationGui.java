package com.github.kill05.blocks.architectstation.inventory;

import com.github.kill05.blocks.architectstation.ArchitectTableButton;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.inventory.button.ItemTexturedButton;
import com.github.kill05.inventory.container.TileContainer;
import com.github.kill05.inventory.gui.TileContainerGui;
import com.github.kill05.items.IArchitectItem;
import com.github.kill05.packet.PacketSetTableOutput;
import net.minecraft.client.entity.player.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

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

			add(new ArchitectTableButton(tile, item, x, y)).setListener(button -> {
				container.setSelected(item);

				// Client to server (tell the server a client has changed the selected part/tool)
				// The check is required in case the client is playing in singleplayer
				if(player instanceof EntityClientPlayerMP playerMP) {
					playerMP.sendQueue.addToSendQueue(new PacketSetTableOutput(container.getSelected()));
				}
			});
		}

		GuiButton button = new ItemTexturedButton(new ItemStack(Item.ammoArrow), 0, 0);
		button.setListener(aButton -> getNextGui().open(player, tile.x, tile.y, tile.z));
		addAlignedButton(button, 7.5f, 6f);
	}

	public abstract RegisteredGui getNextGui();

	@SuppressWarnings("unchecked")
	@Override
	public ArchitectStationContainer<T> getContainer() {
		return (ArchitectStationContainer<T>) super.getContainer();
	}

	public ArchitectTableTileEntity getTile() {
		return getContainer().getTile();
	}
}
