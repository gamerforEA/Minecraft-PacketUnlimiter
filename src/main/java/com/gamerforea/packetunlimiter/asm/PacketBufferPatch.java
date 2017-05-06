package com.gamerforea.packetunlimiter.asm;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.gamerforea.packetunlimiter.CoreMod;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public final class PacketBufferPatch
{
	public static void writeNBTTagCompoundToBuffer(PacketBuffer buf, NBTTagCompound nbt) throws IOException
	{
		if (nbt == null)
			buf.writeShort(-1);
		else
		{
			byte[] arr = CompressedStreamTools.compress(nbt);
			if (arr.length < Short.MAX_VALUE)
			{
				buf.writeShort(arr.length);
				buf.writeBytes(arr);
			}
			else
			{
				byte[] arr1 = Arrays.copyOfRange(arr, 0, Short.MAX_VALUE);
				buf.writeShort(arr1.length);
				buf.writeBytes(arr1);
				byte[] arr2 = arr.length == Short.MAX_VALUE ? new byte[0] : Arrays.copyOfRange(arr, Short.MAX_VALUE, arr.length);
				buf.writeInt(arr2.length);
				buf.writeBytes(arr2);

				if (CoreMod.bigPacketWarning)
					FMLRelaunchLog.warning("[PacketUnlimiter] Sending packet with size " + arr.length + " bytes.");
			}
		}
	}

	public static NBTTagCompound readNBTTagCompoundFromBuffer(PacketBuffer buf) throws IOException
	{
		short length = buf.readShort();
		if (length < 0)
			return null;
		else
		{
			byte[] arr = new byte[length];
			buf.readBytes(arr);

			if (length == Short.MAX_VALUE)
			{
				int length1 = buf.readInt();
				byte[] arr1 = new byte[length1];
				buf.readBytes(arr1);
				arr = ArrayUtils.addAll(arr, arr1);
			}

			return CompressedStreamTools.func_152457_a(arr, new NBTSizeTracker(CoreMod.unpackNbtLimit ? 2097152 : Long.MAX_VALUE));
		}
	}
}
