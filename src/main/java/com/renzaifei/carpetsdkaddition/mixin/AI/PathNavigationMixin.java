package com.renzaifei.carpetsdkaddition.mixin.AI;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.Util;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathNavigation.class)
public abstract class PathNavigationMixin {

    @Shadow protected abstract void stop();


    @Shadow protected Mob mob;

    @Shadow protected Path path;

    @Shadow protected int tick;

    @Shadow protected int lastStuckCheck;

    @Shadow protected Vec3 lastStuckCheckPos;

    @Shadow protected Vec3i timeoutCachedNode;

    @Shadow protected long timeoutTimer;

    @Shadow protected double timeoutLimit;

    @Shadow protected long lastTimeoutCheck;


    @Shadow
    private boolean isStuck;

    @Shadow
    protected abstract void timeoutPath();

    //重新引入旧版寻路机制
    @Inject(method = "doStuckDetection",at = @At("HEAD"),cancellable = true)
    private void onDoStuckDetection(Vec3 vec3, CallbackInfo ci) {
        if (!CarpetSDKAdditionSettings.reintroduceOlderPathfinding) return;
        if (this.tick - this.lastStuckCheck > 100) {
            float f = this.mob.getSpeed() >= 1.0F ? this.mob.getSpeed() : this.mob.getSpeed() * this.mob.getSpeed();
            float g = f * 100.0F * 0.25F;
            if (vec3.distanceToSqr(this.lastStuckCheckPos) < (double)(g * g)) {
                this.isStuck = true;
                this.stop();
            }else {
                this.isStuck = false;
            }

            this.lastStuckCheck = this.tick;
            this.lastStuckCheckPos = vec3;
        }

        if (this.path != null && !this.path.isDone()) {
            Vec3i currentNodePos = this.path.getNextNodePos();
            long currentMs = Util.getMillis();

            if (currentNodePos.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += currentMs - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = currentNodePos;
                double distanceToNode = vec3.distanceTo(Vec3.atBottomCenterOf(currentNodePos));
                this.timeoutLimit = this.mob.getSpeed() > 0.0F
                        ? distanceToNode / (double)this.mob.getSpeed() * 1000.0D
                        : 0.0D;
                this.timeoutTimer = 0L;
            }

            if (this.timeoutLimit > 0.0D && (double)this.timeoutTimer > this.timeoutLimit * 3.0D) {
                this.timeoutPath();
            }
            this.lastTimeoutCheck = currentMs;
        }
        ci.cancel();
    }
}
