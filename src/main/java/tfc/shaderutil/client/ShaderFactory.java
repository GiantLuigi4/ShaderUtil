package tfc.shaderutil.client;

import net.minecraft.client.render.Shader;
import net.minecraft.resource.ResourceFactory;

import java.io.IOException;

@FunctionalInterface
public interface ShaderFactory<T> {
	T create(ResourceFactory factory, String name) throws IOException;
}
