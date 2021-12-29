package tfc.shaderutil.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Matrix4f;

public class BufferUtils {
	public static void drawBuffer(Framebuffer framebuffer, boolean drawColor) {
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
		GlStateManager._colorMask(true, true, true, false);
		GlStateManager._disableDepthTest();
		GlStateManager._depthMask(false);
		int width = framebuffer.viewportWidth;
		int textureWidth = framebuffer.textureWidth;
		int height = framebuffer.viewportHeight;
		int textureHeight = framebuffer.textureHeight;
		GlStateManager._viewport(0, 0, width, height);
		GlStateManager._disableBlend();
		
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		Shader shader = minecraftClient.gameRenderer.blitScreenShader;
		if (drawColor) shader.addSampler("DiffuseSampler", framebuffer.getColorAttachment());
		else shader.addSampler("DiffuseSampler", framebuffer.getDepthAttachment());
		Matrix4f matrix4f = Matrix4f.projectionMatrix((float) width, (float) (-height), 1000.0F, 3000.0F);
		RenderSystem.setProjectionMatrix(matrix4f);
		if (shader.modelViewMat != null) {
			shader.modelViewMat.set(Matrix4f.translate(0.0F, 0.0F, -2000.0F));
		}
		
		if (shader.projectionMat != null) {
			shader.projectionMat.set(matrix4f);
		}
		
		shader.bind();
		float f = (float) width;
		float g = (float) height;
		float h = (float) width / (float) textureWidth;
		float i = (float) height / (float) textureHeight;
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferBuilder.vertex(0.0D, (double) g, 0.0D).texture(0.0F, 0.0F).color(255, 255, 255, 255).next();
		bufferBuilder.vertex((double) f, (double) g, 0.0D).texture(h, 0.0F).color(255, 255, 255, 255).next();
		bufferBuilder.vertex((double) f, 0.0D, 0.0D).texture(h, i).color(255, 255, 255, 255).next();
		bufferBuilder.vertex(0.0D, 0.0D, 0.0D).texture(0.0F, i).color(255, 255, 255, 255).next();
		bufferBuilder.end();
		BufferRenderer.postDraw(bufferBuilder);
		shader.unbind();
		GlStateManager._depthMask(true);
		GlStateManager._colorMask(true, true, true, true);
	}
}
