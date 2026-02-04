package com.renzaifei.carpetsdkaddition.mixin.brain;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.access.PiglinEntityAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class BrainMixin {
    @Inject(method = "tickSensors",at = @At(value = "HEAD"),cancellable = true)
    private void onTickSensors(ServerLevel serverLevel, LivingEntity livingEntity, CallbackInfo ci){
        if (CarpetSDKAdditionSettings.betterPiglinAI){
            if (livingEntity.getType() ==  EntityType.PIGLIN ){
                PiglinEntityAccess access = (PiglinEntityAccess)livingEntity;
                if (access.isHasGoldenCarrot()){
                    ci.cancel();
                }
            }
        }
    }
}
