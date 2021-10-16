package tfc.shaderutil.client.api;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class ItemShaderTools {
	private static final ArrayList<BiFunction<ItemStack, Boolean, RenderLayer>> functions = new ArrayList<>();
	
	public static RenderLayer getFor(ItemStack stack, boolean isDirect) {
		for (BiFunction<ItemStack, Boolean, RenderLayer> function : functions) {
			RenderLayer layer = function.apply(stack, isDirect);
			if (layer != null) {
				return layer;
			}
		}
		return null;
	}
	
	public static void registerLayerFunction(BiFunction<ItemStack, Boolean, RenderLayer> function) {
		functions.add(function);
	}
}
