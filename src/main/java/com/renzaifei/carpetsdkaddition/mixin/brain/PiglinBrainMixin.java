package com.renzaifei.carpetsdkaddition.mixin.brain;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.access.PiglinEntityAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(PiglinAi.class)
public abstract class PiglinBrainMixin {

    @Unique
    private static boolean check(){
        return CarpetSDKAdditionSettings.betterPiglinAI;
    }

    @Inject(method = "wantsToPickup" ,at = @At("HEAD"),cancellable = true)
    private static void onwantsToPickup(Piglin piglin, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir){
        if (!check())return;
        if (piglin.isBaby()) {
            cir.setReturnValue(false);
            return;
        }
        PiglinEntityAccess access = (PiglinEntityAccess)piglin;
        if (!access.isHasGoldenCarrot()){
            if (itemStack.is(Items.GOLDEN_CARROT)){
                cir.setReturnValue(true);
            }
            return;
        }
        if (itemStack.is(Items.GOLD_INGOT)) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }

    @Inject(method = "pickUpItem",at = @At("HEAD"))
    //#if MC <= 12101
    private static void onpickUpItem(Piglin piglin, ItemEntity itemEntity, CallbackInfo ci){
    //#else
    //$$ private static void onpickUpItem(ServerLevel serverLevel,Piglin piglin, ItemEntity itemEntity, CallbackInfo ci){
    //#endif
        if (!check())return;
        PiglinEntityAccess access = (PiglinEntityAccess)piglin;
        if (!itemEntity.getItem().is(Items.GOLDEN_CARROT)) return;
        access.setHasGoldenCarrot(true);
    }

    @Inject(method = "updateActivity",at = @At("HEAD"),cancellable = true)
    private static void onupdateActivity(Piglin piglin, CallbackInfo ci){
        if (!check())return;
        PiglinEntityAccess access = (PiglinEntityAccess)piglin;
        if (access.isHasGoldenCarrot()){
            ci.cancel();
            return;
        }
        boolean has = piglin.getInventory().getItem(0).is(Items.GOLDEN_CARROT);
        if (has){
            access.setHasGoldenCarrot(true);
            ci.cancel();
        }
    }
}
