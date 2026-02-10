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

	//掉落物高亮显示
	@Rule(categories = {SDK})
	public static boolean hightLightItem = false;

	//生物高亮显示
	@Rule(categories = {SDK})
	public static boolean hightLightLivingEntity = false;

	//#if MC >= 12100 && MC <= 12101
	//修复末影珍珠传送BUG
	@Rule(categories = {SDK})
	public static boolean fixEnderPearlTeleport = false;
	//#endif

	//僵尸猪灵不踩碎海龟蛋
	@Rule(categories = {SDK})
	public static boolean cancelZombifiedPiglinsBreakEgg = false;

	//重新引入旧版寻路
	@Rule(categories = {SDK})
	public static boolean reintroduceOlderPathfinding = false;

	//玩家坐下
	@Rule(categories = {SDK})
	public static boolean playerCanSit = false;

	//重新引入旧版潜影贝飞弹
	@Rule(categories = {SDK})
	public static boolean reintroduceOlderShulkerBullet = false;

}