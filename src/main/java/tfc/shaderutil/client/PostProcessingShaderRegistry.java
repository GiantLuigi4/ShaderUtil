//package tfc.shaderutil.client;
//
//import net.minecraft.client.gl.PostProcessShader;
//import net.minecraft.client.gl.ShaderParseException;
//import net.minecraft.client.render.Shader;
//import net.minecraft.resource.ResourceFactory;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.crash.CrashException;
//import net.minecraft.util.crash.CrashReport;
//import net.minecraft.util.crash.CrashReportSection;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.HashMap;
//
//public class PostProcessingShaderRegistry {
//	protected static HashMap<Identifier, PostProcessShader> initializedShaders = new HashMap<>();
//	protected static HashMap<Identifier, ShaderFactory<PostProcessShader>> registeredFactories = new HashMap<>();
//
//	private static final Logger LOGGER = LogManager.getLogger("ShaderUtil");
//
//	public static void register(Identifier name, ShaderFactory<PostProcessShader> factory) {
//		registeredFactories.put(name, factory);
//	}
//
//	public static ShaderFactory<PostProcessShader> getFactory(Identifier name) {
//		return registeredFactories.get(name);
//	}
//
//	public static PostProcessShader getShader(Identifier id) {
//		return initializedShaders.get(id);
//	}
//}
