package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.data.game.command.CommandNode;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.command.SuggestionType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDeclareCommandsPacket;
import com.nukkitx.protocol.bedrock.data.CommandData;
import com.nukkitx.protocol.bedrock.data.CommandEnumData;
import com.nukkitx.protocol.bedrock.data.CommandParamData;
import com.nukkitx.protocol.bedrock.data.CommandParamType;
import com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@PCPacketTranslator(packetClass = ServerDeclareCommandsPacket.class)
public class PCDeclareCommandsTranslator extends PacketTranslator<ServerDeclareCommandsPacket> {

    @Override
    //@SuppressWarnings("unchecked")
    public void translate(ProxySession session, ServerDeclareCommandsPacket packet) {
        Int2ObjectMap<CommandNode> commands = new Int2ObjectOpenHashMap();
        Int2ObjectMap<CommandNode> arguments = new Int2ObjectOpenHashMap();

        CommandNode rootNode = null;

        for(int i = 0; i < packet.getNodes().length; i++) {
            CommandNode node = packet.getNodes()[i];

            //log.warn(node.getType().name() + " - " + i + " .. " + node.getName() + "  //  " + Arrays.toString(node.getChildIndices()));

            switch(node.getType()) {
                case ROOT:
                    rootNode = node;
                    break;
                case LITERAL:
                    for(int childIndex : rootNode.getChildIndices()) {
                        if(childIndex == i) {
                            commands.put(i, node);
                        }
                    }
                    break;
                case ARGUMENT:
                    arguments.put(i, node);
                    break;
            }
        }

        AvailableCommandsPacket availableCommandsPacket = new AvailableCommandsPacket();

        for(CommandNode command : commands.values()) {
            String commandName = command.getName();
            String[] aliasesEnum = new String[]{commandName.toLowerCase()};
            CommandEnumData aliases = new CommandEnumData(commandName + "Aliases", aliasesEnum, false);

            availableCommandsPacket.getCommands().add(
                new CommandData(commandName, "Remote server command", Collections.emptyList(),
                    (byte) 0, aliases, new CommandParamData[][]{}));
        }

        session.sendPacket(availableCommandsPacket);
    }
}
