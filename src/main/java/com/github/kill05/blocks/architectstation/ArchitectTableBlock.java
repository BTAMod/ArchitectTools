package com.github.kill05.blocks.architectstation;

import com.github.kill05.ArchitectGuis;
import com.github.kill05.config.ArchitectConfig;
import com.github.kill05.utils.InventoryUtils;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;

public class ArchitectTableBlock extends BlockTileEntity {

	public ArchitectTableBlock() {
		super("architect_table", ArchitectConfig.BLOCK_ID++, Material.wood);
	}

	@Override
	protected TileEntity getNewBlockEntity() {
		return new ArchitectTableTileEntity();
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if(player.isSneaking()) return false;
		ArchitectGuis.PART_MODE.open(player, x, y, z);
		return true;
	}


	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		ArchitectTableTileEntity tile = ((ArchitectTableTileEntity) world.getBlockTileEntity(x, y, z));
		InventoryUtils.dropInventoryContents(tile.getPartInventory(), world, x, y, z);
		InventoryUtils.dropInventoryContents(tile.getToolInventory(), world, x, y, z);
		super.onBlockRemoved(world, x, y, z, data);
	}
}
