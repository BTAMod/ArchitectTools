package com.github.kill05.inventory.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ItemTexturedButton extends GuiButton {

	protected final ItemStack item;

	public ItemTexturedButton(ItemStack item, int xPosition, int yPosition) {
		super(0, xPosition, yPosition, 18, 18, "");
		this.item = item;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (!this.visible) return;

		ItemModelDispatcher.getInstance().getDispatch(item).renderItemIntoGui(
			Tessellator.instance, mc.fontRenderer, mc.renderEngine,
			item, xPosition, yPosition, 1.0f, 1.0f
		);

		GL11.glDisable(GL11.GL_LIGHTING);
	}
}
