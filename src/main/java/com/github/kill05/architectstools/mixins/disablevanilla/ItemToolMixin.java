package com.github.kill05.architectstools.mixins.disablevanilla;

import com.github.kill05.architectstools.config.ArchitectConfig;
import com.github.kill05.architectstools.utils.ItemUtils;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

@Mixin(
	value = ItemTool.class,
	remap = false
)
public abstract class ItemToolMixin extends Item implements ICustomDescription {

	public ItemToolMixin(int id) {
		super(id);
	}

	@Override
	public String getDescription(ItemStack itemStack) {
		return ItemUtils.getDisabledDescription();
	}

	@Override
	public String getTranslatedName(ItemStack itemstack) {
		return ItemUtils.getDisabledName(super.getTranslatedName(itemstack));
	}

	@Inject(
		method = "getDamageVsEntity",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void getDamageVsEntityInject(Entity entity, CallbackInfoReturnable<Integer> cir) {
		if(ArchitectConfig.getDisableVanillaTools()) cir.setReturnValue(1);
	}

	@Inject(
		method = "isSilkTouch",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void isSilkTouchInject(CallbackInfoReturnable<Boolean> cir) {
		if(ArchitectConfig.getDisableVanillaTools()) cir.setReturnValue(false);
	}

	@Inject(
		method = "getStrVsBlock",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void getStrVsBlockInject(ItemStack itemstack, Block block, CallbackInfoReturnable<Float> cir) {
		if(ArchitectConfig.getDisableVanillaTools()) cir.setReturnValue(1.0f);
	}

	@Override
	public int getMaxDamage() {
		return ArchitectConfig.getDisableVanillaTools() ? 1 : super.getMaxDamage();
	}
}
