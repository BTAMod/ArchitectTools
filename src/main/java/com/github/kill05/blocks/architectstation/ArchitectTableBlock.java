package com.github.kill05.blocks.architectstation;

import com.github.kill05.blocks.architectstation.part.PartModeGui;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;

public class ArchitectTableBlock extends BlockTileEntity {

	public ArchitectTableBlock(int id) {
		super("architect_table", id, Material.wood);
	}

	@Override
	protected TileEntity getNewBlockEntity() {
		return new ArchitectTableTileEntity();
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if(player.isSneaking()) return false;

		ArchitectTableTileEntity tile = (ArchitectTableTileEntity) world.getBlockTileEntity(x, y, z);
		Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(new PartModeGui(tile, player));
		return true;
	}

}
