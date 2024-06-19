package com.github.kill05.architectstools.recipe;

import com.github.kill05.architectstools.ArchitectTools;
import com.github.kill05.architectstools.items.part.ArchitectPart;
import com.github.kill05.architectstools.items.part.PartType;
import com.github.kill05.architectstools.items.tool.ArchitectTool;
import com.github.kill05.architectstools.items.tool.ToolPartInfo;
import com.github.kill05.architectstools.materials.ArchitectMaterial;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingDynamic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryCrafting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RecipeEntryRepairTool extends RecipeEntryCraftingDynamic {

	@Override
	public boolean matches(InventoryCrafting inv) {
		ItemStack repairKit = getRepairKit(inv);
		ItemStack tool = getTool(inv);

		if(repairKit == null || tool == null) return false;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if(itemStack != null && itemStack != tool && itemStack != repairKit) return false;
		}

		ArchitectMaterial kitMaterial = ArchitectTools.getPartMaterial(repairKit);
		if(kitMaterial == null) return false;

		for (Map.Entry<@NotNull ToolPartInfo, @Nullable ArchitectMaterial> entry : ArchitectTools.getToolParts(tool, PartType.HEAD).entrySet()) {
			if(entry.getValue() == kitMaterial) return true;
		}

		return false;
	}

	@Override
	public boolean matchesQuery(SearchQuery searchQuery) {
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack tool = getTool(inv);
		if(tool == null) throw new AssertionError();

		ItemStack itemStack = tool.copy();
		itemStack.setMetadata(0);
		return itemStack;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack[] onCraftResult(InventoryCrafting inv) {
		ItemStack[] items = new ItemStack[inv.getSizeInventory()];

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if(itemStack == null) continue;

			if(--itemStack.stackSize <= 0) {
				inv.setInventorySlotContents(i, null);
			}
		}

		return items;
	}


	private ItemStack getTool(InventoryCrafting inv) {
		ItemStack tool = null;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if(itemStack == null) continue;

			if(itemStack.getItem() instanceof ArchitectTool) {
				if(tool != null) return null;
				tool = itemStack;
			}
		}

		return tool;
	}

	private ItemStack getRepairKit(InventoryCrafting inv) {
		ItemStack repairKit = null;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if(itemStack == null) continue;

			if(itemStack.getItem() == ArchitectPart.REPAIR_KIT) {
				if(repairKit != null) return null;
				repairKit = itemStack;
			}
		}

		return repairKit;
	}

}
