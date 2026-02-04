package com.renzaifei.carpetsdkaddition.mixin.client;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isCurrentlyGlowing",at = @At("HEAD"),cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetSDKAdditionSettings.hightLightItem){
            if ((Object)this instanceof ItemEntity){
                cir.setReturnValue(true);
            }
        }
    }
}
