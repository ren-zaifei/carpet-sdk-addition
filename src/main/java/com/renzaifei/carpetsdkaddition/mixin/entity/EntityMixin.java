package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract EntityType<?> getType();


    @Inject(method = "move",at = @At(value = "HEAD"),cancellable = true)
    private void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci){
        if (CarpetSDKAdditionSettings.betterPiglinAI){
            if (this.getType() == EntityType.PIGLIN){
                ci.cancel();
            }
        }
    }
}
