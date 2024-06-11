package com.github.kill05.mixins.disablevanilla;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

@Mixin(
	value = ItemTool.class,
	remap = false
)
public abstract class ItemToolMixin extends Item implements ICustomDescription {

	public ItemToolMixin(int id) {
		super(id);
	}

	@SuppressWarnings("rawtypes")
	@Inject(
		method = "<init>",
		at = @At(value = "TAIL")
	)
	private void getStrVsBlockInject(String name, int id, int damageDealt, ToolMaterial toolMaterial, Tag tagEffectiveAgainst, CallbackInfo ci) {
		setMaxDamage(1);
	}

	@Override
	public String getDescription(ItemStack itemStack) {
		return """
			§eVanilla tools are disabled by Architect's Tools.
			§eIf you wish to enable vanilla tools,
			§eplease open 'architectstools.cfg',
			§ethen set 'DisableVanillaTools' to 'false'.""";
	}

	@Override
	public String getTranslatedName(ItemStack itemstack) {
		return super.getTranslatedName(itemstack) + " §e(DISABLED)§r";
	}

	@Override
	public int getDamageVsEntity(Entity entity) {
		return super.getDamageVsEntity(entity);
	}

	@Override
	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return super.getStrVsBlock(itemstack, block);
	}

	@Override
	public boolean canHarvestBlock(Block block) {
		return super.canHarvestBlock(block);
	}
}
