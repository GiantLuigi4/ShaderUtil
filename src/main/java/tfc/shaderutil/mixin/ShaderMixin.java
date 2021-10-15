package tfc.shaderutil.mixin;

import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Shader.class)
public class ShaderMixin {
	@Shadow
	@Final
	private String name;
	protected Identifier trueId;

//	@ModifyVariable(
//			method = "<init>",
//			at = @At(value = "NEW", ordinal = 0)
//	)
//	public void preInit(Args args) {
//		String name = args.get(1);
//		Identifier nameId = new Identifier(name);
//		trueId = new Identifier(
//				nameId.getNamespace(),
//				"shaders/core/" + nameId.getPath() + ".json"
//		);
//		args.set(1, nameId.getPath());
//	}
	
	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
	public void preMakeResourceName(ResourceFactory resourceFactory, String string, VertexFormat vertexFormat, CallbackInfo ci) {
		Identifier nameId = new Identifier(string);
		trueId = new Identifier(
				nameId.getNamespace(),
				"shaders/core/" + nameId.getPath() + ".json"
		);
	}
	
	@ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
	public void modifyId(Args args) {
		args.set(0, trueId.toString());
	}
	
	@ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceFactory;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"))
	public void modifyResourceName(@NotNull Args args) {
		args.set(0, trueId);
	}
	
	@ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/ShaderParseException;addFaultyFile(Ljava/lang/String;)V"))
	public void modifyFileName(Args args) {
		args.set(0, trueId.toString());
	}
	
	@Unique
	private static Identifier idLoad = null;
	
	@ModifyArgs(method = "loadProgram", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
	private static void modifyFileId(Args args) {
		if (!args.get(0).toString().contains(":")) idLoad = null;
		else {
			String name = args.get(0).toString().substring("shaders/core/".length());
			String nameWithType = name;
			name = name.substring(0, name.length() - (".vsh".length()));
			Identifier nameId = new Identifier(name);
			idLoad = new Identifier(
					nameId.getNamespace(),
					"shaders/core/" + nameId.getPath() + nameWithType.substring(name.length())
			);
			args.set(0, idLoad.toString());
		}
	}
	
	@ModifyArgs(method = "loadProgram", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FileNameUtil;getPosixFullPath(Ljava/lang/String;)Ljava/lang/String;"))
	private static void modifyFileIdkWhatToCallThisMethodLOL(Args args) {
		if (idLoad == null) return;
		args.set(0, idLoad.toString());
	}
}
