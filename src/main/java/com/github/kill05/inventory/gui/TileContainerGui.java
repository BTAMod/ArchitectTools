package com.github.kill05.inventory.gui;

import com.github.kill05.inventory.container.TileContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;

public abstract class TileContainerGui extends GuiContainer {

	public TileContainerGui(TileContainer container) {
		super(container);
	}


	@Override
	public <E extends GuiButton> E add(E button) {
		button.id = controlList.size();
		button.xPosition = (width - xSize)  / 2 + button.xPosition;
		button.yPosition = (height - ySize) / 2 + button.yPosition;
		return super.add(button);
	}

	public void addAlignedButton(GuiButton button, float x, float y) {
		TileContainer container = getContainer();
		button.xPosition = container.alignX(x);
		button.yPosition = container.alignY(y);
		add(button);
	}

	public TileContainer getContainer() {
		return (TileContainer) inventorySlots;
	}
}
