package com.github.kill05.items.model;

import com.github.kill05.ArchitectTools;
import com.github.kill05.items.tool.ArchitectTool;
import com.github.kill05.items.tool.ToolPartInfo;
import com.github.kill05.materials.ArchitectMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;

public class ArchitectToolModel extends ItemModelStandard {

	private Color color;

	public ArchitectToolModel(ArchitectTool item) {
		super(item, null);
	}

	@Override
	public void renderItemInWorld(Tessellator tessellator, Entity entity, ItemStack itemStack, float brightness, float alpha, boolean worldTransform) {
		ArchitectTools.iterateToolParts(itemStack, true, (part, material) -> {
			setIconAndColor(itemStack, entity, part, material);
			super.renderItemInWorld(tessellator, entity, itemStack, brightness, alpha, worldTransform);
		});
	}

	@Override
	public void renderItemIntoGui(Tessellator tessellator, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemStack, int x, int y, float brightness, float alpha) {
		ArchitectTools.iterateToolParts(itemStack, true, (part, material) -> {
			setIconAndColor(itemStack, null, part, material);
			super.renderItemIntoGui(tessellator, fontrenderer, renderengine, itemStack, x, y, brightness, alpha);
		});
	}

	@Override
	public void renderAsItemEntity(Tessellator tessellator, @Nullable Entity entity, Random random, ItemStack itemStack, int renderCount, float yaw, float brightness, float partialTick) {
		if(Minecraft.getMinecraft(Minecraft.class).gameSettings.items3D.value) {
			super.renderAsItemEntity(tessellator, entity, random, itemStack, renderCount, yaw, brightness, partialTick);
			return;
		}

		ArchitectTools.iterateToolParts(itemStack, true, (part, material) -> {
			setIconAndColor(itemStack, entity, part, material);
			super.renderAsItemEntity(tessellator, entity, random, itemStack, renderCount, yaw, brightness, partialTick);
			GL11.glScalef(2F, 2F, 2F);
		});
	}

	private void setIconAndColor(ItemStack itemStack, Entity entity, ToolPartInfo part, ArchitectMaterial material) {
		this.icon = part.getIcon(itemStack);
		this.color = material != null ? material.color() : null;
	}


	@Override
	public int getColorFromMeta(int meta) {
		return color != null ? color.getRGB() : 0;
	}
}
