package com.github.kill05.architectstools.blocks.architectstation.inventory.part;

import com.github.kill05.architectstools.ArchitectTools;
import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.materials.ArchitectMaterial;
import com.github.kill05.architectstools.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.architectstools.blocks.architectstation.inventory.ArchitectStationGui;
import com.github.kill05.architectstools.inventory.container.TileContainer;
import com.github.kill05.architectstools.materials.MaterialInfo;
import com.github.kill05.architectstools.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import org.lwjgl.opengl.GL11;

public class PartModeGui extends ArchitectStationGui<ArchitectPart> {

	public PartModeGui(ArchitectTableTileEntity tile, EntityPlayer player) {
		super(new PartModeContainer(tile, player), player);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		RenderUtils.bindTexture("gui/part_mode.png");
		GL11.glColor4f(1f, 1f, 1f, 1f);

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

	@Override
	protected void drawGuiContainerForegroundLayer() {
		FontRenderer fontRenderer = Minecraft.getMinecraft(this).fontRenderer;
		ArchitectTableTileEntity tile = getContainer().getTile();
		IInventory inv = tile.getPartInventory();
		ItemStack patternStack = inv.getStackInSlot(0);
		ItemStack materialStack = inv.getStackInSlot(1);

		ArchitectPart part = tile.getSelectedPart();
		MaterialInfo info = ArchitectTools.getMaterialInfo(materialStack);
		float amount = ArchitectMaterial.getDisplayMaterialValue(info.value() * (materialStack != null ? materialStack.stackSize : 0));
		float materialCost = part != null ? ArchitectMaterial.getDisplayMaterialValue(part.getMaterialCost()) : 0;

		boolean hasPattern = part == ArchitectPart.REPAIR_KIT || (patternStack != null && patternStack.stackSize > 0);
		int color = materialCost > amount ? 0x00c00000 : (hasPattern ? 0x0000c000 : 0x00f07000);

		fontRenderer.drawCenteredString(amount + "/" + materialCost, 151, 83, color);
	}
}
