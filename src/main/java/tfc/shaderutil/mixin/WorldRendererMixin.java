package tfc.shaderutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.shaderutil.client.util.TargetAttacher;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Inject(at = @At("TAIL"), method = "render")
	public void postRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		TargetAttacher.depthBuffer.copyDepthFrom(MinecraftClient.getInstance().getFramebuffer());
		MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
	}
}
