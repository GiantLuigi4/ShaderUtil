package tfc.shaderutil.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ShaderUtilClient implements ClientModInitializer {
	private static Shader testShader;
	
	@Override
	public void onInitializeClient() {
		CoreShaderRegistry.register(new Identifier("shaderutil:custom_item_shader"), (factory, name) -> testShader = new Shader(factory, name, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL));
//		PostProcessingShaderRegistry.register(new Identifier("shaderutil:blur"), (factory, name) -> testShader = new Shader(factory, name, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL));
	}
}
