package com.github.kill05.architectstools.mixins.disablevanilla;

import com.github.kill05.architectstools.config.ArchitectConfig;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = ItemToolPickaxe.class,
	remap = false
)
public abstract class ItemToolPickaxeMixin {

	@Inject(
		method = "canHarvestBlock",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void canHarvestBlockInject(Block block, CallbackInfoReturnable<Boolean> cir) {
		if(ArchitectConfig.getDisableVanillaTools()) cir.setReturnValue(false);
	}
}
