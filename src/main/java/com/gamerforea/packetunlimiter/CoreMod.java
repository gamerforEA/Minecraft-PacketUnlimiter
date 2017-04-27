package com.gamerforea.packetunlimiter;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.common.config.Configuration;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.Name(CoreMod.NAME)
@IFMLLoadingPlugin.SortingIndex(1001)
public final class CoreMod implements IFMLLoadingPlugin
{
	public static final String MODID = "PacketUnlimiter";
	public static final String NAME = "PacketUnlimiter";
	public static final String VERSION = "1.0";

	public static boolean isObfuscated = false;
	public static boolean bigPacketWarning = false;

	public CoreMod()
	{
		Configuration config = new Configuration(new File("config", NAME + ".cfg"));
		config.load();
		bigPacketWarning = config.getBoolean("bigPacketWarning", "general", true, "Включить оповещение при превышении стандартного лимита (2 MB)");
		config.save();
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { "com.gamerforea.packetunlimiter.asm.ASMTransformer" };
	}

	@Override
	public String getModContainerClass()
	{
		return "com.gamerforea.packetunlimiter.ModContainer";
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		isObfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
