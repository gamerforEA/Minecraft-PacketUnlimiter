package com.gamerforea.packetunlimiter.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public final class ASMTransformer implements IClassTransformer, Opcodes
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if (transformedName.equals("net.minecraft.network.PacketBuffer"))
			return patchPacketBuffer(basicClass);

		return basicClass;
	}

	private static byte[] patchPacketBuffer(byte[] basicClass)
	{
		ClassNode cNode = new ClassNode();
		new ClassReader(basicClass).accept(cNode, 0);

		String writeNbt = ASMHelper.getMethod("net.minecraft.network.PacketBuffer.writeNBTTagCompoundToBuffer");
		String readNbt = ASMHelper.getMethod("net.minecraft.network.PacketBuffer.readNBTTagCompoundFromBuffer");
		for (MethodNode mNode : cNode.methods)
			if (mNode.name.equals(writeNbt) && mNode.desc.equals("(Lnet/minecraft/nbt/NBTTagCompound;)V"))
			{
				InsnList insn = new InsnList();
				insn.add(new VarInsnNode(ALOAD, 0));
				insn.add(new VarInsnNode(ALOAD, 1));
				insn.add(new MethodInsnNode(INVOKESTATIC, "com/gamerforea/packetunlimiter/asm/PacketBufferPatch", "writeNBTTagCompoundToBuffer", "(Lnet/minecraft/network/PacketBuffer;Lnet/minecraft/nbt/NBTTagCompound;)V", false));
				insn.add(new InsnNode(RETURN));
				mNode.instructions = insn;
			}
			else if (mNode.name.equals(readNbt) && mNode.desc.equals("()Lnet/minecraft/nbt/NBTTagCompound;"))
			{
				InsnList insn = new InsnList();
				insn.add(new VarInsnNode(ALOAD, 0));
				insn.add(new MethodInsnNode(INVOKESTATIC, "com/gamerforea/packetunlimiter/asm/PacketBufferPatch", "readNBTTagCompoundFromBuffer", "(Lnet/minecraft/network/PacketBuffer;)Lnet/minecraft/nbt/NBTTagCompound;", false));
				insn.add(new InsnNode(ARETURN));
				mNode.instructions = insn;
			}

		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cNode.accept(cWriter);
		return cWriter.toByteArray();
	}
}
