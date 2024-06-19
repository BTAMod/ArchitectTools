package com.github.kill05.architectstools.items;

import net.minecraft.core.item.IItemConvertible;

public interface IArchitectItem extends IItemConvertible {

	int ordinal();

	boolean renderPattern();

}
