package org.dragonet.proxy.network.translator.bedrock.world;

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.packet.SetDefaultGameTypePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = SetDefaultGameTypePacket.class)
public class PESetDefaultGameTypeTranslator extends PacketTranslator<SetDefaultGameTypePacket> {

    @Override
    public void translate(ProxySession session, SetDefaultGameTypePacket packet) {
        // Tell the server to update the default game mode and then its up to the server whether it happens or not
        session.sendRemotePacket(new ClientChatPacket("/defaultgamemode " + GameMode.values()[packet.getGamemode()].name().toLowerCase()));
    }
}
