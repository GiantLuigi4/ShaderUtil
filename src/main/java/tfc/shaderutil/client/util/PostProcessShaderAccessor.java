package tfc.shaderutil.client.util;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.util.math.Matrix4f;

public interface PostProcessShaderAccessor {
	void setOutput(Framebuffer framebuffer);
	void setInput(Framebuffer framebuffer);
	Matrix4f getMatrix();
	
	boolean hasAuxTarget(String name);
	
	void tick();
}
