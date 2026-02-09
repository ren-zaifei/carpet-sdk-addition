package com.renzaifei.carpetsdkaddition.mixin.entity;


import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.access.ArmorStandAccess;
import com.renzaifei.carpetsdkaddition.access.PiglinEntityAccess;
import net.minecraft.Util;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin{

    @Shadow
    public abstract EntityType<?> getType();

    @Unique
    private int sneakTimes = 0;
    @Unique
    private long lastSneakTime = 0;

    //猪灵AI优化
    @Inject(method = "move",at = @At(value = "HEAD"),cancellable = true)
    private void onMove(MoverType moverType, Vec3 vec3, CallbackInfo ci){
        if (CarpetSDKAdditionSettings.betterPiglinAI){
            if (this.getType() != EntityType.PIGLIN)return;
            PiglinEntityAccess access = (PiglinEntityAccess)this;
            if (access.isHasGoldenCarrot()){
                ci.cancel();
            }
        }
    }

    @Inject(method = "removePassenger",at = @At("HEAD"))
    private void onRemovePassenger(Entity passenger, CallbackInfo ci){
        if (!CarpetSDKAdditionSettings.playerCanSit) return;
        if ((Object)this instanceof ArmorStand armorStand){
            if (((ArmorStandAccess)armorStand).isSitting()) {
                armorStand.setPos(passenger.getX(), passenger.getY() + 1, passenger.getZ());
                //#if MC < 12102
                armorStand.kill();
                //#else
                //$$ armorStand.kill((ServerLevel)(armorStand.level()));
                //#endif
            }
        }
    }

    @Inject(method = "setShiftKeyDown",at = @At("HEAD"),cancellable = true)
    private void onSetShiftKeyDown(boolean sneaking, CallbackInfo ci){
        if (!CarpetSDKAdditionSettings.playerCanSit || (sneaking && ((Entity)(Object)this).isShiftKeyDown())) {
            return;
        }
        if ((Object)this instanceof ServerPlayer serverPlayer){
            if (sneaking) {
                long nowTime = Util.getMillis();
                if (nowTime - this.lastSneakTime > 200) {
                    this.sneakTimes = 0;
                } else {
                    ci.cancel();
                }

                if (serverPlayer.onGround()) {
                    this.sneakTimes++;
                }

                this.lastSneakTime = nowTime;

                if (this.sneakTimes > 2) {
                    ArmorStand armorStandEntity = new ArmorStand(serverPlayer.level(), serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());
                    ((ArmorStandAccess) armorStandEntity).sit(true);
                    serverPlayer.level().addFreshEntity(armorStandEntity);
                    serverPlayer.setShiftKeyDown(false);

                    if (serverPlayer.connection != null) {
                        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(
                                serverPlayer.getId(), serverPlayer.getEntityData().getNonDefaultValues()
                        ));
                    }
                    //#if MC < 12110
                    serverPlayer.startRiding(armorStandEntity);
                    //#else
                    //$$ serverPlayer.startRiding(armorStandEntity,true,false);
                    //#endif
                    this.sneakTimes = 0;
                    ci.cancel();
                }
            }
        }
    }
}
