package com.renzaifei.carpetsdkaddition;

import carpet.api.settings.Rule;

public class CarpetSDKAdditionSettings {
	private static final String SDK = "SDK";

	//不死图腾扳手
	@Rule(categories = {SDK})
	public static boolean flippinTotem = false;

	//猪灵AI优化
	@Rule(categories = {SDK})
	public static boolean betterPiglinAI = false;
}