package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.renzaifei.carpetsdkaddition.access.ArmorStandAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends LivingEntity implements ArmorStandAccess {
    //玩家坐下依旧从PCA移植

    @Unique
    private boolean sitEntity = false;

    protected ArmorStandMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract void setMarker(boolean bl);

    @Shadow
    public abstract void setInvisible(boolean bl);

    @Override
    public boolean isSitting() {
        return this.sitEntity;
    }

    @Override
    public void sit(boolean isSitting) {
        this.sitEntity = isSitting;
        this.setMarker(isSitting);
        this.setInvisible(isSitting);
    }

    @Override
    @Intrinsic
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
    }

    @SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference", "target"})
    @Inject(
            method = "removePassenger(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("HEAD")
    )
    private void preRemovePassenger(Entity passenger, CallbackInfo ci) {
        if (this.isSitting()) {
            this.setPos(passenger.getX(), passenger.getY() + 1, passenger.getZ());
            this.kill();
        }
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("RETURN")
    )
    private void postAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (this.sitEntity) {
            nbt.putBoolean("SitEntity", true);
        }
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("RETURN")
    )
    private void postReadAdditionalSaveData(@NotNull CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("SitEntity", Tag.TAG_BYTE)) {
            this.sitEntity = nbt.getBoolean("SitEntity");
        }
    }
}
