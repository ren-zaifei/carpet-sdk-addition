package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.renzaifei.carpetsdkaddition.access.ArmorStandAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
//#if MC >= 12102
//$$ import net.minecraft.server.level.ServerLevel;
//#endif
//#if MC >= 12110
//$$ import net.minecraft.world.level.storage.ValueInput;
//$$ import net.minecraft.world.level.storage.ValueOutput;
//#endif
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
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

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("RETURN")
    )
    //#if MC < 12110
    private void postAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (this.sitEntity) {
            nbt.putBoolean("SitEntity", true);
        }
    }
    //#else
    //$$ private void postAddAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {if (this.sitEntity) {valueOutput.putBoolean("SitEntity", true);}}
    //#endif


    @Inject(
            method = "readAdditionalSaveData",
            at = @At("RETURN")
    )
    //#if MC < 12105
    private void postReadAdditionalSaveData(@NotNull CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("SitEntity", Tag.TAG_BYTE)) {
            this.sitEntity = nbt.getBoolean("SitEntity");
        }
    }
    //#elseif MC < 12110
    //$$ private void postReadAdditionalSaveData(@NotNull CompoundTag nbt, CallbackInfo ci) {
    //$$        if (nbt.contains("SitEntity")) {
    //$$            this.sitEntity = nbt.getBooleanOr("SitEntity",false);
    //$$        }
    //$$    }
    //#else
    //$$ private void postReadAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
    //$$        this.sitEntity = valueInput.getBooleanOr("SitEntity", false);
    //$$        if (this.sitEntity) {
    //$$            this.sit(true);
    //$$        }
    //$$    }
    //#endif
}
