package com.github.kill05.architectstools.utils;

import com.github.kill05.architectstools.ArchitectTools;
import net.minecraft.client.render.stitcher.TextureRegistry;

public final class TextureUtils {

	public static final String TEXTURE_PATH = "/assets/" + ArchitectTools.MOD_ID + "/textures/";

	public static boolean textureFileExists(String path) {
		return RenderUtils.class.getResource(TEXTURE_PATH + path) != null;
	}

	public static boolean registerTextureIfPresent(String path) {
		if(!textureFileExists(path + ".png")) return false;
		TextureRegistry.getTexture(ArchitectTools.MOD_ID + ":" + path);
		return true;
	}

}
