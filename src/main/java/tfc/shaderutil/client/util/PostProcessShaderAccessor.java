package tfc.shaderutil.client.util;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

public interface PostProcessShaderAccessor {
	void setOutput(Framebuffer framebuffer);
	
	void setInput(Framebuffer framebuffer);
	
	Matrix4f getMatrix();
}
