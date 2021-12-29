package tfc.shaderutil.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import tfc.shaderutil.client.util.TargetAttacher;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {
//	@Overwrite
//	public static NativeImage takeScreenshot(Framebuffer framebuffer) {
//		int i = framebuffer.textureWidth;
//		int j = framebuffer.textureHeight;
//		framebuffer = TargetAttacher.depthBuffer;
//		NativeImage nativeImage = new NativeImage(i, j, false);
//		RenderSystem.bindTexture(framebuffer.getDepthAttachment());
//		nativeImage.loadFromTextureImage(0, true);
//		nativeImage.mirrorVertically();
//		return nativeImage;
//	}
}
