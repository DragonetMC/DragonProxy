package org.dragonet.dragonproxy.proxy.util.translators;

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
import com.github.steveice10.packetlib.packet.Packet;
import com.nukkitx.protocol.MinecraftPacket;
import com.nukkitx.protocol.bedrock.packet.*;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.dragonproxy.proxy.util.users.User;

//My Ide annoys me
@SuppressWarnings("unused")
public final class BedrockToJavaWindowConverter extends BedrockToJavaTranslator {
    public static final BedrockToJavaWindowConverter INSTANCE = new BedrockToJavaWindowConverter();

    private BedrockToJavaWindowConverter() {
        if(INSTANCE != null) {
            throw new RuntimeException();
        }
    }

    public boolean convert(BlockPickRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public ClientEditBookPacket convert(BookEditPacket packet, BedrockSession<User> session) {
        return new ClientEditBookPacket(
            new ItemStack(packet.getType().equals(BookEditPacket.Type.SIGN_BOOK) ? 387 : 386, 1),
            packet.getType().equals(BookEditPacket.Type.SIGN_BOOK));
    }

    public ClientUpdateCommandBlockPacket convert(CommandBlockUpdatePacket packet, BedrockSession<User> session) {
        Vector3i pos = packet.getBlockPosition();
        return new ClientUpdateCommandBlockPacket(
            new Position(pos.getX(), pos.getY(), pos.getZ()),
            packet.getCommand(),
            CommandBlockMode.values()[packet.getCommandBlockMode()],
            packet.isOutputTracked(),
            packet.isConditional(),
            !packet.isRedstoneMode());
    }

    public ClientChatPacket convert(CommandRequestPacket packet, BedrockSession<User> session) {
        return new ClientChatPacket("/" + packet.getCommand());
    }

    public ClientCloseWindowPacket convert(ContainerClosePacket packet, BedrockSession<User> session) {
        return new ClientCloseWindowPacket(packet.getWindowId());
    }

    public ClientPlayerInteractEntityPacket convert(EntityPickRequestPacket packet, BedrockSession<User> session) {
        return new ClientPlayerInteractEntityPacket((int)packet.getRuntimeEntityId(), InteractAction.INTERACT_AT);
    }

    public boolean convert(EventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public ClientPlayerInteractEntityPacket convert(InteractPacket packet, BedrockSession<User> session) {
        return new ClientPlayerInteractEntityPacket((int)packet.getRuntimeEntityId(),
            InteractAction.values()[packet.getAction()],
            packet.getMousePosition().getX(),
            packet.getMousePosition().getY(),
            packet.getMousePosition().getZ(),
            Hand.MAIN_HAND);
    }

    public boolean convert(InventoryTransactionPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(LevelSoundEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(LevelSoundEvent3Packet packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(MapInfoRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(MobArmorEquipmentPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(MobEquipmentPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ModalFormResponsePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(MoveEntityAbsolutePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(NetworkStackLatencyPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(PhotoTransferPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(PlayerActionPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(PlayerHotbarPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(PlayerInputPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(PurchaseReceiptPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(RequestChunkRadiusPacket packet, BedrockSession<User> session) {
        return false;
    }

    public ClientSteerVehiclePacket convert(RiderJumpPacket packet, BedrockSession<User> session) {
        return new ClientSteerVehiclePacket(0, 0, true, false);
    }

    public boolean convert(ServerSettingsRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetLocalPlayerAsInitializedPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SubClientLoginPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(TextPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(AddBehaviorTreePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(BlockEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(BossEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(CameraPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ChunkRadiusUpdatedPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ClientboundMapItemDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(CommandOutputPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ContainerSetDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(CraftingDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(DisconnectPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ExplodePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(FullChunkDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(GameRulesChangedPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(GuiDataPickItemPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(HurtArmorPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(LevelEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(MobEffectPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ModalFormRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(MoveEntityDeltaPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(NpcRequestPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(PlayerListPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(PlaySoundPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(PlayStatusPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(RemoveEntityPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(RemoveObjectivePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(RespawnPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ScriptCustomEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ServerSettingsResponsePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ServerToClientHandshakePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetDifficultyPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetDisplayObjectivePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetEntityDataPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetEntityLinkPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetEntityMotionPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetHealthPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetLastHurtByPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetScoreboardIdentityPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetScorePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SetTitlePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ShowCreditsPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ShowProfilePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(ShowStoreOfferPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SimpleEventPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(StopSoundPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(StructureBlockUpdatePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(TakeItemEntityPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(TransferPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(UpdateBlockPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(UpdateBlockSyncedPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(UpdateEquipPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(UpdateSoftEnumPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(UpdateTradePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(AvailableEntityIdentifiersPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(BiomeDefinitionListPacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(LevelSoundEvent2Packet packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(NetworkChunkPublisherUpdatePacket packet, BedrockSession<User> session) {
        return false;
    }

    public boolean convert(SpawnParticleEffectPacket packet, BedrockSession<User> session) {
        return false;
    }

    /**
     * @param p the packet to convert
     * @return the java equivalent of p
     */
    //Not yet supported
    @Override
    public Packet convert(MinecraftPacket p) {
        return null;
    }
}
