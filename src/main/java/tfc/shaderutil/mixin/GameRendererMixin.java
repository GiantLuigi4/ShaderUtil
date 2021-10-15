package tfc.shaderutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.gl.ShaderParseException;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.shaderutil.client.CoreShaderRegistry;
import tfc.shaderutil.client.PostProcessUtils;
import tfc.shaderutil.client.util.GameRendererAccessor;
import tfc.shaderutil.client.util.ShaderEffectAccessor;

import java.util.HashMap;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererAccessor {
	@Shadow
	private boolean shadersEnabled;
	
	@Shadow
	@Nullable
	private ShaderEffect shader;
	
	@Shadow
	@Final
	private MinecraftClient client;
	
	@Shadow
	@Final
	private ResourceManager resourceManager;
	
	@Inject(at = @At(value = "TAIL"), method = "loadShaders")
	public void preClear(ResourceManager manager, CallbackInfo ci) {
		CoreShaderRegistry.reload(manager);
	}
	
	@Inject(at = @At("HEAD"), method = "disableShader")
	public void preDisableShaders(CallbackInfo ci) {
		preSwapShaders();
	}
	
	@Inject(at = @At("HEAD"), method = "loadShader(Lnet/minecraft/util/Identifier;)V")
	public void preLoadShader(Identifier id, CallbackInfo ci) {
		preSwapShaders();
	}
	
	@Inject(at = @At("TAIL"), method = "reload")
	public void postReload(ResourceManager manager, CallbackInfo ci) {
		postSwapShaders();
	}
	
	@Inject(at = @At("TAIL"), method = "loadShader(Lnet/minecraft/util/Identifier;)V")
	public void postLoadShader(Identifier id, CallbackInfo ci) {
		postSwapShaders();
	}
	
	@Inject(at = @At("TAIL"), method = "loadShaders")
	public void postLoadShaders(ResourceManager manager, CallbackInfo ci) {
		if (shader == dummyEffect) shader = null;
		if (dummyEffect != null) {
			dummyEffect.close();
		}
		try {
			ShaderEffect tempDummyEffect = new ShaderEffect(
					this.client.getTextureManager(),
					this.resourceManager,
					this.client.getFramebuffer(),
					new Identifier("shaderutil:shaders/post/blit.json")
			);
			tempDummyEffect.setupDimensions(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
			shadersEnabled = false;
			shader = null;
			this.dummyEffect = tempDummyEffect;
//			if (shader == null) shader = dummyEffect;
//			((ShaderEffectAccessor) dummyEffect).setProjectionMatrix(Matrix4f.projectionMatrix(1, 1, 1, 1));
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}
	
	@Inject(at = @At("TAIL"), method = "onCameraEntitySet")
	public void postSetCamEntity(Entity entity, CallbackInfo ci) {
		postSwapShaders();
	}
	
	@Inject(at = @At("HEAD"), method = "onCameraEntitySet")
	public void preSetCamEntity(Entity entity, CallbackInfo ci) {
		preSwapShaders();
	}
	
	@Inject(at = @At("TAIL"), method = "disableShader")
	public void postDisableShaders(CallbackInfo ci) {
		postSwapShaders();
	}
	
	@Inject(at = @At("TAIL"), method = "onResized")
	public void postResize(int width, int height, CallbackInfo ci) {
		if (dummyEffect != null)
			dummyEffect.setupDimensions(width, height);
	}
	
	@Unique
	private void preSwapShaders() {
		if (shader == dummyEffect) {
			shader = null;
			shadersEnabled = false;
		} else {
			if (shader != null)
				((ShaderEffectAccessor) shader).clearPasses();
		}
	}
	
	@Unique
	private void postSwapShaders() {
		if (shader == null) {
			shader = dummyEffect;
			shadersEnabled = true;
		} else {
			HashMap<Identifier, PostProcessShader> shaderMap = ((ShaderEffectAccessor) dummyEffect).getPasses();
			for (Identifier identifier : shaderMap.keySet())
				((ShaderEffectAccessor) shader).addPass(identifier, shaderMap.get(identifier));
		}
	}
	
	@Unique
	ShaderEffect dummyEffect;
	
	@Unique
	private static final Logger LOGGER = LogManager.getLogger("ShaderUtil");
	
	@Inject(at = @At("TAIL"), method = "loadProjectionMatrix")
	public void postLoadProjMat(Matrix4f matrix, CallbackInfo ci) {
//		((ShaderEffectAccessor) dummyEffect).setProjectionMatrix(matrix);
	}
	
	@Override
	public PostProcessShader addPass(Identifier passId, Identifier shader) {
		PostProcessShader shader1 = null;
		try {
			shader1 = dummyEffect.addPass(shader.toString(), client.getFramebuffer(), client.getFramebuffer());
			passes.put(passId, shader1);
			if (shader1 != null) {
				((ShaderEffectAccessor) dummyEffect).addPass(passId, shader1);
				if (this.shader != null && this.shader != dummyEffect)
					((ShaderEffectAccessor) this.shader).addPass(passId, shader1);
			}
			((ShaderEffectAccessor) dummyEffect).removeLast();
		} catch (Throwable err) {
			Throwable cause = err;
			StringBuilder exception = new StringBuilder();
			// heck you too mojang, I don't care about your stupid wrapper which consumes the actual issue
			if (cause instanceof ShaderParseException || cause instanceof RuntimeException) {
				if (cause.getCause() != null) cause = cause.getCause();
			}
			exception.append(cause.getClass().getName()).append(" : ").append(cause.getMessage());
			for (StackTraceElement stackTraceElement : cause.getStackTrace())
				exception.append("\n  ").append(stackTraceElement.toString());
			LOGGER.error(exception.toString());
		}
		
		dummyEffect.setupDimensions(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
		
		return shader1;
	}
	
	@Unique
	HashMap<Identifier, PostProcessShader> passes = new HashMap<>();
	
	@Override
	public void removePass(Identifier passId) {
		passes.remove(passId);
		PostProcessShader shader1 = ((ShaderEffectAccessor) dummyEffect).removePass(passId);
		((ShaderEffectAccessor) dummyEffect).removePass(shader1);
		shader1.close();
		if (shader != dummyEffect) {
			shader1 = ((ShaderEffectAccessor) dummyEffect).removePass(passId);
		}
	}
}
