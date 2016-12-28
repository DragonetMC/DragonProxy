/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import lombok.Getter;

import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.protocol.Protocol;
import org.dragonet.proxy.utilities.PatternChecker;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.Server;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddHangingEntityPacket;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.AddItemPacket;
import cn.nukkit.network.protocol.AddPaintingPacket;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.AdventureSettingsPacket;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.AvailableCommandsPacket;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.BlockEventPacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.ChangeDimensionPacket;
import cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket;
import cn.nukkit.network.protocol.CommandStepPacket;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.nukkit.network.protocol.ContainerSetSlotPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.CraftingEventPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.DisconnectPacket;
import cn.nukkit.network.protocol.DropItemPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.ExplodePacket;
import cn.nukkit.network.protocol.FullChunkDataPacket;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.network.protocol.HurtArmorPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.InventoryActionPacket;
import cn.nukkit.network.protocol.ItemFrameDropItemPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerInputPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.RemoveBlockPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.ReplaceSelectedItemPacket;
import cn.nukkit.network.protocol.RequestChunkRadiusPacket;
import cn.nukkit.network.protocol.ResourcePackClientResponsePacket;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.SetCommandsEnabledPacket;
import cn.nukkit.network.protocol.SetDifficultyPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import cn.nukkit.network.protocol.SetHealthPacket;
import cn.nukkit.network.protocol.SetPlayerGameTypePacket;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.network.protocol.SetTimePacket;
import cn.nukkit.network.protocol.SpawnExperienceOrbPacket;
import cn.nukkit.network.protocol.StartGamePacket;
import cn.nukkit.network.protocol.TakeItemEntityPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.network.protocol.UseItemPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Zlib;

public class PEPacketProcessor implements Runnable {

    public final static int MAX_PACKETS_PER_CYCLE = 200;

    @Getter
    private final UpstreamSession client;

    private final Deque<byte[]> packets = new ArrayDeque<>();

    public PEPacketProcessor(UpstreamSession client) {
        this.client = client;
    }

    public void putPacket(byte[] packet) {
        packets.add(packet);
    }

    @Override
    public void run() {
        int cnt = 0;
        while (cnt < MAX_PACKETS_PER_CYCLE && !packets.isEmpty()) {
            cnt++;
            byte[] bin = packets.pop();
            DataPacket[] packets = Protocol.decode(bin);
            if (packets == null || packets.length < 1) {
                continue;
            }
            
            for(DataPacket packet : packets){
            	handlePacket(packet);
            }
        }
    }

    public void handlePacket(DataPacket packet) {
        if (packet != null) {
            if(packet.pid() == ProtocolInfo.LOGIN_PACKET){
                try {
                	client.onLogin((LoginPacket) packet);
                } catch (Exception e){
                	e.printStackTrace();
                }
                return;
            }
        
	        if(packet.pid() == ProtocolInfo.TEXT_PACKET && client.getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null){
	        	TextPacket pack = (TextPacket) packet;
	            if (client.getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("email")) {
	                if (!PatternChecker.matchEmail(pack.message.trim())) {
	                	
	                    client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
	                    client.disconnect(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
	                    return;
	                }
	                client.getDataCache().put(CacheKey.AUTHENTICATION_EMAIL, pack.message.trim());
	                client.getDataCache().put(CacheKey.AUTHENTICATION_STATE, "password");
	                client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_PASSWORD));
	            } else if (client.getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("password")) {
	                if (client.getDataCache().get(CacheKey.AUTHENTICATION_EMAIL) == null || pack.message.equals(" ")) {
	                    client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
	                    client.disconnect(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
	                    return;
	                }
	                client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_LOGGIN_IN));
	                client.getDataCache().remove(CacheKey.AUTHENTICATION_STATE);
	                client.authenticateOnlineMode(pack.message); //We NEVER cache password for better security. 
	            }
	            return;
	        }
	        
	        Packet[] translated = PacketTranslatorRegister.translateToPC(client, packet);
            if (translated != null && translated.length > 0 && client.getDownstream() != null && client.getDownstream().isConnected()) {
                client.getDownstream().send(translated);
            }
        }
    }
}
