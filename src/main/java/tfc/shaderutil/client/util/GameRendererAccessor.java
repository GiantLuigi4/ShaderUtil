package tfc.shaderutil.client.util;

import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.util.Identifier;

public interface GameRendererAccessor {
	PostProcessShader addPass(Identifier passId, Identifier shader);
	void removePass(Identifier passId);
	boolean isShaderEnabled(Identifier passId);
	PostProcessShader getPass(Identifier passId);
}
