package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {
    @Redirect(method = "initGoals",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"))
    private void redirectGoalSelector(GoalSelector instance, int priority, Goal goal) {
        if (CarpetSDKAdditionSettings.cancelZombifiedPiglinsBreakEgg){
            if (!((ZombieEntity)(Object)this instanceof ZombifiedPiglinEntity)) return;
            return;
        }
        instance.add(priority, goal);
    }
}
