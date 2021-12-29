package tfc.shaderutil.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.shaderutil.client.util.GLTracker;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {
	@Inject(at = @At("HEAD"), method = "_glBindFramebuffer")
	private static void preBindFrameBuffer(int i, int j, CallbackInfo ci) {
		GLTracker.setBound(i, j);
	}
	
	@Inject(at = @At("HEAD"), method = "_viewport")
	private static void preViewport(int i, int j, int k, int l, CallbackInfo ci) {
		GLTracker.lvx(i);
		GLTracker.lvy(j);
		GLTracker.rvx(k);
		GLTracker.rvy(l);
	}
}
