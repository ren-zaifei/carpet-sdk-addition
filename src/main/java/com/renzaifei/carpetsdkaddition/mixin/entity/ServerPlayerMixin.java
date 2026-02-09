package com.renzaifei.carpetsdkaddition.mixin.entity;

import com.mojang.authlib.GameProfile;
import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.access.ArmorStandAccess;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Shadow
    public abstract boolean startRiding(Entity entity, boolean bl);

    @Shadow
    public ServerGamePacketListenerImpl connection;

    @Override
    @Intrinsic
    public void setShiftKeyDown(boolean bl) {
        super.setShiftKeyDown(bl);
    }

    @Unique
    private int sneakTimes = 0;
    @Unique
    private long lastSneakTime = 0;

    @SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference", "target"})
    @Inject(
            method = "setShiftKeyDown(Z)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void customSetShiftKeyDownCheck(boolean sneaking, CallbackInfo ci) {
        if (!CarpetSDKAdditionSettings.playerCanSit || (sneaking && this.isShiftKeyDown())) {
            return;
        }

        if (sneaking) {
            long nowTime = Util.getMillis();
            if (nowTime - this.lastSneakTime > 200) {
                this.sneakTimes = 0;
            } else {
                ci.cancel();
            }

            if (this.onGround()) {
                this.sneakTimes++;
            }

            this.lastSneakTime = nowTime;

            if (this.sneakTimes > 2) {
                ArmorStand armorStandEntity = new ArmorStand(this.level(), this.getX(), this.getY(), this.getZ());
                ((ArmorStandAccess) armorStandEntity).sit(true);
                this.level().addFreshEntity(armorStandEntity);
                this.setShiftKeyDown(false);

                if (this.connection != null) {
                    this.connection.send(new ClientboundSetEntityDataPacket(
                            this.getId(), this.getEntityData().getNonDefaultValues()
                    ));
                }

                this.startRiding(armorStandEntity);
                this.sneakTimes = 0;
                ci.cancel();
            }
        }
    }
}
