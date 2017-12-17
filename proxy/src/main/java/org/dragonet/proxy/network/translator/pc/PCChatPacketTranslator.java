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
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.message.TranslationMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.MessageTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.TextPacket;

public class PCChatPacketTranslator implements IPCPacketTranslator<ServerChatPacket> {
	// vars
	public static final byte TYPE_RAW = 0;
	public static final byte TYPE_CHAT = 1;
	public static final byte TYPE_TRANSLATION = 2;
	public static final byte TYPE_POPUP = 3;
	public static final byte TYPE_TIP = 4;
	public static final byte TYPE_SYSTEM = 5;
	public static final byte TYPE_WHISPER = 6;
	public static final byte TYPE_ANNOUNCEMENT = 7;

	// constructor
	public PCChatPacketTranslator() {

	}

	// public
	public PEPacket[] translate(UpstreamSession session, ServerChatPacket packet) {
		TextPacket pe = new TextPacket();
                if (packet.getMessage() instanceof TranslationMessage)
                {
                    pe.type = TYPE_TRANSLATION;
                    pe.message = pe.message = MessageTranslator.translate(packet.getMessage());
                }
                else
                {
                    pe.type = TYPE_RAW;
                    pe.message = pe.message = MessageTranslator.translate(packet.getMessage());
                }

		return new PEPacket[] { pe };

		// TODO: Detect type
		/*
		 * Reset the chat message so we can parse the JSON again (if needed)
		 */
		/*
		 * switch (packet.getType()) { case CHAT: ret.type = ChatPacket.TextType.CHAT;
		 * break; case NOTIFICATION: case SYSTEM: default: ret.type =
		 * ChatPacket.TextType.CHAT; break; } return new PEPacket[]{ret};
		 */
	}

	// private

}
