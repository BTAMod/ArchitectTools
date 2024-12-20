package com.github.kill05.architectstools.blocks.architectstation;

import com.github.kill05.architectstools.ArchitectGuis;
import com.github.kill05.architectstools.config.ArchitectConfig;
import com.github.kill05.architectstools.utils.InventoryUtils;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import net.minecraft.core.util.helper.Side;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class ArchitectTableBlock extends BlockTileEntity {

	public ArchitectTableBlock() {
		super("architect_table", ArchitectConfig.BLOCK_ID++, Material.wood);
	}

	@Override
	protected TileEntity getNewBlockEntity() {
		return new ArchitectTableTileEntity();
	}

	@Override
	public boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, Side side, double xPlaced, double yPlaced) {
		if(player.isSneaking()) return false;

		ArchitectGuis.PART_MODE.open(player, x, y, z);
		if(player instanceof EntityPlayerMP playerMP) {
			//playerMP.playerNetServerHandler.sendPacket(new Packet140TileEntityData(world.getBlockTileEntity(x, y, z)));
		}

		return true;
	}


	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		ArchitectTableTileEntity tile = (ArchitectTableTileEntity) world.getBlockTileEntity(x, y, z);
		InventoryUtils.dropInventoryContents(tile.getPartInventory(), world, x, y, z);
		InventoryUtils.dropInventoryContents(tile.getToolInventory(), world, x, y, z);
		super.onBlockRemoved(world, x, y, z, data);
	}
}
