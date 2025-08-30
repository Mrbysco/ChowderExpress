package com.mrbysco.chowderexpress.client;

import com.mrbysco.chowderexpress.ChowderExpress;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TextureHelper {
	private static final ResourceLocation DEFAULT_SOUP = ChowderExpress.modLoc("textures/soup/default_soup.png");
	private final TextureManager textureManager;

	public TextureHelper(TextureManager manager) {
		this.textureManager = manager;
	}

	public AbstractTexture getTexture(ResourceLocation path) {
		AbstractTexture abstracttexture = this.textureManager.byPath.get(path);
		if (abstracttexture != null) {
			return abstracttexture;
		} else {
			SimpleTexture simpletexture = new SimpleTexture(path);
			registerAndLoad(path, simpletexture);
			return simpletexture;
		}
	}

	public void registerAndLoad(ResourceLocation textureId, ReloadableTexture texture) {
		try {
			texture.apply(this.loadContentsSafe(textureId, texture));
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.forThrowable(throwable, "Uploading texture");
			CrashReportCategory crashreportcategory = crashreport.addCategory("Uploaded texture");
			crashreportcategory.setDetail("Resource location", texture.resourceId());
			crashreportcategory.setDetail("Texture id", textureId);
			throw new ReportedException(crashreport);
		}

		this.textureManager.register(textureId, texture);
	}

	private TextureContents loadContentsSafe(ResourceLocation textureId, ReloadableTexture texture) {
		try {
			return loadContents(this.textureManager.resourceManager, textureId, texture);
		} catch (Exception exception) {
			return createMissing();
		}
	}

	private TextureContents loadContents(ResourceManager resourceManager, ResourceLocation textureId, ReloadableTexture texture) throws IOException {
		try {
			return texture.loadContents(resourceManager);
		} catch (FileNotFoundException filenotfoundexception) {
			return createMissing();
		}
	}

	private TextureContents createMissing() {
		return loadContentsSafe(DEFAULT_SOUP, new SimpleTexture(DEFAULT_SOUP));
	}
}
