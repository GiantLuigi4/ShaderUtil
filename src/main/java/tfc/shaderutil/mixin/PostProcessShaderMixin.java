package tfc.shaderutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.JsonEffectGlShader;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.shaderutil.client.util.PostProcessShaderAccessor;

import java.util.List;

@Mixin(PostProcessShader.class)
public class PostProcessShaderMixin implements PostProcessShaderAccessor {
	@Mutable @Shadow @Final public Framebuffer input;
	@Mutable @Shadow @Final public Framebuffer output;
	
	@Shadow private Matrix4f projectionMatrix;
	
	@Shadow @Final private JsonEffectGlShader program;
	
	@Shadow @Final private List<String> samplerNames;
	@Unique int tick;
	
	@Inject(at = @At("HEAD"), method = "render")
	public void preRender(float time, CallbackInfo ci) {
		program.getUniformByNameOrDummy("Ticks").set(time + MinecraftClient.getInstance().getTickDelta());
	}
	
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
	
	@Override
	public void tick() {
		tick++;
	}
	
	@Override
	public boolean hasAuxTarget(String name) {
		return samplerNames.contains(name);
	}
}
