package com.github.kill05.architectstools.mixins;

import com.github.kill05.architectstools.items.tool.ArchitectTool;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//todo: remove once 7.2_pre2 comes out
@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {

	@Shadow
	public abstract Item getItem();

	@Inject(
		method = "canHarvestBlock",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void canHarvestBlockMixin(Block block, CallbackInfoReturnable<Boolean> cir) {
		if (getItem() instanceof ArchitectTool tool) {
			cir.setReturnValue(tool.canHarvestBlock(getThis(), block));
		}
	}

	@Inject(
		method = "getMaxDamage",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void getMaxDamageMixin(CallbackInfoReturnable<Integer> cir) {
		if (getItem() instanceof ArchitectTool tool) {
			cir.setReturnValue(tool.getMaxDamage(getThis()));
		}
	}

	@Inject(
		method = "isItemStackDamageable",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void isItemStackDamageableMixin(CallbackInfoReturnable<Boolean> cir) {
		if (getItem() instanceof ArchitectTool) cir.setReturnValue(true);
	}

	@Inject(
		method = "damageItem",
		at = @At(value = "FIELD", target = "Lnet/minecraft/core/item/ItemStack;metadata:I", ordinal = 0, shift = At.Shift.BEFORE),
		cancellable = true
	)
	public void injectDamageItem(int i, Entity entity, CallbackInfo ci) {
		if (!(getItem() instanceof ArchitectTool)) return;
		if(ArchitectTool.isToolBroken(getThis())) ci.cancel();
	}

	@Inject(
		method = "damageItem",
		at = @At(value = "FIELD", target = "Lnet/minecraft/core/item/ItemStack;stackSize:I", ordinal = 1),
		cancellable = true
	)
	public void injectBreakItem(int i, Entity entity, CallbackInfo ci) {
		if (!(getItem() instanceof ArchitectTool)) return;
		ci.cancel();
	}

	@Inject(
		method = "getDamageVsEntity",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void getDamageVsEntityMixin(Entity entity, CallbackInfoReturnable<Integer> cir) {
		if (getItem() instanceof ArchitectTool item) {
			cir.setReturnValue(item.getDamageVsEntity(entity, getThis()));
		}
	}


	@Unique
	private ItemStack getThis() {
		return (ItemStack) ((Object) this);
	}

}
