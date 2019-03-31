package org.dragonet.dragonproxy.proxy.util.translators;

import com.github.steveice10.packetlib.packet.Packet;
import com.nukkitx.protocol.MinecraftPacket;
import com.nukkitx.protocol.bedrock.packet.*;

//My Ide annoys me
@SuppressWarnings("unused")
public final class BedrockToJavaLoginConverter extends BedrockToJavaTranslator {
    public static final BedrockToJavaLoginConverter INSTANCE = new BedrockToJavaLoginConverter();

    private BedrockToJavaLoginConverter() {
        if(INSTANCE != null) {
            throw new RuntimeException();
        }
    }

    public boolean convert(AdventureSettingsPacket packet) {
        return false;
    }

    public boolean convert(AnimatePacket packet) {
        return false;
    }

    public boolean convert(BlockEntityDataPacket packet) {
        return false;
    }

    public boolean convert(BlockPickRequestPacket packet) {
        return false;
    }

    public boolean convert(BookEditPacket packet) {
        return false;
    }

    public boolean convert(ClientToServerHandshakePacket packet) {
        return false;
    }

    public boolean convert(CommandBlockUpdatePacket packet) {
        return false;
    }

    public boolean convert(CommandRequestPacket packet) {
        return false;
    }

    public boolean convert(ContainerClosePacket packet) {
        return false;
    }

    public boolean convert(CraftingEventPacket packet) {
        return false;
    }

    public boolean convert(EntityEventPacket packet) {
        return false;
    }

    public boolean convert(EntityFallPacket packet) {
        return false;
    }

    public boolean convert(EntityPickRequestPacket packet) {
        return false;
    }

    public boolean convert(EventPacket packet) {
        return false;
    }

    public boolean convert(InteractPacket packet) {
        return false;
    }

    public boolean convert(InventoryContentPacket packet) {
        return false;
    }

    public boolean convert(InventorySlotPacket packet) {
        return false;
    }

    public boolean convert(InventoryTransactionPacket packet) {
        return false;
    }

    public boolean convert(ItemFrameDropItemPacket packet) {
        return false;
    }

    public boolean convert(LabTablePacket packet) {
        return false;
    }

    public boolean convert(LecternUpdatePacket packet) {
        return false;
    }

    public boolean convert(LevelSoundEventPacket packet) {
        return false;
    }

    public boolean convert(LevelSoundEvent3Packet packet) {
        return false;
    }

    public boolean convert(LoginPacket packet) {
        return false;
    }

    public boolean convert(MapInfoRequestPacket packet) {
        return false;
    }

    public boolean convert(MobArmorEquipmentPacket packet) {
        return false;
    }

    public boolean convert(MobEquipmentPacket packet) {
        return false;
    }

    public boolean convert(ModalFormResponsePacket packet) {
        return false;
    }

    public boolean convert(MoveEntityAbsolutePacket packet) {
        return false;
    }

    public boolean convert(MovePlayerPacket packet) {
        return false;
    }

    public boolean convert(NetworkStackLatencyPacket packet) {
        return false;
    }

    public boolean convert(PhotoTransferPacket packet) {
        return false;
    }

    public boolean convert(PlayerActionPacket packet) {
        return false;
    }

    public boolean convert(PlayerHotbarPacket packet) {
        return false;
    }

    public boolean convert(PlayerInputPacket packet) {
        return false;
    }

    public boolean convert(PlayerSkinPacket packet) {
        return false;
    }

    public boolean convert(PurchaseReceiptPacket packet) {
        return false;
    }

    public boolean convert(RequestChunkRadiusPacket packet) {
        return false;
    }

    public boolean convert(ResourcePackChunkRequestPacket packet) {
        return false;
    }

    public boolean convert(ResourcePackClientResponsePacket packet) {
        return false;
    }

    public boolean convert(RiderJumpPacket packet) {
        return false;
    }

    public boolean convert(ServerSettingsRequestPacket packet) {
        return false;
    }

    public boolean convert(SetDefaultGameTypePacket packet) {
        return false;
    }

    public boolean convert(SetLocalPlayerAsInitializedPacket packet) {
        return false;
    }

    public boolean convert(SetPlayerGameTypePacket packet) {
        return false;
    }

    public boolean convert(SubClientLoginPacket packet) {
        return false;
    }

    public boolean convert(TextPacket packet) {
        return false;
    }

    public boolean convert(AddBehaviorTreePacket packet) {
        return false;
    }

    public boolean convert(AddEntityPacket packet) {
        return false;
    }

    public boolean convert(AddHangingEntityPacket packet) {
        return false;
    }

    public boolean convert(AddItemEntityPacket packet) {
        return false;
    }

    public boolean convert(AddPaintingPacket packet) {
        return false;
    }

    public boolean convert(AddPlayerPacket packet) {
        return false;
    }

    public boolean convert(AvailableCommandsPacket packet) {
        return false;
    }

    public boolean convert(BlockEventPacket packet) {
        return false;
    }

    public boolean convert(BossEventPacket packet) {
        return false;
    }

    public boolean convert(CameraPacket packet) {
        return false;
    }

    public boolean convert(ChangeDimensionPacket packet) {
        return false;
    }

