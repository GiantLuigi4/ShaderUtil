package tfc.shaderutil.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.util.Identifier;
import tfc.shaderutil.client.util.GameRendererAccessor;

public class PostProcessUtils {
	public static PostProcessShader addPass(String passId, String name) {
		return addPass(new Identifier(passId), new Identifier(name));
	}
	
	public static PostProcessShader addPass(String passId, Identifier name) {
		return addPass(new Identifier(passId), name);
	}
	
	public static PostProcessShader addPass(Identifier passId, String name) {
		return addPass(passId, new Identifier(name));
	}
	
	public static PostProcessShader addPass(Identifier passId, Identifier name) {
		return ((GameRendererAccessor)MinecraftClient.getInstance().gameRenderer).addPass(passId, name);
	}
	
	public static PostProcessShader getPass(Identifier passId) {
		return ((GameRendererAccessor)MinecraftClient.getInstance().gameRenderer).getPass(passId);
	}
	
	public boolean hasPass(Identifier passId) {
		return ((GameRendererAccessor)MinecraftClient.getInstance().gameRenderer).isShaderEnabled(passId);
	}
}
