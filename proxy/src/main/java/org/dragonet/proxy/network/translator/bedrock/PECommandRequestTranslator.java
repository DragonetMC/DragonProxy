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
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.packet.CommandRequestPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.command.CommandManager;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
public class PECommandRequestTranslator implements PacketTranslator<CommandRequestPacket> {
    public static final PECommandRequestTranslator INSTANCE = new PECommandRequestTranslator();

    @Override
    public void translate(ProxySession session, CommandRequestPacket packet) {
        CommandManager commandManager = DragonProxy.INSTANCE.getCommandManager();
        // TODO: better command management
        String command = packet.getCommand().replace("/", "");
        if(commandManager.getCommands().containsKey(command)) {
            commandManager.executeCommand(session, command);
            return;
        }

        ClientChatPacket chatPacket = new ClientChatPacket(packet.getCommand());
        session.getDownstream().getSession().send(chatPacket);
    }
}
