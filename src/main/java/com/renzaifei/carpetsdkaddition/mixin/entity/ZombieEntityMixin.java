package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieEntityMixin {

    //猪灵不寻路海龟蛋
    @Inject(method = "registerGoals",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Zombie;addBehaviourGoals()V",
                    shift = At.Shift.BEFORE
            )
    )
    private void onregisterGoals(CallbackInfo ci) {
        if (CarpetSDKAdditionSettings.cancelZombifiedPiglinsBreakEgg){
            Zombie zombie = (Zombie)(Object)this;
            if (zombie instanceof ZombifiedPiglin){
                zombie.goalSelector.getAvailableGoals().removeIf( goal ->
                    goal.getGoal() instanceof Zombie.ZombieAttackTurtleEggGoal);
            }
        }
    }
}
