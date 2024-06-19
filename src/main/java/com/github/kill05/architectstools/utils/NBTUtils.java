package com.github.kill05.architectstools.utils;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;

public final class NBTUtils {

	public static CompoundTag getOrCreateCompound(CompoundTag root, String key) {
		CompoundTag tag = root.getCompoundOrDefault(key, null);
		if(tag != null) return tag;

		tag = new CompoundTag();
		root.putCompound(key, tag);
		return tag;
	}

	public static ListTag getOrCreateList(CompoundTag root, String key) {
		ListTag list = root.getListOrDefault(key, null);
		if(list != null) return list;

		list = new ListTag();
		root.putList(key, list);
		return list;
	}

}
