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

		GL11.glBindTexture(3553, mc.renderEngine.getTexture("/gui/items.png"));
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		render(mc, item, xPosition, yPosition, 1.0f, 1.0f);
		this.mouseDragged(mc, mouseX, mouseY);
	}

	public void render(Minecraft mc, ItemStack item, int x, int y, float brightness, float alpha) {
		ItemModelDispatcher.getInstance().getDispatch(item).renderItemIntoGui(
			Tessellator.instance, mc.fontRenderer, mc.renderEngine,
			item, x, y, brightness, alpha
		);
	}
}
