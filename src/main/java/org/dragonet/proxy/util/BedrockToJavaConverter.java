/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.util;

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
import org.dragonet.proxy.network.session.ProxySession;

//My Ide annoys me
@SuppressWarnings("unused")
public final class BedrockToJavaConverter {
    private BedrockToJavaConverter() {}

    public static boolean convert(BlockPickRequestPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static ClientEditBookPacket convert(BookEditPacket packet, BedrockSession<ProxySession> session) {
        return new ClientEditBookPacket(
            new ItemStack(packet.getType().equals(BookEditPacket.Type.SIGN_BOOK) ? 387 : 386, 1),
            packet.getType().equals(BookEditPacket.Type.SIGN_BOOK));
    }

    public static ClientUpdateCommandBlockPacket convert(CommandBlockUpdatePacket packet, BedrockSession<ProxySession> session) {
        Vector3i pos = packet.getBlockPosition();
        return new ClientUpdateCommandBlockPacket(
            new Position(pos.getX(), pos.getY(), pos.getZ()),
            packet.getCommand(),
            CommandBlockMode.values()[packet.getCommandBlockMode()],
            packet.isOutputTracked(),
            packet.isConditional(),
            !packet.isRedstoneMode());
    }

    public static ClientChatPacket convert(CommandRequestPacket packet, BedrockSession<ProxySession> session) {
        return new ClientChatPacket("/" + packet.getCommand());
    }

    public static ClientCloseWindowPacket convert(ContainerClosePacket packet, BedrockSession<ProxySession> session) {
        return new ClientCloseWindowPacket(packet.getWindowId());
    }

    public static ClientPlayerInteractEntityPacket convert(EntityPickRequestPacket packet, BedrockSession<ProxySession> session) {
        return new ClientPlayerInteractEntityPacket((int)packet.getRuntimeEntityId(), InteractAction.INTERACT_AT);
    }

    public static boolean convert(EventPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static ClientPlayerInteractEntityPacket convert(InteractPacket packet, BedrockSession<ProxySession> session) {
        return new ClientPlayerInteractEntityPacket((int)packet.getRuntimeEntityId(),
            InteractAction.values()[packet.getAction()],
            packet.getMousePosition().getX(),
            packet.getMousePosition().getY(),
            packet.getMousePosition().getZ(),
            Hand.MAIN_HAND);
    }

    public static boolean convert(InventoryTransactionPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(LevelSoundEventPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(LevelSoundEvent3Packet packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(MapInfoRequestPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(MobArmorEquipmentPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(MobEquipmentPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ModalFormResponsePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(MoveEntityAbsolutePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(NetworkStackLatencyPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(PhotoTransferPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(PlayerActionPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(PlayerHotbarPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(PlayerInputPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(PurchaseReceiptPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(RequestChunkRadiusPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static ClientSteerVehiclePacket convert(RiderJumpPacket packet, BedrockSession<ProxySession> session) {
        return new ClientSteerVehiclePacket(0, 0, true, false);
    }

    public static boolean convert(ServerSettingsRequestPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetLocalPlayerAsInitializedPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SubClientLoginPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(TextPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(AddBehaviorTreePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(BlockEventPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(BossEventPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(CameraPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ChunkRadiusUpdatedPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ClientboundMapItemDataPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(CommandOutputPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ContainerSetDataPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(CraftingDataPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(DisconnectPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ExplodePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(FullChunkDataPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(GameRulesChangedPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(GuiDataPickItemPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(HurtArmorPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(LevelEventPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(MobEffectPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ModalFormRequestPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(MoveEntityDeltaPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(NpcRequestPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(PlayerListPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(PlaySoundPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(PlayStatusPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(RemoveEntityPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(RemoveObjectivePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(RespawnPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ScriptCustomEventPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ServerSettingsResponsePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ServerToClientHandshakePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetDifficultyPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetDisplayObjectivePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetEntityDataPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetEntityLinkPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetEntityMotionPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetHealthPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetLastHurtByPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetScoreboardIdentityPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetScorePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SetTitlePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ShowCreditsPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ShowProfilePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(ShowStoreOfferPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SimpleEventPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(StopSoundPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(StructureBlockUpdatePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(TakeItemEntityPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(TransferPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(UpdateBlockPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(UpdateBlockSyncedPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(UpdateEquipPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(UpdateSoftEnumPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(UpdateTradePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(AvailableEntityIdentifiersPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(BiomeDefinitionListPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(LevelSoundEvent2Packet packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(NetworkChunkPublisherUpdatePacket packet, BedrockSession<ProxySession> session) {
        return false;
    }

    public static boolean convert(SpawnParticleEffectPacket packet, BedrockSession<ProxySession> session) {
        return false;
    }
}
