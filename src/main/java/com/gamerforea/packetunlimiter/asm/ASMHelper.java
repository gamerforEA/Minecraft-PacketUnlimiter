package com.gamerforea.packetunlimiter.asm;

import com.gamerforea.packetunlimiter.CoreMod;
import com.google.common.collect.ImmutableMap;

public final class ASMHelper
{
	private static final ImmutableMap<String, String> methods;

	static
	{
		ImmutableMap.Builder<String, String> builderMethods = ImmutableMap.builder();
		builderMethods.put("net.minecraft.network.PacketBuffer.writeNBTTagCompoundToBuffer", "func_150786_a");
		builderMethods.put("net.minecraft.network.PacketBuffer.readNBTTagCompoundFromBuffer", "func_150793_b");
		methods = builderMethods.build();
	}

	public static String getMethod(String method)
	{
		return CoreMod.isObfuscated ? methods.get(method) : method.substring(method.lastIndexOf('.') + 1);
	}
}
