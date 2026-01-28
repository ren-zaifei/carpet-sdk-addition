package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin{

    @Shadow
    @Nullable
    private Entity owner;

    @Shadow
    @Nullable
    private UUID ownerUuid;

    @Inject(method = "getOwner",at = @At("HEAD"),cancellable = true)
    private void getOwner(CallbackInfoReturnable<Entity> cir){
        if (CarpetSDKAdditionSettings.fixEnderPearlTeleport){
            if ((Object)this instanceof EnderPearlEntity){
                if (this.ownerUuid != null && ((ProjectileEntity)(Object)this).getWorld() instanceof ServerWorld serverWorld) {
                    Entity entity = serverWorld.getEntity(this.ownerUuid);
                    if (entity == null) {
                        entity = serverWorld.getServer().getPlayerManager().getPlayer(this.ownerUuid);
                    }
                    this.owner = entity;
                    cir.setReturnValue(entity);
                }
            }
        }
    }
}
