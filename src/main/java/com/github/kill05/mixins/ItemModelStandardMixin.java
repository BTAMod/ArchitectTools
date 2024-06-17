package com.github.kill05.mixins;

import com.github.kill05.items.tool.ArchitectTool;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
	value = ItemModelStandard.class,
	remap = false
)
public abstract class ItemModelStandardMixin {

	@Redirect(
		method = "renderItemOverlayIntoGUI",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/ItemStack;isItemDamaged()Z")
	)
	public boolean redirectRenderItemOverlay(ItemStack item) {
		if(!(item.getItem() instanceof ArchitectTool)) return item.isItemDamaged();
		return !ArchitectTool.isToolBroken(item) && item.isItemDamaged();
	}
}
