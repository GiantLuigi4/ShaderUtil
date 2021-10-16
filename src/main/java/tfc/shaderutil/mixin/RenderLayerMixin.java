package tfc.shaderutil.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.shaderutil.client.api.ItemShaderTools;

@Mixin(RenderLayers.class)
public class RenderLayerMixin {
	@Inject(at = @At("HEAD"), method = "getItemLayer", cancellable = true)
	private static void preGetLayer(ItemStack stack, boolean direct, CallbackInfoReturnable<RenderLayer> cir) {
		RenderLayer layer = ItemShaderTools.getFor(stack, direct);
		if (layer != null) cir.setReturnValue(layer);
	}
}
