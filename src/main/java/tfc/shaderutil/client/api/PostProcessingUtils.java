package tfc.shaderutil.client.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.util.Identifier;
import tfc.shaderutil.client.util.GameRendererAccessor;
import tfc.shaderutil.client.util.PostProcessShaderAccessor;

public class PostProcessingUtils {
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
		return ((GameRendererAccessor) MinecraftClient.getInstance().gameRenderer).addPass(passId, name);
	}
	
	public static PostProcessShader createShader(Identifier shader, Framebuffer input, Framebuffer output) {
		return ((GameRendererAccessor) MinecraftClient.getInstance().gameRenderer).createShader(shader, input, output);
	}
	
	public static void removePass(Identifier passId) {
		((GameRendererAccessor) MinecraftClient.getInstance().gameRenderer).removePass(passId);
	}
	
	public static PostProcessShader getPass(Identifier passId) {
		return ((GameRendererAccessor) MinecraftClient.getInstance().gameRenderer).getPass(passId);
	}
	
	public static boolean hasPass(Identifier passId) {
		return ((GameRendererAccessor) MinecraftClient.getInstance().gameRenderer).isShaderEnabled(passId);
	}
	
	public boolean checkAuxTarget(PostProcessShader shader, String name) {
		return ((PostProcessShaderAccessor)shader).hasAuxTarget(name);
	}
}
