package com.github.kill05.mixins;

import com.github.kill05.items.part.ArchitectPart;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import turniplabs.halplibe.util.CreativeEntry;

@Mixin(
	value = CreativeEntry.class,
	remap = false
)
public abstract class CreativeEntryMixin {

	@Redirect(
		method = "addEntry",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/ItemStack;toString()Ljava/lang/String;")
	)
	private static String redirectEntryKey(ItemStack itemStack) {
		if (!(itemStack.getItem() instanceof ArchitectPart)) return itemStack.toString();
		return itemStack + ", " + itemStack.getData();
	}

}
