package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Zombie.class)
public class ZombieEntityMixin {

    //猪灵不寻路海龟蛋
    @Redirect(method = "registerGoals",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V"))
    private void redirectGoalSelector(GoalSelector instance, int priority, Goal goal) {
        if (CarpetSDKAdditionSettings.cancelZombifiedPiglinsBreakEgg){
            if (!(goal instanceof Zombie.ZombieAttackTurtleEggGoal)){
                instance.addGoal(priority,goal);
            }
            return;
        }
        instance.addGoal(priority,goal);
    }
}
