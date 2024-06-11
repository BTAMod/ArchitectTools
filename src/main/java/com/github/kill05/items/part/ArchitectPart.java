package com.github.kill05.items.part;

import com.github.kill05.ArchitectTools;
import com.github.kill05.config.ArchitectConfig;
import com.github.kill05.items.ArchitectItem;
import com.github.kill05.items.model.ArchitectPartModel;
import com.github.kill05.items.part.statistics.PartStatistic;
import com.github.kill05.items.part.statistics.PartStatistics;
import com.github.kill05.materials.ArchitectMaterial;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import turniplabs.halplibe.helper.ItemBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ArchitectPart extends Item implements ArchitectItem, ICustomDescription {

	public static final List<ArchitectPart> VALUES = new ArrayList<>();

	public static final ArchitectPart TOOL_ROD = partItem("tool_rod")
		.validTypes(PartType.HANDLE, PartType.EXTRA);

	public static final ArchitectPart TOOL_BINDING = partItem("tool_binding", "part/binding")
		.validTypes(PartType.EXTRA);

	public static final ArchitectPart SWORD_GUARD = partItem("sword_guard", "part/wide_guard")
		.validTypes(PartType.EXTRA);

	public static final ArchitectPart PICKAXE_HEAD = partItem("pickaxe_head", "pickaxe/head")
		.validTypes(PartType.HEAD);

	public static final ArchitectPart AXE_HEAD = partItem("axe_head", "axe/head")
		.validTypes(PartType.HEAD);

	public static final ArchitectPart SHOVEL_HEAD = partItem("shovel_head", "shovel/head")
		.validTypes(PartType.HEAD);

	public static final ArchitectPart SWORD_BLADE = partItem("sword_blade")
		.validTypes(PartType.HEAD);


	public static ArchitectPart partItem(String id) {
		return partItem(id, "part/" + id);
	}

	public static ArchitectPart partItem(String id, String texture) {
		return new ItemBuilder(ArchitectTools.MOD_ID)
			.setItemModel(item -> new ArchitectPartModel((ArchitectPart) item, texture))
			.build(new ArchitectPart(id));
	}

	private final String partId;
	private final int ordinal;
	private final List<PartType> validTypes;

	public ArchitectPart(String partId) {
		super(partId + "_part", ArchitectConfig.PART_ID++);
		this.partId = partId;
		this.ordinal = VALUES.size();
		this.validTypes = new ArrayList<>();

		VALUES.add(this);
	}


	public String getTranslatedPartName() {
		return I18n.getInstance().translateNameKey("item." + ArchitectTools.MOD_ID + "." + getPartId());
	}

	@Override
	public String getTranslatedName(ItemStack itemstack) {
		ArchitectMaterial material = ArchitectTools.getPartMaterial(itemstack);
		String materialName = material != null ? material.getTranslatedName() : "ERROR";
		return String.format(getTranslatedPartName(), materialName);
	}

	@Override
	public String getDescription(ItemStack itemStack) {
		ArchitectMaterial material = ArchitectTools.getPartMaterial(itemStack);
		if(material == null) return "ERROR";

		StringBuilder builder = new StringBuilder();
		Iterator<PartType> iterator = validTypes.iterator();

		while (iterator.hasNext()) {
			PartType type = iterator.next();
			PartStatistics statistics = material.getStatistics(type);
			if(statistics == null) continue; // Material is not compatible with this statistic
			// (ex. HEAD and gold because you can't make gold pickaxe heads)

			builder.append("§1").append(type.getTranslatedName()).append(":\n");
			Iterator<Map.Entry<PartStatistic<?>, Object>> iterator1 = statistics.iterator();
			while (iterator1.hasNext()) {
				Map.Entry<PartStatistic<?>, Object> next = iterator1.next();
				builder.append(' ');
				builder.append(next.getKey().getTranslatedName()).append(": ");
				builder.append(next.getKey().formatPartValue(itemStack, next.getValue()));
				if(iterator1.hasNext()) builder.append('\n');
			}

			builder.append('\n');
			if(iterator.hasNext()) builder.append("\n");
		}

		return builder.toString();
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
