package com.github.kill05.architectstools.blocks.architectstation.inventory.tool;

import com.github.kill05.architectstools.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.architectstools.blocks.architectstation.inventory.ArchitectStationGui;
import com.github.kill05.architectstools.inventory.container.TileContainer;
import com.github.kill05.architectstools.items.tool.ArchitectTool;
import com.github.kill05.architectstools.items.tool.ToolPartInfo;
import com.github.kill05.architectstools.utils.RenderUtils;
import net.minecraft.core.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class ToolModeGui extends ArchitectStationGui<ArchitectTool> {

	public ToolModeGui(ArchitectTableTileEntity tile, EntityPlayer player) {
		super(new ToolModeContainer(tile, player), player);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		RenderUtils.bindTexture("gui/tool_mode.png");
		GL11.glColor4f(1f, 1f, 1f, 1f);

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
				xSize, 16 * (info.part().ordinal() - 1), 16, 16
			);
		}
	}
}
