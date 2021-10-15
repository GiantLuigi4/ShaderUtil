package tfc.shaderutil.mixin;

import net.minecraft.client.gl.JsonEffectGlShader;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(JsonEffectGlShader.class)
public class JsonEffectGlShaderMixin {
	protected Identifier trueId;
	
	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
	public void preMakeResourceName(ResourceManager resourceManager, String string, CallbackInfo ci) {
		Identifier nameId = new Identifier(string);
		trueId = new Identifier(
				nameId.getNamespace(),
				"shaders/program/" + nameId.getPath() + ".json"
		);
	}
	
	@ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
	public void modifyResourceName(Args args) {
		args.set(0, trueId.toString());
	}
	
	@Unique
	private static Identifier idLoad = null;
	
	@ModifyArgs(method = "loadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
	private static void modifyFileId(Args args) {
		if (!args.get(0).toString().contains(":")) idLoad = null;
		else {
			String name = args.get(0).toString().substring("shaders/program/".length());
			String nameWithType = name;
			name = name.substring(0, name.length() - (".vsh".length()));
			Identifier nameId = new Identifier(name);
			idLoad = new Identifier(
					nameId.getNamespace(),
					"shaders/program/" + nameId.getPath() + nameWithType.substring(name.length())
			);
			args.set(0, idLoad.toString());
		}
	}
	
	@ModifyArgs(method = "loadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/EffectProgram;createFromResource(Lnet/minecraft/client/gl/Program$Type;Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;)Lnet/minecraft/client/gl/EffectProgram;"))
	private static void modifyName(Args args) {
		if (idLoad == null) return;
		args.set(1, idLoad.toString());
	}
}
