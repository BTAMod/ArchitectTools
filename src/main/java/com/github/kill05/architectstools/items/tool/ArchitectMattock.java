package com.github.kill05.architectstools.items.tool;

import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.items.part.statistics.PartStatistic;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class ArchitectMattock extends ArchitectTool {

	public ArchitectMattock() {
		super("mattock");

		addPart(ArchitectPart.AXE_HEAD, "mattock/head", "mattock/broken_head");
		addPart(ArchitectPart.SHOVEL_HEAD, "mattock/back");
		addPart(ArchitectPart.TOOL_ROD, "mattock/handle").renderPriority(1);

		addValidStatistic(PartStatistic.DURABILITY, 0.5f);
		addValidStatistic(PartStatistic.MINING_SPEED, 0.4f);
		addValidStatistic(PartStatistic.ENTITY_DAMAGE, 0.25f);

		addMineableTags(BlockTags.MINEABLE_BY_AXE);
		addMineableTags(BlockTags.MINEABLE_BY_SHOVEL);
		addMineableTags(BlockTags.MINEABLE_BY_HOE);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		return Item.toolHoeDiamond.onItemUse(itemstack, entityplayer, world, blockX, blockY, blockZ, side, xPlaced, yPlaced);
	}
}
