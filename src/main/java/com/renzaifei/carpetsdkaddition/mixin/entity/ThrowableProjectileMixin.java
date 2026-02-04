package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Projectile.class)
public abstract class ThrowableProjectileMixin {
    //#if MC >= 12100 && MC <= 12101

    @Shadow
    @Nullable
    private Entity cachedOwner;

    @Shadow
    @Nullable
    private UUID ownerUUID;


    @Inject(method = "getOwner",at = @At("HEAD"),cancellable = true)
    private void getOwner(CallbackInfoReturnable<Entity> cir){
        if (CarpetSDKAdditionSettings.fixEnderPearlTeleport){
            if ((Object)this instanceof ThrownEnderpearl){
                if (this.ownerUUID != null && ((Projectile)(Object)this).level() instanceof ServerLevel serverWorld) {
                    Entity entity = serverWorld.getEntity(this.ownerUUID);
                    if (entity == null) {
                        entity = serverWorld.getServer().getPlayerList().getPlayer(this.ownerUUID);
                    }
                    this.cachedOwner = entity;
                    cir.setReturnValue(entity);
                }
            }
        }
    }
    //#endif
}
