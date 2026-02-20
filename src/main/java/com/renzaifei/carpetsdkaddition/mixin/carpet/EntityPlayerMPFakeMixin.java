package com.renzaifei.carpetsdkaddition.mixin.carpet;

import carpet.patches.EntityPlayerMPFake;
import carpet.patches.FakeClientConnection;
import com.mojang.authlib.GameProfile;
import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Set;


//#if MC < 12109
import static net.minecraft.world.entity.player.Player.DATA_PLAYER_MODE_CUSTOMISATION;
//#else
//$$ import static net.minecraft.world.entity.Avatar.DATA_PLAYER_MODE_CUSTOMISATION;
//#endif

//#if MC > 12101
//$$ import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
//#endif


@Mixin(EntityPlayerMPFake.class)
public class EntityPlayerMPFakeMixin {

    @Shadow
    public static EntityPlayerMPFake respawnFake(MinecraftServer server, ServerLevel level, GameProfile profile, ClientInformation cli){return null;}

    @Inject(method = "createFake",
            at = @At("HEAD"),
            cancellable = true)
    private static void onCreateFake(String username, MinecraftServer server, Vec3 pos, double yaw, double pitch, 
                                     ResourceKey<Level> dimensionId, GameType gamemode, boolean flying, 
                                     CallbackInfoReturnable<Boolean> cir) {

        if (!CarpetSDKAdditionSettings.forcefullyGenerateOfflineFakePlayer) {
            return;
        }

        ServerLevel worldIn = server.getLevel(dimensionId);
        if (worldIn == null) {
            cir.setReturnValue(false);
            return;
        }

        GameProfile gameprofile = new GameProfile(UUIDUtil.createOfflinePlayerUUID(username), username);

        server.execute(() -> {
            EntityPlayerMPFake instance = respawnFake(server, worldIn, gameprofile, ClientInformation.createDefault());
            
            instance.fixStartingPosition = () -> instance.moveTo(pos.x, pos.y, pos.z, (float) yaw, (float) pitch);
            
            server.getPlayerList().placeNewPlayer(
                    new FakeClientConnection(PacketFlow.SERVERBOUND),
                    instance,
                    new CommonListenerCookie(gameprofile, 0, instance.clientInformation(), false)
            );
            //#if MC < 12102
            instance.teleportTo(worldIn, pos.x, pos.y, pos.z, (float) yaw, (float) pitch);
            //#else
            //$$ instance.teleportTo(worldIn,pos.x, pos.y, pos.z,Set.of(),(float) yaw, (float) pitch,true);
            //#endif
            instance.setHealth(20.0F);
            instance.unsetRemoved();
            instance.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(0.6F);
            instance.gameMode.changeGameModeForPlayer(gamemode);
            //#if MC < 12102
            server.getPlayerList().broadcastAll(
                    new ClientboundRotateHeadPacket(instance, (byte) (instance.yHeadRot * 256 / 360)),
                    dimensionId
            );
            server.getPlayerList().broadcastAll(
                    new ClientboundTeleportEntityPacket(instance),
                    dimensionId
            );
            //#else
            //$$ server.getPlayerList().broadcastAll(new ClientboundRotateHeadPacket(instance, (byte) (instance.yHeadRot * 256 / 360)), dimensionId);
            //$$ server.getPlayerList().broadcastAll(ClientboundEntityPositionSyncPacket.of(instance), dimensionId);
            //#endif
            instance.getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0x7f);
            instance.getAbilities().flying = flying;
        });

        cir.setReturnValue(true);
    }
}