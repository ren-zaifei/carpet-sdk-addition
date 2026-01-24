package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinBrain.class)
public class piglinBrainMixin {

    @Unique
    private static void clearNonTradeMemories(Brain<PiglinEntity> brain) {
        brain.forget(MemoryModuleType.ATTACK_TARGET);
        brain.forget(MemoryModuleType.ANGRY_AT);
        brain.forget(MemoryModuleType.UNIVERSAL_ANGER);
        brain.forget(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        brain.forget(MemoryModuleType.HURT_BY);
        brain.forget(MemoryModuleType.HURT_BY_ENTITY);
        brain.forget(MemoryModuleType.AVOID_TARGET);
        brain.forget(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        brain.forget(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        brain.forget(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
        brain.forget(MemoryModuleType.CELEBRATE_LOCATION);
        brain.forget(MemoryModuleType.DANCING);
        brain.forget(MemoryModuleType.HUNTED_RECENTLY);
        brain.forget(MemoryModuleType.WALK_TARGET);
        brain.forget(MemoryModuleType.LOOK_TARGET);
    }

    //猪灵优化
    @Inject(method = "tickActivities",at = @At("HEAD"),cancellable = true)
    private static void ontickActivities(PiglinEntity piglin, CallbackInfo ci) {
        if (piglin.getInventory().getStack(0).isOf(Items.GOLDEN_CARROT) && CarpetSDKAdditionSettings.betterPiglinAI){
            Brain<PiglinEntity> brain = piglin.getBrain();
            clearNonTradeMemories(brain);
            ci.cancel();
        }
    }
}
