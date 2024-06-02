package com.github.kill05.items.model;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.part.ArchitectPart;
import com.github.kill05.materials.ArchitectMaterial;
import com.github.kill05.materials.MaterialType;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArchitectPartModel extends ItemModelStandard {

	private final String texture;

	public ArchitectPartModel(ArchitectPart item, String texture) {
		super(item, null);
		this.texture = texture;
	}


	@Override
	public int getColor(ItemStack stack) {
		ArchitectMaterial material = ArchitectTools.getPartMaterial(stack);
		if(material == null) return 0x00FFFFFF;
		return material.color().getRGB();
	}

	@Override
	public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
		ArchitectMaterial material = ArchitectTools.getPartMaterial(itemStack);
		return MaterialType.getTexture(texture, material);
	}

	public ArchitectPart getItem() {
		return (ArchitectPart) item;
	}

}
