package com.renzaifei.carpetsdkaddition.mixin.client;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    //实体高亮
    @Inject(method = "isCurrentlyGlowing",at = @At("HEAD"),cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetSDKAdditionSettings.hightLightItem) {
            if ((Object) this instanceof ItemEntity) {
                cir.setReturnValue(true);
                return;
            }
        }
        if (CarpetSDKAdditionSettings.hightLightLivingEntity){
            if ((Object) this instanceof LivingEntity) {
                cir.setReturnValue(true);
            }
        }
    }
    //设置颜色
    @Inject(method = "getTeamColor",at = @At("HEAD"),cancellable = true)
    private void onGetTeamColor(CallbackInfoReturnable<Integer> cir) {
        if (CarpetSDKAdditionSettings.hightLightLivingEntity){
            if ((Object) this instanceof LivingEntity) {
                cir.setReturnValue(0xFFFF00FF);
            }
        }
    }
}
