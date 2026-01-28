package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin{
    @Redirect(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isAlive()Z"))
    private boolean onEnderPearlEntityTick(Entity instance) {
        if (CarpetSDKAdditionSettings.fixEnderPearlTeleport){
            return true;
        }else {
            return instance.isAlive();
        }

    }
}
