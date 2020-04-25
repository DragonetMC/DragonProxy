package org.dragonet.proxy.network.translator.bedrock.world;

import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.packet.SetDifficultyPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = SetDifficultyPacket.class)
public class PESetDifficultyTranslator extends PacketTranslator<SetDifficultyPacket> {

    @Override
    public void translate(ProxySession session, SetDifficultyPacket packet) {
        // Reset the difficulty on the client
        SetDifficultyPacket setDifficultyPacket = new SetDifficultyPacket();
        setDifficultyPacket.setDifficulty(session.getWorldCache().getDifficulty().ordinal());
        session.sendPacket(setDifficultyPacket);

        // Tell the server to update the difficulty, and then its up to the server whether it happens or not
        session.sendRemotePacket(new ClientChatPacket("/difficulty " + Difficulty.values()[packet.getDifficulty()].name().toLowerCase()));
    }
}
