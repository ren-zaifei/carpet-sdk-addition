package com.renzaifei.carpetsdkaddition.mixin.brain;


import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;


@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {

    @Unique
    private static boolean check(){
        return CarpetSDKAdditionSettings.betterPiglinAI;
    }


    @Inject(method = "getNearestZombifiedPiglin",at = @At("HEAD"),cancellable = true)
    private static void ongetNearestZombifiedPiglin(PiglinEntity piglin, CallbackInfoReturnable<Boolean> cir){
        if (check()){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "makeRandomWanderTask",at = @At("HEAD"),cancellable = true)
    private static void onmakeRandomWanderTask(CallbackInfoReturnable<RandomTask<PiglinEntity>> cir){
        if (check()){
            RandomTask<PiglinEntity> ranT =  new RandomTask<>(ImmutableList.of(
                    Pair.of(new WaitTask(30, 60), 1)
            ));
            cir.setReturnValue(ranT);
        }
    }
    @Inject(method = "canGather" ,at = @At("HEAD"),cancellable = true)
    private static void oncanGather(PiglinEntity piglin, ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if (!check())return;
        if (piglin.isBaby()) {
            cir.setReturnValue(false);
            return;
        }
        if (stack.isOf(Items.GOLD_INGOT)) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }
    @Inject(method = "groupRunAwayFrom",at = @At("HEAD"),cancellable = true)
    private static void ongroupRunAwayFrom(PiglinEntity piglin, LivingEntity target, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "getPreferredTarget",at = @At("HEAD"),cancellable = true)
    private static void ongetPreferredTarget(PiglinEntity piglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir){
        if (check()){
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(method = "isPreferredAttackTarget",at = @At("HEAD"),cancellable = true)
    private static void onisPreferredAttackTarget(PiglinEntity piglin, LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (check()){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "isGoldHoldingPlayer",at = @At("HEAD"),cancellable = true)
    private static void onisGoldHoldingPlayer(LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (check()){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "isHuntingTarget",at = @At("HEAD"),cancellable = true)
    private static void onisHuntingTarget(LivingEntity piglin, LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (check()){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "onGuardedBlockInteracted",at = @At("HEAD"),cancellable = true)
    private static void onGuardedBlockInteracted(PlayerEntity player, boolean blockOpen, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "wearsGoldArmor",at = @At("HEAD"),cancellable = true)
    private static void wearsGoldArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if (check()){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "onAttacked",at = @At("HEAD"),cancellable = true)
    private static void onAttacked(PiglinEntity piglin, LivingEntity attacker, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "tryRevenge",at = @At("HEAD"),cancellable = true)
    private static void tryRevenge(AbstractPiglinEntity piglin, LivingEntity target, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "becomeAngryWith",at = @At("HEAD"),cancellable = true)
    private static void becomeAngryWith(AbstractPiglinEntity piglin, LivingEntity target, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "angerNearbyPiglins",at = @At("HEAD"),cancellable = true)
    private static void angerNearbyPiglins(AbstractPiglinEntity piglin, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "angerAtCloserTargets",at = @At("HEAD"),cancellable = true)
    private static void angerAtCloserTargets(AbstractPiglinEntity piglin, LivingEntity target, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "becomeAngryWithPlayer",at = @At("HEAD"),cancellable = true)
    private static void becomeAngryWithPlayer(AbstractPiglinEntity piglin, LivingEntity player, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "runAwayFromClosestTarget",at = @At("HEAD"),cancellable = true)
    private static void runAwayFromClosestTarget(PiglinEntity piglin, LivingEntity target, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "runAwayFrom",at = @At("HEAD"),cancellable = true)
    private static void onrunAwayFrom(PiglinEntity piglin, LivingEntity target, CallbackInfo ci){
        if (check()){
            ci.cancel();
        }
    }
    @Inject(method = "canWander",at = @At("HEAD"),cancellable = true)
    private static void oncanWander(LivingEntity piglin, CallbackInfoReturnable<Boolean> cir){
        if (check()){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "findGround",at = @At("HEAD"),cancellable = true)
    private static void findGround(PiglinEntity piglin, CallbackInfoReturnable<Vec3d> cir){
        if (check()){
            cir.setReturnValue(piglin.getPos());
        }
    }
}
