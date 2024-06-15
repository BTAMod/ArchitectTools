package com.github.kill05.blocks.architectstation.part;

import com.github.kill05.ArchitectTools;
import com.github.kill05.blocks.architectstation.ArchitectTableButton;
import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.blocks.architectstation.ChangePageButton;
import com.github.kill05.blocks.architectstation.tool.ToolModeGui;
import com.github.kill05.inventory.container.TileContainer;
import com.github.kill05.inventory.gui.TileContainerGui;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.materials.ArchitectMaterial;
import com.github.kill05.materials.MaterialInfo;
import com.github.kill05.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import org.lwjgl.opengl.GL11;

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
		ArchitectTableTileEntity tile = ((ArchitectTableTileEntity) getContainer().getTile());
		IInventory inv = tile.getPartInventory();
		ItemStack materialStack = inv.getStackInSlot(1);

		ArchitectPart part = tile.getSelectedPart();
		MaterialInfo info = ArchitectTools.getMaterialInfo(materialStack);
		float amount = ArchitectMaterial.getDisplayMaterialValue(info.value() * (materialStack != null ? materialStack.stackSize : 0));
		float materialCost = part != null ? ArchitectMaterial.getDisplayMaterialValue(part.getMaterialCost()) : 0;
		fontRenderer.drawCenteredString(amount + "/" + materialCost, 151, 83, materialCost > amount ? 0x00c00000 : 0x0000c000);
	}
}
