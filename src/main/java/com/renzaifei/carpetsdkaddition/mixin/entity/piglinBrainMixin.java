package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class piglinBrainMixin {

    @Shadow
    private static void addAdmireItemActivities(Brain<PiglinEntity> brain){}
    @Shadow
    private static void addCoreActivities(Brain<PiglinEntity> piglin){}
    @Shadow
    private static void addAvoidActivities(Brain<PiglinEntity> brain){}
    @Shadow
    private static Task<PathAwareEntity> makeGoToSoulFireTask(){ return null;}
    @Shadow
    private static boolean shouldRunAwayFromHoglins(PiglinEntity piglin){ return false;}
    @Inject(method = "addAvoidActivities",at = @At("HEAD"),cancellable = true)
    private static void onaddAvoidActivities(Brain<PiglinEntity> brain, CallbackInfo ci){
        if (CarpetSDKAdditionSettings.betterPiglinAI){
            brain.setTaskList(Activity.AVOID, 10, ImmutableList.of(GoToRememberedPositionTask.createEntityBased(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), makeGoToSoulFireTask(),ForgetTask.create(piglinBrainMixin::shouldRunAwayFromHoglins, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
            ci.cancel();
        }
    }

    @Inject(method = "create",at = @At("HEAD"),cancellable = true)
    private static void oncreate(PiglinEntity piglin, Brain<PiglinEntity> brain, CallbackInfoReturnable<Brain<?>> cir){
        if (CarpetSDKAdditionSettings.betterPiglinAI){
            addAdmireItemActivities(brain);
            addCoreActivities(brain);
            addAvoidActivities(brain);
            brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
            cir.setReturnValue(brain);
        }
    }
}
