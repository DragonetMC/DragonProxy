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

import com.github.steveice10.mc.protocol.data.message.TranslationMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.nukkitx.protocol.bedrock.packet.TextPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.types.MessageTranslator;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCChatTranslator implements PacketTranslator<ServerChatPacket> {
    public static final PCChatTranslator INSTANCE = new PCChatTranslator();

    @Override
    public void translate(ProxySession session, ServerChatPacket packet) {
        TextPacket textPacket = new TextPacket();
        textPacket.setPlatformChatId("");
        textPacket.setSourceName("");
        textPacket.setXuid("");

        switch(packet.getType()) {
            case CHAT:
                textPacket.setType(TextPacket.Type.CHAT);
                break;
            case NOTIFICATION:
                textPacket.setType(TextPacket.Type.TIP);
            case SYSTEM:
                textPacket.setType(TextPacket.Type.SYSTEM);
                break;
            default:
                textPacket.setType(TextPacket.Type.RAW);
                break;
        }

        if(packet.getMessage() instanceof TranslationMessage) {
            log.warn("needs translation");

            textPacket.setNeedsTranslation(true);
            textPacket.setType(TextPacket.Type.TRANSLATION);
            textPacket.setMessage(MessageTranslator.translationTranslateText((TranslationMessage) packet.getMessage()));
            textPacket.setParameters(MessageTranslator.translationTranslateParams(((TranslationMessage) packet.getMessage()).getTranslationParams()));
        } else {
            textPacket.setNeedsTranslation(false);
            textPacket.setMessage(MessageTranslator.translate(packet.getMessage()));
        }

        session.getBedrockSession().sendPacket(textPacket);
    }
}
