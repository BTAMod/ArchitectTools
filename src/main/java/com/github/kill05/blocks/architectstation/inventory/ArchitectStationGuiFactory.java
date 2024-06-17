package com.github.kill05.blocks.architectstation.inventory;

import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.items.IArchitectItem;
import net.minecraft.core.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.gui.factory.block.TileGuiFactory;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public interface ArchitectStationGuiFactory extends TileGuiFactory<ArchitectTableTileEntity> {

	RegisteredGui getNextGui();

	@Override
	default void onButtonClick(@NotNull RegisteredGui gui, @NotNull EntityPlayer player, @NotNull EntityPlayer clicker, int buttonId) {
		if(!(player.craftingInventory instanceof ArchitectStationContainer<?> container)) return;

		if(buttonId == container.getModeValues().size()) {
			if(player != clicker) return;
			getNextGui().open(player, container.getTile());
			return;
		}

		setSelected(container, buttonId);
	}

	private static <T extends IArchitectItem> void setSelected(ArchitectStationContainer<T> container, int buttonId) {
		boolean inBounds = buttonId >= 0 && buttonId < container.getModeValues().size();
		container.setSelected(inBounds ? container.getModeValues().get(buttonId) : null);
	}
}
