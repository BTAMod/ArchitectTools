package com.github.kill05.blocks.architectstation.part;

import com.github.kill05.blocks.architectstation.ArchitectTableButton;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.blocks.architectstation.ChangePageButton;
import com.github.kill05.blocks.architectstation.tool.ToolModeGui;
import com.github.kill05.inventory.container.TileContainer;
import com.github.kill05.inventory.gui.TileContainerGui;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.utils.RenderUtils;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;

public class PartModeGui extends TileContainerGui {

	private final EntityPlayer player;

	public PartModeGui(ArchitectTableTileEntity tile, EntityPlayer player) {
		super(new PartModeContainer(tile, player));
		this.player = player;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	public void init() {
		super.init();

		ArchitectTableTileEntity tile = (ArchitectTableTileEntity) getContainer().getTile();
		for(int i = 0; i < ArchitectPart.VALUES.size(); i++) {
			int x = (i % 4) * 18 + 17;
			int y = (i / 4) * 18 + 18;

			ArchitectPart part = ArchitectPart.VALUES.get(i);
			add(new ArchitectTableButton(tile, part, x, y)).setListener(button -> {
				boolean different = tile.getSelectedPart() != part;
				tile.setSelectedPart(different ? part : null);
			});
		}

		addAlignedButton(new ChangePageButton(() -> new ToolModeGui((ArchitectTableTileEntity) getContainer().getTile(), player)), 7.5f, 6f);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		RenderUtils.bindTexture("gui/part_mode.png");
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		TileContainer container = getContainer();
		IInventory inventory = container.getInventory();
		for(int i = 0; i < 2; i++) {
			if (inventory.getStackInSlot(i) == null) {
				drawTexturedModalRect(
					x + container.alignX(5.5f),
					y + container.alignY(2.5f + i * 2),
					xSize, i * 16, 16, 16
				);
			}
		}
	}
}
