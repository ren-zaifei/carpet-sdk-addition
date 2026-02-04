package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.world.entity.projectile.ThrownEnderpearl.class)
public class ThrownEnderpearlMixin {

    //#if MC >= 12100 && MC <= 12101
    @Redirect(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isAlive()Z"))
    private boolean onEnderPearlEntityTick(Entity instance) {
        if (CarpetSDKAdditionSettings.fixEnderPearlTeleport){
            return true;
        }else {
            return instance.isAlive();
        }

    }
    //#endif
}
