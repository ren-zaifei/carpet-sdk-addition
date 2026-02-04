package com.renzaifei.carpetsdkaddition.mixin;

import carpet.CarpetSettings;
import carpet.helpers.BlockRotator;
import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRotator.class)
public class FlippinTotemMixin {
    //借鉴了原pca的源码，并加以改进

    @Unique
    private static long fliCoolDown = 0;

    @Unique
    private static boolean SDK$playerHoldsTotemOfUndyingMainHand(@NotNull Player player) {
        return player.getMainHandItem().getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Inject(
            method = "flipBlockWithCactus",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true,
            remap = false
    )
    private static void postFlipBlockWithCactus(BlockState state, Level world, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && CarpetSDKAdditionSettings.flippinTotem &&
                fliCoolDown < world.getGameTime()) {
            //这里需要加冷却，否则侦测器一类的会直接翻转两次，看着就像是没翻转
            //当现在的时间大于之前加冷却的时间时，才能再次触发
            if (!player.getAbilities().mayBuild ||
                    !SDK$playerHoldsTotemOfUndyingMainHand(player)) {
                return;
            }
            //这里是实际加上冷却的地方
            fliCoolDown = world.getGameTime() + 2L;
            CarpetSettings.impendingFillSkipUpdates.set(true);
            boolean ret = BlockRotator.flipBlock(state, world, player, hand, hit);
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
    private static void postFlippinEligibility(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && CarpetSDKAdditionSettings.flippinTotem && (entity instanceof Player)) {
            Player player = (Player) entity;
            cir.setReturnValue(SDK$playerHoldsTotemOfUndyingMainHand(player));
        }
    }
}
