package com.github.kill05;

import com.github.kill05.blocks.architectstation.ArchitectTableTileEntity;
import com.github.kill05.blocks.architectstation.part.PartModeContainer;
import com.github.kill05.blocks.architectstation.part.PartModeGui;
import com.github.kill05.blocks.architectstation.tool.ToolModeContainer;
import com.github.kill05.blocks.architectstation.tool.ToolModeGui;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.factory.block.TileGuiFactory;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public class ArchitectGuis {

	public static final RegisteredGui PART_MODE = GuiHelper.registerServerGui(ArchitectTools.MOD_ID, "part_mode", new TileGuiFactory<ArchitectTableTileEntity>() {
		@Override
		public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull ArchitectTableTileEntity tile) {
			return new PartModeGui(tile, player);
		}

		@Override
		public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull ArchitectTableTileEntity tile) {
			return new PartModeContainer(tile, player);
		}
	});

	public static final RegisteredGui TOOL_MODE = GuiHelper.registerServerGui(ArchitectTools.MOD_ID, "tool_mode", new TileGuiFactory<ArchitectTableTileEntity>() {
		@Override
		public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull ArchitectTableTileEntity tile) {
			return new ToolModeGui(tile, player);
		}

		@Override
		public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull ArchitectTableTileEntity tile) {
			return new ToolModeContainer(tile, player);
		}
	});

}
