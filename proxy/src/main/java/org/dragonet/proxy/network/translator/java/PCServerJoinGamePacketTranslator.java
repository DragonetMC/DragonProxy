package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCServerJoinGamePacketTranslator implements PacketTranslator<ServerJoinGamePacket> {
    public static final PCServerJoinGamePacketTranslator INSTANCE = new PCServerJoinGamePacketTranslator();

    @Override
    public void translate(ProxySession session, ServerJoinGamePacket packet) {
        StartGamePacket startGamePacket = new StartGamePacket();
        startGamePacket.setLevelGamemode(packet.getGameMode().ordinal());
        startGamePacket.setPlayerGamemode(packet.getGameMode().ordinal());
        startGamePacket.setDifficulty(packet.getDifficulty().ordinal());
        startGamePacket.setUniqueEntityId(packet.getEntityId());
        startGamePacket.setRuntimeEntityId(packet.getEntityId());

        // TODO: 01/04/2019 Add support for deserializing the chunk in the protocol library

        //session.getUpstream().sendPacketImmediately(startGamePacket);
    }
}
