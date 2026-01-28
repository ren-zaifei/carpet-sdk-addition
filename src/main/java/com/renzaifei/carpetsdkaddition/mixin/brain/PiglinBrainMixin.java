package com.renzaifei.carpetsdkaddition.mixin.brain;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.access.PiglinEntityAccess;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {

    @Unique
    private static boolean check(){
        return CarpetSDKAdditionSettings.betterPiglinAI;
    }

    @Inject(method = "canGather" ,at = @At("HEAD"),cancellable = true)
    private static void oncanGather(PiglinEntity piglin, ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if (!check())return;
        if (piglin.isBaby()) {
            cir.setReturnValue(false);
            return;
        }
        PiglinEntityAccess access = (PiglinEntityAccess)piglin;
        if (!access.isHasGoldenCarrot()){
            if (stack.isOf(Items.GOLDEN_CARROT)){
                cir.setReturnValue(true);
            }
            return;
        }
        if (stack.isOf(Items.GOLD_INGOT)) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }
    @Inject(method = "barterItem",at = @At("HEAD"))
    private static void onbarterItem(PiglinEntity piglin, ItemStack stack, CallbackInfo ci){
        if (!check())return;
        PiglinEntityAccess access = (PiglinEntityAccess)piglin;
        if (!stack.isOf(Items.GOLDEN_CARROT)) return;
        access.setHasGoldenCarrot(true);
    }
    @Inject(method = "tickActivities",at = @At("HEAD"),cancellable = true)
    private static void ontickActivities(PiglinEntity piglin, CallbackInfo ci){
        if (!check())return;
        PiglinEntityAccess access = (PiglinEntityAccess)piglin;
        if (access.isHasGoldenCarrot()){
            ci.cancel();
            return;
        }
        boolean has = piglin.getInventory().getStack(0).isOf(Items.GOLDEN_CARROT);
        if (has){
            access.setHasGoldenCarrot(true);
            ci.cancel();
        }
    }
}
