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
import java.util.Deque;

import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.gui.CustomFormComponent;
import org.dragonet.proxy.gui.InputComponent;
import org.dragonet.proxy.gui.LabelComponent;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.Protocol;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.packets.*;
import org.json.JSONArray;

public class PEPacketProcessor implements Runnable {

    public static final int MAX_PACKETS_PER_CYCLE = 200;

    private final UpstreamSession client;
    private final Deque<byte[]> packets = new ArrayDeque<>();

    public PEPacketProcessor(UpstreamSession client) {
        this.client = client;
    }

    public UpstreamSession getClient() {
        return client;
    }

    public void putPacket(byte[] packet) {
        packets.add(packet);
    }

    public void run() {
        int cnt = 0;
        while (cnt < MAX_PACKETS_PER_CYCLE && !packets.isEmpty()) {
            cnt++;
            byte[] p = packets.pop();
            PEPacket[] packets;
            try {
                packets = Protocol.decode(p);
                if (packets == null || packets.length <= 0)
                    continue;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            for (PEPacket decoded : packets)
                handlePacket(decoded);
        }

    }

    // this method should be in UpstreamSession
    public void handlePacket(PEPacket packet) {
        if (packet == null)
            return;

        switch (packet.pid()) {
            case ProtocolInfo.LOGIN_PACKET:
                client.onLogin((LoginPacket) packet);
                break;
            case ProtocolInfo.MOVE_PLAYER_PACKET:
                if (client.getDataCache().getOrDefault(CacheKey.AUTHENTICATION_STATE, "").equals("online_login_wait")) {

                    // client.getDataCache().put(CacheKey.AUTHENTICATION_STATE, "online_login");
                    ModalFormRequestPacket packetForm = new ModalFormRequestPacket();
                    CustomFormComponent form = new CustomFormComponent(client.getProxy().getLang().get(Lang.FORM_LOGIN_TITLE));
                    form.addComponent(new LabelComponent(client.getProxy().getLang().get(Lang.FORM_LOGIN_DESC)));
                    form.addComponent(new LabelComponent(client.getProxy().getLang().get(Lang.FORM_LOGIN_PROMPT)));
                    form.addComponent(new InputComponent(client.getProxy().getLang().get(Lang.FORM_LOGIN_USERNAME)).setPlaceholder("steve@example.com"));
                    form.addComponent(new InputComponent(client.getProxy().getLang().get(Lang.FORM_LOGIN_PASSWORD)).setPlaceholder("123456"));
                    packetForm.formId = 1;
                    packetForm.formData = form.serializeToJson().toString();
                    client.sendPacket(packetForm);
                    break;
                }
            case ProtocolInfo.MODAL_FORM_RESPONSE_PACKET:
                if (client.getDataCache().getOrDefault(CacheKey.AUTHENTICATION_STATE, "").equals("online_login_wait")) {

                    client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_LOGIN_PROGRESS));
                    client.getDataCache().remove(CacheKey.AUTHENTICATION_STATE);

                    ModalFormResponsePacket formResponse = (ModalFormResponsePacket) packet;
                    JSONArray array = new JSONArray(formResponse.formData);
                    client.authenticate(array.get(2).toString(), array.get(3).toString());
                    break;
                }
            case ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET:
                if (client.isLoggedIn())
                    return;
                client.postLogin();
                break;
            case ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET:
                client.sendPacket(new ChunkRadiusUpdatedPacket(((RequestChunkRadiusPacket) packet).radius));
                break;
            default:
                if (client.getDownstream() == null)
                    break;
                if (!client.getDownstream().isConnected())
                    break;
                Packet[] translated = PacketTranslatorRegister.translateToPC(client, packet);
                if (translated == null || translated.length == 0)
                    break;
                client.getDownstream().send(translated);
                break;
        }
    }
}
