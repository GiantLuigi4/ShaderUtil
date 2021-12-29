package tfc.shaderutil.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.*;
import net.minecraft.client.util.ScreenshotRecorder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TargetAttacher {
	public static Framebuffer depthBuffer = new SimpleFramebuffer(10, 10, true, MinecraftClient.IS_SYSTEM_MAC);
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void attachTargets(ShaderEffect dummyEffect, PostProcessShader shader1) {
		Framebuffer framebuffer = depthBuffer;
//		Framebuffer framebuffer = ((MinecraftAccessor)MinecraftClient.getInstance()).getVanillaBuffer();
//		Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
//		Framebuffer framebuffer = ((ShaderEffectAccessor)dummyEffect).getShaderTarget("minecraft:main");
		shader1.addAuxTarget(
				"DiffuseDepthSampler",
				framebuffer::getDepthAttachment,
				framebuffer.textureWidth,
				framebuffer.textureHeight
		);
	}
	
	public static void mergeDepth(Framebuffer buffer0, Framebuffer buffer1) {
		// TODO:
	}
}
