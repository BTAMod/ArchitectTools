package com.github.kill05.blocks.architectstation;

import com.github.kill05.inventory.button.ItemTexturedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

import java.util.function.Supplier;

public class ChangePageButton extends ItemTexturedButton {

	public ChangePageButton(Supplier<GuiContainer> nextGuiSupplier) {
		super(new ItemStack(Item.ammoArrow), 0, 0);

		setListener(button -> Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(nextGuiSupplier.get()));
	}


}
