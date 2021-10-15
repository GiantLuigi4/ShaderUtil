package tfc.shaderutil.client;

import net.minecraft.client.gl.ShaderParseException;
import net.minecraft.client.render.Shader;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class CoreShaderRegistry {
	protected static HashMap<Identifier, Shader> initializedShaders = new HashMap<>();
	protected static HashMap<Identifier, ShaderFactory<Shader>> registeredFactories = new HashMap<>();
	
	private static final Logger LOGGER = LogManager.getLogger("ShaderUtil");
	
	public static void register(Identifier name, ShaderFactory<Shader> factory) {
		registeredFactories.put(name, factory);
	}
	
	public static ShaderFactory<Shader> getFactory(Identifier name) {
		return registeredFactories.get(name);
	}
	
	public static Shader getShader(Identifier id) {
		return initializedShaders.get(id);
	}
	
	public static void reload(ResourceFactory factory) {
		initializedShaders.clear();
		for (Identifier identifier : registeredFactories.keySet()) {
			try {
				System.out.println("Initializing core shader: " + identifier.toString());
				initializedShaders.put(identifier, registeredFactories.get(identifier).create(factory, identifier.toString()));
				System.out.println("Successfully initialized core shader: " + identifier.toString());
			} catch (Throwable err) {
				LOGGER.error("Error while initializing shader: " + identifier.toString());
				if (!(err instanceof ShaderParseException)) {
					LOGGER.fatal("Exception of an unexpected type");
					if (err instanceof RuntimeException) throw (RuntimeException) err;
					else {
						CrashReport report = CrashReport.create(err, "Registering shaders");
						CrashReportSection section = report.addElement("Shader being loaded");
						section.add("Namespace: ", identifier.getNamespace());
						section.add("Path: ", identifier.getPath());
						section.add("Exception Class: ", err.getClass());
						throw new CrashException(report);
					}
				}
				Throwable cause = err;
				StringBuilder exception = new StringBuilder();
				// heck you too mojang, I don't care about your stupid wrapper which consumes the actual issue
				if (cause instanceof ShaderParseException || cause instanceof RuntimeException) {
					cause = cause.getCause();
				}
				exception.append(cause.getClass().getName()).append(" : ").append(cause.getMessage());
				for (StackTraceElement stackTraceElement : cause.getStackTrace()) exception.append("\n  ").append(stackTraceElement.toString());
				LOGGER.error(exception.toString());
			}
		}
	}
}
