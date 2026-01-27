package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.access.PiglinEntityAccess;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinEntity.class)
public class PiglinEntityMixin implements PiglinEntityAccess {

    @Inject(method = "canHunt", at = @At("HEAD"), cancellable = true)
    private void onCanHunt(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetSDKAdditionSettings.betterPiglinAI && this.isHasGoldenCarrot()) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean hasGoldenCarrot;

    @Override
    public boolean isHasGoldenCarrot() {
        return this.hasGoldenCarrot;
    }

    @Override
    public void setHasGoldenCarrot(boolean hasGoldenCarrot) {
        this.hasGoldenCarrot = hasGoldenCarrot;
    }
}
