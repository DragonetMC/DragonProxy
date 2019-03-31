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
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.nukkitx.protocol.bedrock.packet.SetDifficultyPacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.UpstreamSession;
import org.dragonet.proxy.network.translator.IPacketTranslator;

public class PCServerDifficultyTranslator implements IPacketTranslator<ServerDifficultyPacket> {

    @Override
    public void translate(BedrockSession<UpstreamSession> session, ServerDifficultyPacket packet) {
        SetDifficultyPacket bedrockPacket = new SetDifficultyPacket();
        bedrockPacket.setDifficulty(packet.getDifficulty().ordinal());

        session.sendPacket(bedrockPacket);
    }
}
