package com.github.kill05.blocks.architectstation.tool;

import com.github.kill05.blocks.architectstation.ArchitectTableButton;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.blocks.architectstation.ChangePageButton;
import com.github.kill05.blocks.architectstation.part.PartModeGui;
import com.github.kill05.inventory.container.TileContainer;
import com.github.kill05.inventory.gui.TileContainerGui;
import com.github.kill05.items.tool.ArchitectTool;
import com.github.kill05.items.tool.ToolPartInfo;
import com.github.kill05.utils.RenderUtils;
import net.minecraft.core.entity.player.EntityPlayer;

public class ToolModeGui extends TileContainerGui {

	private final EntityPlayer player;

	public ToolModeGui(ArchitectTableTileEntity tile, EntityPlayer player) {
		super(new ToolModeContainer(tile, player));
		this.player = player;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	public void init() {
		super.init();

		ArchitectTableTileEntity tile = (ArchitectTableTileEntity) getContainer().getTile();
		for(int i = 0; i < ArchitectTool.VALUES.size(); i++) {
			int x = (i % 4) * 18 + 17;
			int y = (i / 4) * 18 + 18;

			ArchitectTool tool = ArchitectTool.VALUES.get(i);
			add(new ArchitectTableButton(tile, tool, x, y)).setListener(button -> {
				boolean different = tile.getSelectedTool() != tool;
				tile.setSelectedTool(different ? tool : null);
			});
		}

		addAlignedButton(new ChangePageButton(() -> new PartModeGui((ArchitectTableTileEntity) getContainer().getTile(), player)), 7.5f, 6f);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		RenderUtils.bindTexture("gui/tool_mode.png");
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		TileContainer container = getContainer();
		ArchitectTableTileEntity tile = ((ArchitectTableTileEntity) container.getTile());
		ArchitectTool tool = tile.getSelectedTool();
		if(tool == null) return;

		int parts = 0;
		for (ToolPartInfo info : tool.getPartList()) {
			drawTexturedModalRect(
				x + container.alignX(5.5f),
				y + container.alignY(2.5f + parts++),
				xSize, 16 * info.part().ordinal(), 16, 16
			);
		}
		/*
		TileContainer container = getContainer();
		IInventory inventory = container.getInventory();
		if(inventory.getStackInSlot(0) == null) {
			drawTexturedModalRect(
				x + container.alignX(5.5f),
				y + container.alignY(2.5f),
				xSize, 0, 16, 16
			);
		}

		if(inventory.getStackInSlot(1) == null) {
			drawTexturedModalRect(
				x + container.alignX(5.5f),
				y + container.alignY(4.5f),
				xSize, 16, 16, 16
			);
		}

		 */
	}
}
