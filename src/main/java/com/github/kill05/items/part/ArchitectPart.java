package com.github.kill05.items.part;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.ArchitectItem;
import com.github.kill05.items.model.ArchitectPartModel;
import com.github.kill05.materials.ArchitectMaterial;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.World;
import turniplabs.halplibe.helper.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class ArchitectPart extends Item implements ArchitectItem {

	public static final List<ArchitectPart> VALUES = new ArrayList<>();

	public static final ArchitectPart TOOL_ROD = partItem("tool_rod")
		.validTypes(PartType.HANDLE, PartType.EXTRA);

	public static final ArchitectPart TOOL_BINDING = partItem("tool_binding", "part/binding")
		.validTypes(PartType.EXTRA);

	public static final ArchitectPart PICKAXE_HEAD = partItem("pickaxe_head", "pickaxe/head")
		.validTypes(PartType.HEAD);

	public static final ArchitectPart AXE_HEAD = partItem("axe_head", "axe/head")
		.validTypes(PartType.HEAD);

	public static final ArchitectPart SHOVEL_HEAD = partItem("shovel_head", "shovel/head")
		.validTypes(PartType.HEAD);


	public static ArchitectPart partItem(String id) {
		return partItem(id, "part/" + id);
	}

	public static ArchitectPart partItem(String id, String texture) {
		return new ItemBuilder(ArchitectTools.MOD_ID)
			.setItemModel(item -> new ArchitectPartModel((ArchitectPart) item, texture))
			.build(new ArchitectPart(ArchitectTools.ITEM_ID++, id));
	}

	private final String partId;
	private final int ordinal;
	private final List<PartType> validTypes;

	public ArchitectPart(int id, String partId) {
		super(partId + "_part", id);
		this.partId = partId;
		this.ordinal = VALUES.size();
		this.validTypes = new ArrayList<>();

		VALUES.add(this);
	}


	public String getTranslatedPartName() {
		return I18n.getInstance().translateNameKey("part." + ArchitectTools.MOD_ID + "." + getPartId());
	}

	@Override
	public String getTranslatedName(ItemStack itemstack) {
		ArchitectMaterial material = ArchitectTools.getPartMaterial(itemstack);
		String materialName = material != null ? material.getTranslatedName() : "ERROR";
		return materialName + " " + getTranslatedPartName();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		player.sendMessage(String.valueOf(item));
		return item;
	}

	@Override
	public boolean renderPattern() {
		return true;
	}


	public ArchitectPart validTypes(PartType... types) {
		validTypes.addAll(List.of(types));
		return this;
	}

	public List<PartType> getValidTypes() {
		return validTypes;
	}

	public String getPartId() {
		return partId;
	}

	public int ordinal() {
		return ordinal;
	}

}
