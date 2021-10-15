package tfc.shaderutil.mixin;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import tfc.shaderutil.client.util.PostProcessShaderAccessor;

@Mixin(PostProcessShader.class)
public class PostProcessShaderMixin implements PostProcessShaderAccessor {
	@Mutable @Shadow @Final public Framebuffer input;
	@Mutable @Shadow @Final public Framebuffer output;
	
	@Shadow private Matrix4f projectionMatrix;
	
	@Override
	public void setOutput(Framebuffer framebuffer) {
		output = framebuffer;
	}
	
	@Override
	public void setInput(Framebuffer framebuffer) {
		input = framebuffer;
	}
	
	@Override
	public Matrix4f getMatrix() {
		return projectionMatrix;
	}
}
