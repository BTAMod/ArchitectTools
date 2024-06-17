package com.github.kill05.utils;

import com.github.kill05.ArchitectTools;
import com.github.kill05.config.ArchitectConfig;
import com.github.kill05.items.IArchitectItem;
import com.github.kill05.items.part.PartType;
import com.github.kill05.items.tool.ArchitectTool;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemUtils {

	public static boolean compare(@Nullable ItemStack stack, @NotNull Item item) {
		if(stack == null) return false;
		return stack.itemID == item.id;
	}

	public static @NotNull CompoundTag getArchitectCompound(@NotNull ItemStack item) {
		if(!(item.getItem() instanceof IArchitectItem))
			throw new IllegalArgumentException("Item must be an ArchitectItem.");

		return NBTUtils.getOrCreateCompound(item.getData(), ArchitectTools.MOD_ID);
	}

	public static @NotNull CompoundTag getToolComponents(@NotNull ItemStack item) {
		if(!(item.getItem() instanceof ArchitectTool))
			throw new IllegalArgumentException("Item must be an ArchitectTool.");

		return NBTUtils.getOrCreateCompound(getArchitectCompound(item), "components");
	}

	public static @NotNull ListTag getToolComponents(@NotNull ItemStack item, PartType part) {
		return NBTUtils.getOrCreateList(getToolComponents(item), part.getId());
	}


	public static String getDisabledName(String originalName) {
		return ArchitectConfig.getDisableVanillaTools() ? originalName + " §e(DISABLED)§r" : originalName;
	}

	public static String getDisabledDescription() {
		if(!ArchitectConfig.getDisableVanillaTools()) return "";
		return """
			§eVanilla tools are disabled by Architect's Tools.
			§eIf you wish to enable vanilla tools,
			§eplease open 'architectstools.cfg',
			§ethen set 'DisableVanillaTools' to 'false'.""";
	}


	public static boolean isBroken(ItemStack item) {
		if(!item.isItemStackDamageable()) return false;
		return item.getMetadata() > item.getMaxDamage();
	}

}
