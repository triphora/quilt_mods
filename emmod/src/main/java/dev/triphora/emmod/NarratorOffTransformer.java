package dev.triphora.emmod;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.gudenau.lib.unsafe.Unsafe;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.transformers.MixinClassWriter;

// Code from auoeke's narrator-off, licensed LGPL-3.0
public class NarratorOffTransformer implements IMixinConfigPlugin {
	public void onLoad(String mixinPackage) {}
	public String getRefMapperConfig() { return null; }
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) { return true; }
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
	public List<String> getMixins() { return null; }
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	static {
		try {
			var node = new ClassNode();
			//noinspection DataFlowIssue
			new ClassReader(NarratorOffTransformer.class.getResourceAsStream("/com/mojang/text2speech/Narrator.class")).accept(node, 0);

			for (var method : node.methods) {
				if (method.name.equals("getNarrator")) {
					method.instructions.clear();
					method.tryCatchBlocks = null;

					method.visitTypeInsn(Opcodes.NEW, "com/mojang/text2speech/NarratorDummy");
					method.visitInsn(Opcodes.DUP);
					method.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/mojang/text2speech/NarratorDummy", "<init>", "()V", false);
					method.visitInsn(Opcodes.ARETURN);

					break;
				}
			}

			var writer = new MixinClassWriter(ClassWriter.COMPUTE_FRAMES);
			node.accept(writer);
			var bytecode = writer.toByteArray();
			Unsafe.defineClass("com.mojang.text2speech.Narrator", bytecode, 0, bytecode.length, ClassLoader.getSystemClassLoader(), null);
		} catch (IOException exception) {
			throw Unsafe.throwException(exception);
		}
	}
}
