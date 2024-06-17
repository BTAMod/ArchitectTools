package com.github.kill05.blocks.architectstation;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.IArchitectItem;
import com.github.kill05.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.Item;

public class ArchitectTableButton extends GuiButton {

	private final ArchitectTableTileEntity tile;
	private final IArchitectItem item;

	public ArchitectTableButton(ArchitectTableTileEntity tile, IArchitectItem item, int x, int y) {
		super(0, x, y, 16, 16, "");
		this.tile = tile;
		this.item = item;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (!this.visible) return;

		float brightness = tile.getSelectedPart() == item ? 0.5f : 1.0f;
		Item pattern = ArchitectTools.BLANK_PATTERN;

		if(item.renderPattern()) {
			RenderUtils.getItemModel(pattern).renderItemIntoGui(
				Tessellator.instance, mc.fontRenderer, mc.renderEngine,
				pattern.getDefaultStack(),
				xPosition,
				yPosition,
				brightness, 1.0f
			);
		}

		RenderUtils.getItemModel(item).renderItemIntoGui(
			Tessellator.instance, mc.fontRenderer, mc.renderEngine,
			item.getDefaultStack(),
			xPosition, //+ item.getButtonRenderOffsetX(),
			yPosition, //+ item.getButtonRenderOffsetX(),
			brightness, 1.0f
		);
	}

	public IArchitectItem getItem() {
		return item;
	}

}