    public boolean convert(ChunkRadiusUpdatedPacket packet) {
        return false;
    }

    public boolean convert(ClientboundMapItemDataPacket packet) {
        return false;
    }

    public boolean convert(CommandOutputPacket packet) {
        return false;
    }

    public boolean convert(ContainerOpenPacket packet) {
        return false;
    }

    public boolean convert(ContainerSetDataPacket packet) {
        return false;
    }

    public boolean convert(CraftingDataPacket packet) {
        return false;
    }

    public boolean convert(DisconnectPacket packet) {
        return false;
    }

    public boolean convert(ExplodePacket packet) {
        return false;
    }

    public boolean convert(FullChunkDataPacket packet) {
        return false;
    }

    public boolean convert(GameRulesChangedPacket packet) {
        return false;
    }

    public boolean convert(GuiDataPickItemPacket packet) {
        return false;
    }

    public boolean convert(HurtArmorPacket packet) {
        return false;
    }

    public boolean convert(WSConnectPacket packet) {
        return false;
    }

    public boolean convert(LevelEventPacket packet) {
        return false;
    }

    public boolean convert(MobEffectPacket packet) {
        return false;
    }

    public boolean convert(ModalFormRequestPacket packet) {
        return false;
    }

    public boolean convert(MoveEntityDeltaPacket packet) {
        return false;
    }

    public boolean convert(NpcRequestPacket packet) {
        return false;
    }

    public boolean convert(PlayerListPacket packet) {
        return false;
    }

    public boolean convert(PlaySoundPacket packet) {
        return false;
    }

    public boolean convert(PlayStatusPacket packet) {
        return false;
    }

    public boolean convert(RemoveEntityPacket packet) {
        return false;
    }

    public boolean convert(RemoveObjectivePacket packet) {
        return false;
    }

    public boolean convert(ResourcePackChunkDataPacket packet) {
        return false;
    }

    public boolean convert(ResourcePackDataInfoPacket packet) {
        return false;
    }

    public boolean convert(ResourcePacksInfoPacket packet) {
        return false;
    }

    public boolean convert(ResourcePackStackPacket packet) {
        return false;
    }

    public boolean convert(RespawnPacket packet) {
        return false;
    }

    public boolean convert(ScriptCustomEventPacket packet) {
        return false;
    }

    public boolean convert(ServerSettingsResponsePacket packet) {
        return false;
    }

    public boolean convert(ServerToClientHandshakePacket packet) {
        return false;
    }

    public boolean convert(SetCommandsEnabledPacket packet) {
        return false;
    }

    public boolean convert(SetDifficultyPacket packet) {
        return false;
    }

    public boolean convert(SetDisplayObjectivePacket packet) {
        return false;
    }

    public boolean convert(SetEntityDataPacket packet) {
        return false;
    }

    public boolean convert(SetEntityLinkPacket packet) {
        return false;
    }

    public boolean convert(SetEntityMotionPacket packet) {
        return false;
    }

    public boolean convert(SetHealthPacket packet) {
        return false;
    }

    public boolean convert(SetLastHurtByPacket packet) {
        return false;
    }

    public boolean convert(SetScoreboardIdentityPacket packet) {
        return false;
    }

    public boolean convert(SetScorePacket packet) {
        return false;
    }

    public boolean convert(SetSpawnPositionPacket packet) {
        return false;
    }

    public boolean convert(SetTimePacket packet) {
        return false;
    }

    public boolean convert(SetTitlePacket packet) {
        return false;
    }

    public boolean convert(ShowCreditsPacket packet) {
        return false;
    }

    public boolean convert(ShowProfilePacket packet) {
        return false;
    }

    public boolean convert(ShowStoreOfferPacket packet) {
        return false;
    }

    public boolean convert(SimpleEventPacket packet) {
        return false;
    }

    public boolean convert(SpawnExperienceOrbPacket packet) {
        return false;
    }

    public boolean convert(StartGamePacket packet) {
        return false;
    }

    public boolean convert(StopSoundPacket packet) {
        return false;
    }

    public boolean convert(StructureBlockUpdatePacket packet) {
        return false;
    }

    public boolean convert(TakeItemEntityPacket packet) {
        return false;
    }

    public boolean convert(TransferPacket packet) {
        return false;
    }

    public boolean convert(UpdateAttributesPacket packet) {
        return false;
    }

    public boolean convert(UpdateBlockPacket packet) {
        return false;
    }

    public boolean convert(UpdateBlockSyncedPacket packet) {
        return false;
    }

    public boolean convert(UpdateEquipPacket packet) {
        return false;
    }

    public boolean convert(UpdateSoftEnumPacket packet) {
        return false;
    }

    public boolean convert(UpdateTradePacket packet) {
        return false;
    }

    public boolean convert(AvailableEntityIdentifiersPacket packet) {
        return false;
    }

    public boolean convert(BiomeDefinitionListPacket packet) {
        return false;
    }

    public boolean convert(LevelSoundEvent2Packet packet) {
        return false;
    }

    public boolean convert(NetworkChunkPublisherUpdatePacket packet) {
        return false;
    }

    public boolean convert(SpawnParticleEffectPacket packet) {
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
