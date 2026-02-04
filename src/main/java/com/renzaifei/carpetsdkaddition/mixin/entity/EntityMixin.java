package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.access.PiglinEntityAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract EntityType<?> getType();

    //猪灵AI优化
    @Inject(method = "move",at = @At(value = "HEAD"),cancellable = true)
    private void onMove(MoverType moverType, Vec3 vec3, CallbackInfo ci){
        if (CarpetSDKAdditionSettings.betterPiglinAI){
            if (this.getType() != EntityType.PIGLIN)return;
            PiglinEntityAccess access = (PiglinEntityAccess)this;
            if (access.isHasGoldenCarrot()){
                ci.cancel();
            }
        }
    }
}
