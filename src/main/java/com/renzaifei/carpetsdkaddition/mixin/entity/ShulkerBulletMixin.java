package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ShulkerBullet.class)
public class ShulkerBulletMixin {

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Direction$Axis;)V",
            at = @At("RETURN")
    )
    private void onBulletInit(Level level, LivingEntity livingEntity, Entity entity, Direction.Axis axis, CallbackInfo ci){
        if (CarpetSDKAdditionSettings.reintroduceOlderShulkerBullet){
            ShulkerBullet bullet = (ShulkerBullet) (Object) this;
            BlockPos blockPos = livingEntity.blockPosition();
            double x = (double) blockPos.getX() + 0.5D;
            double y = (double) blockPos.getY() + 0.5D;
            double z = (double) blockPos.getZ() + 0.5D;
            bullet.moveTo(x, y, z, bullet.getYRot(), bullet.getXRot());
        }
    }

    @Inject(method = "selectNextMoveDirection",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/core/Direction;getRandom(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/core/Direction;",
                    shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onselectNextMoveDirection(Direction.Axis axis, CallbackInfo ci, BlockPos blockPos, double d, double e, double f, double g, Direction direction, BlockPos blockPos2, List<Direction> list){
        if (CarpetSDKAdditionSettings.reintroduceOlderShulkerBullet){
            ShulkerBullet shulkerBullet = (ShulkerBullet)(Object)this;
            list.clear();
            if (axis != Direction.Axis.X) {
                if (blockPos2.getX() < blockPos.getX() && shulkerBullet.level().getBlockState(blockPos2.east()).isAir()) {
                    list.add(Direction.EAST);
                } else if (blockPos2.getX() > blockPos.getX() && shulkerBullet.level().getBlockState(blockPos2.west()).isAir()) {
                    list.add(Direction.WEST);
                }
            }

            if (axis != Direction.Axis.Y) {
                if (blockPos2.getY() < blockPos.getY() && shulkerBullet.level().getBlockState(blockPos2.above()).isAir()) {
                    list.add(Direction.UP);
                } else if (blockPos2.getY() > blockPos.getY() && shulkerBullet.level().getBlockState(blockPos2.below()).isAir()) {
                    list.add(Direction.DOWN);
                }
            }

            if (axis != Direction.Axis.Z) {
                if (blockPos2.getZ() < blockPos.getZ() && shulkerBullet.level().getBlockState(blockPos2.south()).isAir()) {
                    list.add(Direction.SOUTH);
                } else if (blockPos2.getZ() > blockPos.getZ() && shulkerBullet.level().getBlockState(blockPos2.north()).isAir()) {
                    list.add(Direction.NORTH);
                }
            }
        }
    }
}
