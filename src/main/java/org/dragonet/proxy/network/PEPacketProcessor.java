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

import java.util.ArrayDeque;
import java.util.Deque;

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

    public void handlePacket(PEPacket packet) {
        if (packet == null)
            return;

        // Wait for player logginig in
        if ("online_login_wait".equals(this.client.getDataCache().get(CacheKey.AUTHENTICATION_STATE))) {
            if (packet.pid() == ProtocolInfo.MOVE_PLAYER_PACKET) {

                // client.getDataCache().put(CacheKey.AUTHENTICATION_STATE, "online_login");
                ModalFormRequestPacket packetForm = new ModalFormRequestPacket();
                CustomFormComponent form = new CustomFormComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_TITLE));
                form.addComponent(new LabelComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_DESC)));
                form.addComponent(new LabelComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_PROMPT)));
                form.addComponent(new InputComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_USERNAME)).setPlaceholder("steve@example.com"));
                form.addComponent(new InputComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_PASSWORD)).setPlaceholder("123456"));
                packetForm.formId = 1;
                packetForm.formData = form.serializeToJson().toString();
                this.client.sendPacket(packetForm);
                return;
            }

            if (packet.pid() == ProtocolInfo.MODAL_FORM_RESPONSE_PACKET) {

                this.client.sendChat(this.client.getProxy().getLang().get(Lang.MESSAGE_LOGIN_PROGRESS));
                this.client.getDataCache().remove(CacheKey.AUTHENTICATION_STATE);

                ModalFormResponsePacket formResponse = (ModalFormResponsePacket) packet;
                JSONArray array = new JSONArray(formResponse.formData);
                this.client.authenticate(array.get(2).toString(), array.get(3).toString());
                return;
            }
        }

        switch (packet.pid()) {
            case ProtocolInfo.LOGIN_PACKET:
                this.client.onLogin((LoginPacket) packet);
                break;
            case ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET:
                if (!this.client.isLoggedIn())
                    this.client.postLogin();

                break;
            case ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET:
                this.client.sendPacket(new ChunkRadiusUpdatedPacket(((RequestChunkRadiusPacket) packet).radius));
                break;
            default:
                if (this.client.getDownstream() == null || !this.client.getDownstream().isConnected())
                    break;

                Packet[] translated = PacketTranslatorRegister.translateToPC(this.client, packet);
                if (translated == null || translated.length == 0)
                    break;

                client.getDownstream().send(translated);
                break;
        }
    }
}
