package com.renzaifei.carpetsdkaddition.mixin.brain;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class BrainMixin {
    @Inject(method = "tickSensors",at = @At(value = "HEAD"),cancellable = true)
    private void onTickSensors(ServerWorld world, LivingEntity entity, CallbackInfo ci){
        if (CarpetSDKAdditionSettings.betterPiglinAI){
            if (entity.getType() ==  EntityType.PIGLIN ){
                ci.cancel();
            }
        }
    }
}
