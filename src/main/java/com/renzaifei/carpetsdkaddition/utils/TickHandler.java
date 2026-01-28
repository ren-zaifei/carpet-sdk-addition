package com.renzaifei.carpetsdkaddition.utils;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class TickHandler {
    private int tickCounter = 0;
    public TickHandler() {ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);}

    private boolean piglinCheck;

    private void onServerTick(MinecraftServer server) {
        tickCounter++;
        if (tickCounter == 10) {
            piglinRuleCheck(server,true);
        }
        if (tickCounter == 20){
            piglinRuleCheck(server,false);
        }
    }

    private void piglinRuleCheck(MinecraftServer server,boolean check) {
        if (check) {
            piglinCheck = CarpetSDKAdditionSettings.betterPiglinAI;
        }
        if (piglinCheck == CarpetSDKAdditionSettings.betterPiglinAI){

        }
    }
}
