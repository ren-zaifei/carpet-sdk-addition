package com.renzaifei.carpetsdkaddition.mixin;

import carpet.CarpetSettings;
import carpet.helpers.BlockRotator;
import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRotator.class)
public class flippinTotemMixin {
    //借鉴了原pca的源码，并加以改进

    @Unique
    private static long fliCoolDown = 0;

    @Unique
    private static boolean SDK$playerHoldsTotemOfUndyingMainHand(@NotNull PlayerEntity player) {
        return player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Inject(
            method = "flipBlockWithCactus",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true,
            remap = false
    )
    private static void postFlipBlockWithCactus(BlockState state, World level, PlayerEntity player, Hand hand, BlockHitResult hit, @NotNull CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && CarpetSDKAdditionSettings.flippinTotem &&
                fliCoolDown < level.getTime()) {
            //这里需要加冷却，否则侦测器一类的会直接翻转两次，看着就像是没翻转
            //当现在的时间大于之前加冷却的时间时，才能再次触发
            if (!player.getAbilities().allowModifyWorld ||
                    !SDK$playerHoldsTotemOfUndyingMainHand(player) ||
                    !player.getOffHandStack().isEmpty()) {
                return;
            }
            //这里是实际加上冷却的地方
            fliCoolDown = level.getTime() + 2L;
            CarpetSettings.impendingFillSkipUpdates.set(true);
            boolean ret = BlockRotator.flipBlock(state, level, player, hand, hit);
            CarpetSettings.impendingFillSkipUpdates.set(false);

            cir.setReturnValue(ret);
        }
    }

    @Inject(
            method = "flippinEligibility",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true,
            remap = false
    )
    private static void postFlippinEligibility(Entity entity, @NotNull CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && CarpetSDKAdditionSettings.flippinTotem && (entity instanceof PlayerEntity)) {
            PlayerEntity player = (PlayerEntity) entity;
            boolean ret = !player.getOffHandStack().isEmpty() && SDK$playerHoldsTotemOfUndyingMainHand(player);
            cir.setReturnValue(ret);
        }
    }
}
