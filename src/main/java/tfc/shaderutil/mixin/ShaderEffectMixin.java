package tfc.shaderutil.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.shaderutil.client.api.FBOBinder;
import tfc.shaderutil.client.util.*;

import java.io.File;
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
	
	@Shadow
	private Matrix4f projectionMatrix;
	@Shadow
	@Final
	private List<Framebuffer> defaultSizedTargets;
	@Shadow
	@Final
	private Framebuffer mainTarget;
	@Shadow
	@Final
	private Map<String, Framebuffer> targetsByName;
	
	@Shadow
	public abstract void addTarget(String name, int width, int height);
	
	@Shadow
	private int width;
	@Shadow
	private int height;
	@Unique
	HashMap<Identifier, PostProcessShader> shaderUtilShaders = new HashMap<>();
	
	@Unique
	Framebuffer endTarg;
	
	@Inject(at = @At("HEAD"), method = "render")
	public void copyDepthBuffer(float tickDelta, CallbackInfo ci) {
//		Framebuffer depth = TargetAttacher.depthBuffer;
//		depth.beginWrite(true);
//		drawBuffer(MinecraftClient.getInstance().getFramebuffer());
//		try {
//			if (!new File("test.png").exists()) {
//				NativeImage img = ScreenshotRecorder.takeScreenshot(MinecraftClient.getInstance().getFramebuffer());
//				img.writeTo(new File("test.png"));
//				img.close();
//			}
//		} catch (Throwable ignored) {
//		}
//		depth.endWrite();
		
//		int target = GLTracker.getBound(36160);
//		int lvx = GLTracker.lvx();
//		int lvy = GLTracker.lvy();
//		int rvx = GLTracker.rvx();
//		int rvy = GLTracker.rvy();
		Framebuffer depth = TargetAttacher.depthBuffer;
		FBOBinder binder = new FBOBinder();
//		depth.beginWrite(true);
//		BufferUtils.drawBuffer(MinecraftClient.getInstance().getFramebuffer(), false);
//		depth.endWrite();
		TargetAttacher.mergeDepth(depth, MinecraftClient.getInstance().getFramebuffer());
		binder.rebind();
//		GlStateManager._glBindFramebuffer(36160, target);
//		GlStateManager._viewport(lvx, lvy, rvx, rvy);
	}
	
	@Inject(at = @At("TAIL"), method = "render")
	public void drawShaderUtilShaders(float tickDelta, CallbackInfo ci) {
		if (shaderUtilShaders.isEmpty()) return;
		
		if (targetsByName.isEmpty()) {
			// currently; if this ever gets called, the game will likely crash
			addTarget("shaderutil:foolproofing", width, height);
		}
		
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
			if (((PostProcessShaderAccessor) shaderUtilShader).getMatrix() != null) {
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
		
		TargetAttacher.depthBuffer.resize(targetsWidth, targetsHeight, MinecraftClient.IS_SYSTEM_MAC);
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
	public PostProcessShader getPass(Identifier passId) {
		return shaderUtilShaders.get(passId);
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
	
	@Override
	public Framebuffer getShaderTarget(String name) {
		return getTarget(name);
	}
}
