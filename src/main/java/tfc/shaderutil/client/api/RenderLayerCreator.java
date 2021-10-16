package tfc.shaderutil.client.api;

import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class RenderLayerCreator {
	public static RenderLayer makeItem(String name, Supplier<Shader> shaderSupplier, boolean useLightmap, boolean useTransparency) {
		return makeBasic(name, shaderSupplier, useLightmap, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, useTransparency);
	}
	
	/**
	 * This method is not meant for use for making a custom render layer for your own purposes
	 * This method exists to give you control for creating render layers with custom textures instead of the block atlas
	 */
	public static RenderLayer makeBasic(
			String name,
			Supplier<Shader> shaderSupplier,
			boolean useLightmap,
			Identifier texture,
			boolean useTransparency
	) {
		RenderLayer.MultiPhaseParameters params =
				RenderLayer.MultiPhaseParameters
						.builder()
						.lightmap(useLightmap ? RenderLayer.ENABLE_LIGHTMAP : RenderLayer.DISABLE_LIGHTMAP)
						.shader(new RenderPhase.Shader(shaderSupplier))
						.texture(new RenderPhase.Texture(texture, false, true))
						.transparency(useTransparency ? RenderLayer.TRANSLUCENT_TRANSPARENCY : RenderLayer.NO_TRANSPARENCY)
						.target(RenderLayer.ITEM_TARGET)
						.build(true);
		return RenderLayer.of(
				name,
				VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
				VertexFormat.DrawMode.QUADS,
				2097152, true, useTransparency,
				params
		);
	}
}
