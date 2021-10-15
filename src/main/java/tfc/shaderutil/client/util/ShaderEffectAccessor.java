package tfc.shaderutil.client.util;

import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.util.HashMap;

public interface ShaderEffectAccessor {
	void addPass(Identifier passId, PostProcessShader shader);
	PostProcessShader removePass(Identifier passId);
	void removePass(PostProcessShader pass);
	void removeLast();
	
	void setProjectionMatrix(Matrix4f matrix);
	
	HashMap<Identifier, PostProcessShader> getPasses();
	
	void clearPasses();
	
	PostProcessShader getPass(Identifier passId);
}
