package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearl.class)
public class ThrownEnderpearlMixin extends ThrowableItemProjectile {

    public ThrownEnderpearlMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    //#if MC >= 12100 && MC <= 12101
    @Inject(method = "tick",at = @At("HEAD"), cancellable = true)
    private void onEnderPearlEntityTick(CallbackInfo ci) {
        if (CarpetSDKAdditionSettings.fixEnderPearlTeleport){
            ThrownEnderpearl enderpearl = (ThrownEnderpearl)(Object)this;
            Entity entity = enderpearl.getOwner();
            if (entity instanceof ServerPlayer && enderpearl.level().getGameRules().getBoolean(GameRules.RULE_ENDER_PEARLS_VANISH_ON_DEATH)){
                enderpearl.discard();
            } else {
                super.tick();
            }
            ci.cancel();
        }
    }
    //#endif

    @Shadow
    protected Item getDefaultItem() {return null;}


}
