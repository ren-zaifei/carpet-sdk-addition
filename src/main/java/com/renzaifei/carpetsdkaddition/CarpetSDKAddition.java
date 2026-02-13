package com.renzaifei.carpetsdkaddition;

import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;


public class CarpetSDKAddition implements ModInitializer{
	public static final String MOD_ID = "carpet-sdk-addition";
	public static final String MOD_NAME = "Carpet SDK Addition";
	public static String version;

	@Override
	public void onInitialize() {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
		CarpetServer.manageExtension(new CarpetSDKAdditionExtension());
	}
}