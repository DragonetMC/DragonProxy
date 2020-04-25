package org.dragonet.proxy.network.translator.bedrock.world;

import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSetDifficultyPacket;
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
        Difficulty difficulty = getDifficulty(packet.getDifficulty());
        if(difficulty != null) {
            session.sendRemotePacket(new ClientSetDifficultyPacket(difficulty));
        }
    }

    /**
     * This point of this method is because sometimes the bedrock client sends weird
     * values like -2?
     */
    private Difficulty getDifficulty(int difficiulty) {
        switch(difficiulty) {
            case 0: return Difficulty.PEACEFUL;
            case 1: return Difficulty.EASY;
            case 2: return Difficulty.NORMAL;
            case 3: return Difficulty.HARD;
        }
        return null;
    }
}
