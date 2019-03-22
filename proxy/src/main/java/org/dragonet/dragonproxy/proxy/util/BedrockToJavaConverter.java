package org.dragonet.dragonproxy.proxy.util;

import com.flowpowered.math.vector.Vector3i;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.world.block.CommandBlockMode;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.*;

import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSteerVehiclePacket;
import com.nukkitx.protocol.bedrock.packet.*;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.dragonproxy.proxy.util.users.User;

//My Ide annoys me
@SuppressWarnings("unused")
public final class BedrockToJavaConverter {
    private BedrockToJavaConverter() {}

    public static boolean convert(BlockPickRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static ClientEditBookPacket convert(BookEditPacket packet, BedrockSession<User> session) {
        return new ClientEditBookPacket(
            new ItemStack(packet.getType().equals(BookEditPacket.Type.SIGN_BOOK) ? 387 : 386, 1),
            packet.getType().equals(BookEditPacket.Type.SIGN_BOOK));
    }

    public static ClientUpdateCommandBlockPacket convert(CommandBlockUpdatePacket packet, BedrockSession<User> session) {
        Vector3i pos = packet.getBlockPosition();
        return new ClientUpdateCommandBlockPacket(
            new Position(pos.getX(), pos.getY(), pos.getZ()),
            packet.getCommand(),
            CommandBlockMode.values()[packet.getCommandBlockMode()],
            packet.isOutputTracked(),
            packet.isConditional(),
            !packet.isRedstoneMode());
    }

    public static ClientChatPacket convert(CommandRequestPacket packet, BedrockSession<User> session) {
        return new ClientChatPacket("/" + packet.getCommand());
    }

    public static ClientCloseWindowPacket convert(ContainerClosePacket packet, BedrockSession<User> session) {
        return new ClientCloseWindowPacket(packet.getWindowId());
    }

    public static ClientPlayerInteractEntityPacket convert(EntityPickRequestPacket packet, BedrockSession<User> session) {
        return new ClientPlayerInteractEntityPacket((int)packet.getRuntimeEntityId(), InteractAction.INTERACT_AT);
    }

    public static boolean convert(EventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static ClientPlayerInteractEntityPacket convert(InteractPacket packet, BedrockSession<User> session) {
        return new ClientPlayerInteractEntityPacket((int)packet.getRuntimeEntityId(),
            InteractAction.values()[packet.getAction()],
            packet.getMousePosition().getX(),
            packet.getMousePosition().getY(),
            packet.getMousePosition().getZ(),
            Hand.MAIN_HAND);
    }

    public static boolean convert(InventoryTransactionPacket packet, BedrockSession<User> session) {
    }

    public static boolean convert(LevelSoundEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(LevelSoundEvent3Packet packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(MapInfoRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(MobArmorEquipmentPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(MobEquipmentPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ModalFormResponsePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(MoveEntityAbsolutePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(NetworkStackLatencyPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(PhotoTransferPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(PlayerActionPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(PlayerHotbarPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(PlayerInputPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(PurchaseReceiptPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(RequestChunkRadiusPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static ClientSteerVehiclePacket convert(RiderJumpPacket packet, BedrockSession<User> session) {
        return new ClientSteerVehiclePacket(0, 0, true, false);
    }

    public static boolean convert(ServerSettingsRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetLocalPlayerAsInitializedPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SubClientLoginPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(TextPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(AddBehaviorTreePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(BlockEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(BossEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(CameraPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ChunkRadiusUpdatedPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ClientboundMapItemDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(CommandOutputPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ContainerSetDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(CraftingDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(DisconnectPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ExplodePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(FullChunkDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(GameRulesChangedPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(GuiDataPickItemPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(HurtArmorPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(WSConnectPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(LevelEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(MobEffectPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ModalFormRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(MoveEntityDeltaPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(NpcRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(PlayerListPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(PlaySoundPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(PlayStatusPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(RemoveEntityPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(RemoveObjectivePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(RespawnPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ScriptCustomEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ServerSettingsResponsePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ServerToClientHandshakePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetDifficultyPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetDisplayObjectivePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetEntityDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetEntityLinkPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetEntityMotionPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetHealthPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetLastHurtByPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetScoreboardIdentityPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetScorePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SetTitlePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ShowCreditsPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ShowProfilePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(ShowStoreOfferPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SimpleEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(StopSoundPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(StructureBlockUpdatePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(TakeItemEntityPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(TransferPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(UpdateBlockPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(UpdateBlockSyncedPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(UpdateEquipPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(UpdateSoftEnumPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(UpdateTradePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(AvailableEntityIdentifiersPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(BiomeDefinitionListPacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(LevelSoundEvent2Packet packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(NetworkChunkPublisherUpdatePacket packet, BedrockSession<User> session) {
        return false;
    }

    public static boolean convert(SpawnParticleEffectPacket packet, BedrockSession<User> session) {
        return false;
    }
}
