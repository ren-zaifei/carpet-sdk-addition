package com.renzaifei.carpetsdkaddition;

import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;


public class CarpetSDKAddition implements ModInitializer{
	public static final String MOD_ID = "carpet-sdk-addition";

	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(new CarpetSDKAdditionExtension());
	}
}