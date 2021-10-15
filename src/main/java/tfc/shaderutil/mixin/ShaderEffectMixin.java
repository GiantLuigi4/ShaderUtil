package tfc.shaderutil.mixin;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.shaderutil.client.util.PostProcessShaderAccessor;
import tfc.shaderutil.client.util.ShaderEffectAccessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ShaderEffect.class)
public abstract class ShaderEffectMixin implements ShaderEffectAccessor {
	@Shadow
	@Final
	private List<PostProcessShader> passes;
	
	@Shadow
	protected abstract Framebuffer getTarget(String name);
	
	@Shadow private Matrix4f projectionMatrix;
	@Shadow @Final private List<Framebuffer> defaultSizedTargets;
	@Shadow @Final private Framebuffer mainTarget;
	@Shadow @Final private Map<String, Framebuffer> targetsByName;
	@Unique
	HashMap<Identifier, PostProcessShader> shaderUtilShaders = new HashMap<>();
	
	@Unique
	Framebuffer endTarg;
	
	@Inject(at = @At("TAIL"), method = "render")
	public void drawShaderUtilShaders(float tickDelta, CallbackInfo ci) {
		if (shaderUtilShaders.isEmpty()) return;
		
		Framebuffer buffer = null;
		for (String s : targetsByName.keySet()) {
			buffer = targetsByName.get(s);
			if (buffer != mainTarget) {
				break;
			}
		}
//		Framebuffer alternator = defaultSizedTargets.get(0);
//		Framebuffer src = alternator;
//		Framebuffer alt = (alternator == mainTarget) ? mainTarget : defaultSizedTargets.get(1);
		Framebuffer alternator = mainTarget;
		Framebuffer src = mainTarget;
		Framebuffer alt = buffer;
		
		for (PostProcessShader shaderUtilShader : shaderUtilShaders.values()) {
			if (((PostProcessShaderAccessor)shaderUtilShader).getMatrix() != null) {
				((PostProcessShaderAccessor) shaderUtilShader).setOutput(alternator);
				if (alternator == src) alternator = alt;
				else alternator = src;
				((PostProcessShaderAccessor) shaderUtilShader).setInput(alternator);
				shaderUtilShader.render(tickDelta);
			}
		}
//		if (alternator == src) alternator = alt;
//		else alternator = src;
		if (alternator != alt) {
			PostProcessShader endShader = passes.get(passes.size() - 1);
			endShader.render(tickDelta);
		}

//		PostProcessShader endShader = passes.get(passes.size() - 1);
//		((PostProcessShaderAccessor) endShader).setOutput(endTarg);
	}
	
	@Inject(at = @At("TAIL"), method = "setupDimensions")
	public void updateDimensions(int targetsWidth, int targetsHeight, CallbackInfo ci) {
		if (shaderUtilShaders.isEmpty()) return;
		
		for (PostProcessShader shaderUtilShader : shaderUtilShaders.values())
			shaderUtilShader.setProjectionMatrix(projectionMatrix);
	}
	
	@Inject(at = @At("TAIL"), method = "close")
	public void close(CallbackInfo ci) {
		if (shaderUtilShaders.isEmpty()) return;
		
		for (PostProcessShader shaderUtilShader : shaderUtilShaders.values())
			shaderUtilShader.close();
		shaderUtilShaders.clear();
	}
	
	@Override
	public void addPass(Identifier passId, PostProcessShader shader) {
		shaderUtilShaders.put(passId, shader);
	}
	
	@Override
	public HashMap<Identifier, PostProcessShader> getPasses() {
		return shaderUtilShaders;
	}
	
	@Override
	public void clearPasses() {
		shaderUtilShaders.clear();
	}
	
	@Override
	public void removeLast() {
		passes.remove(passes.size() - 1);
	}
	
	@Override
	public PostProcessShader removePass(Identifier passId) {
		return shaderUtilShaders.remove(passId);
	}
	
	@Override
	public void removePass(PostProcessShader pass) {
		passes.remove(pass);
	}
	
	@Override
	public void setProjectionMatrix(Matrix4f matrix) {
		this.projectionMatrix = matrix;
	}
	
//	@Inject(at = @At("HEAD"), method = "render")
//	public void tellShadersNo(float tickDelta, CallbackInfo ci) {
//		if (!shaderUtilShaders.isEmpty()) {
//			Framebuffer target1 = getTarget("swap");
////		Framebuffer target2 = getTarget("swap2");
//			PostProcessShader endShader = passes.get(passes.size() - 1);
////		((PostProcessShaderAccessor)endShader).setOutput(endShader.input == target1 ? target2 : target1);
//			endTarg = endShader.output;
//			((PostProcessShaderAccessor) endShader).setOutput(target1);
//		}
//	}
}
