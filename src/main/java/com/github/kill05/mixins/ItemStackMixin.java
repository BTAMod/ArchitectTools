package com.github.kill05.mixins;

import com.github.kill05.items.tool.ArchitectTool;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
		if(getItem() instanceof ArchitectTool tool) {
			cir.setReturnValue(tool.canHarvestBlock((ItemStack) ((Object) this), block));
		}
	}

}
