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

import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import java.util.ArrayDeque;
import java.util.Deque;

import com.github.steveice10.packetlib.packet.Packet;
import com.google.gson.JsonArray;
import org.dragonet.common.utilities.JsonUtil;
import java.net.InetSocketAddress;
import java.net.Proxy;
import org.dragonet.protocol.packets.*;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.common.gui.CustomFormComponent;
import org.dragonet.common.gui.InputComponent;
import org.dragonet.common.gui.LabelComponent;
import org.dragonet.common.utilities.BinaryStream;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.dragonet.api.configuration.IServerConfig;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IPEPacketProcessor;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.protocol.Protocol;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.events.defaults.packets.PacketfromPlayerEvent;

public class PEPacketProcessor implements IPEPacketProcessor {

    public final static int MAX_PACKETS_PER_CYCLE = 200;

    private final static Set<Class<? extends PEPacket>> FORWARDED_PACKETS;

    static {
        Set<Class<? extends PEPacket>> packets = new HashSet<>();
        packets.add(InventoryTransactionPacket.class);
        packets.add(ContainerClosePacket.class);
        packets.add(ModalFormResponsePacket.class);

        FORWARDED_PACKETS = Collections.unmodifiableSet(packets);
    }

    private final AtomicBoolean enableForward = new AtomicBoolean();

    private final IUpstreamSession client;
    private final Deque<byte[]> packets = new ArrayDeque<>();

    private Proxy authProxy = null;

    public PEPacketProcessor(IUpstreamSession client) {
        IServerConfig config = DragonProxy.getInstance().getConfig();

        if (config.getProxy_type().equalsIgnoreCase("none") || config.getProxy_type().equalsIgnoreCase("direct"))
            authProxy = null;
        else {
            Proxy.Type type = Proxy.Type.valueOf(config.getProxy_type().toUpperCase());
            if (type != null)
                authProxy = new Proxy(type, new InetSocketAddress(config.getProxy_ip(), config.getProxy_port()));
            else
                authProxy = null;
        }

        this.client = client;
    }

    @Override
    public IUpstreamSession getClient() {
        return client;
    }

    @Override
    public void putPacket(byte[] packet) {
        packets.add(packet);
    }

    @Override
    public void onTick() {
        int cnt = 0;
        Timings.playerNetworkReceiveTimer.startTiming();
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
                try (Timing timing = Timings.getReceiveDataPacketTiming(decoded)) {
                    handlePacket(decoded);
                }
        }
        Timings.playerNetworkReceiveTimer.stopTiming();
    }

    // this method should be in UpstreamSession
    @Override
    public void handlePacket(PEPacket packet) {
        if (packet == null)
            return;

        if(!client.getProxy().getConfig().getDisable_packet_events()){
            PacketfromPlayerEvent packetEvent = new PacketfromPlayerEvent(client, packet);
            client.getProxy().getEventManager().callEvent(packetEvent);
            if(packetEvent.isCancelled​()){
                return;
            }
        }

        // Wait for player logginig in
        if ("online_login_wait".equals(this.client.getDataCache().get(CacheKey.AUTHENTICATION_STATE))) {
            if (packet.pid() == ProtocolInfo.MOVE_PLAYER_PACKET) {

                InputComponent username = new InputComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_USERNAME)).setPlaceholder("steve@example.com");
                InputComponent password = new InputComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_PASSWORD)).setPlaceholder("123456");

                if (this.client.getProxy().getConfig().getAuto_login()) {
                    username.setDefaultValue(this.client.getProxy().getConfig().getOnline_username());
                    password.setDefaultValue(this.client.getProxy().getConfig().getOnline_password());
                }

                // client.getDataCache().put(CacheKey.AUTHENTICATION_STATE, "online_login");
                ModalFormRequestPacket packetForm = new ModalFormRequestPacket();
                CustomFormComponent form = new CustomFormComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_TITLE));
                form.addComponent(new LabelComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_DESC)));
                form.addComponent(new LabelComponent(this.client.getProxy().getLang().get(Lang.FORM_LOGIN_PROMPT)));
                form.addComponent(username);
                form.addComponent(password);
                packetForm.formId = 1;
                packetForm.formData = form.serializeToJson().toString();
                this.client.sendPacket(packetForm, true);
                return;
            }

            if (packet.pid() == ProtocolInfo.MODAL_FORM_RESPONSE_PACKET) {
                try {
                    this.client.sendChat(this.client.getProxy().getLang().get(Lang.MESSAGE_LOGIN_PROGRESS));

                    ModalFormResponsePacket formResponse = (ModalFormResponsePacket) packet;
                    JsonArray array = JsonUtil.parseArray(formResponse.formData);
                    this.client.getDataCache().remove(CacheKey.AUTHENTICATION_STATE);
                    this.client.authenticate(array.get(2).getAsString(), array.get(3).getAsString(), authProxy);
                } catch(Exception ex) {
                    this.client.sendChat(this.client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_LOGIN_FAILD));
                }
                return;
            }
        }

        switch (packet.pid()) {
        case ProtocolInfo.BATCH_PACKET:
            DragonProxy.getInstance().getLogger().debug("Received batch packet from client !");
            break;
        case ProtocolInfo.LOGIN_PACKET:
            this.client.onLogin((LoginPacket) packet);
            break;
        case ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET:
            if (!this.client.isLoggedIn())
                this.client.postLogin();

            break;
        default:
            if (this.client.getDownstream() == null || !this.client.getDownstream().isConnected())
                break;

            if (enableForward.get() && FORWARDED_PACKETS.contains(packet.getClass())) {
                BinaryStream bis = new BinaryStream();
                bis.putString("PacketForward");
                bis.putByteArray(packet.getBuffer());
                ClientPluginMessagePacket msg = new ClientPluginMessagePacket("DragonProxy", bis.getBuffer());
                client.getDownstream().send(msg);
            } else
                // IMPORTANT Do not send packet until client is connected !
                if (client.isSpawned()) {
                    Packet[] translated = PacketTranslatorRegister.translateToPC(this.client, packet);
                    if (translated == null || translated.length == 0)
                        break;

                    client.getDownstream().send(translated);
                }
            break;
        }
    }

    public void setPacketForwardMode(boolean enabled) {
        enableForward.set(enabled);
    }
}
