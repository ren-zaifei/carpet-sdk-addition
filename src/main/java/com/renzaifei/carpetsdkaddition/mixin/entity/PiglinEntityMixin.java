package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinEntity.class)
public class PiglinEntityMixin {

    @Inject(method = "canHunt", at = @At("HEAD"), cancellable = true)
    private void onCanHunt(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetSDKAdditionSettings.betterPiglinAI) {
            cir.setReturnValue(false);
        }
    }
}
