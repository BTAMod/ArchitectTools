package com.github.kill05.architectstools.mixins.disablevanilla;

import com.github.kill05.architectstools.config.ArchitectConfig;
import com.github.kill05.architectstools.utils.ItemUtils;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemToolSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

@Mixin(
	value = ItemToolSword.class,
	remap = false
)
public abstract class ItemToolSwordMixin extends Item implements ICustomDescription {

	public ItemToolSwordMixin(int id) {
		super(id);
	}

	@Inject(
		method = "getStrVsBlock",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void getStrVsBlockInject(ItemStack itemstack, Block block, CallbackInfoReturnable<Float> cir) {
		if(ArchitectConfig.getDisableVanillaTools()) cir.setReturnValue(1.0f);
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
		method = "getDamageVsEntity",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void getDamageVsEntityInject(Entity entity, CallbackInfoReturnable<Integer> cir) {
		if(ArchitectConfig.getDisableVanillaTools()) cir.setReturnValue(1);
	}


	@Override
	public int getMaxDamage() {
		return ArchitectConfig.getDisableVanillaTools() ? 1 : super.getMaxDamage();
	}

	@Override
	public String getDescription(ItemStack itemStack) {
		return ItemUtils.getDisabledDescription();
	}

	@Override
	public String getTranslatedName(ItemStack itemstack) {
		return ItemUtils.getDisabledName(super.getTranslatedName(itemstack));
	}
}
