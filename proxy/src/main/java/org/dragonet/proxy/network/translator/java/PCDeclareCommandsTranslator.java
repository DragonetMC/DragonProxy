package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.data.game.command.CommandNode;
import com.github.steveice10.mc.protocol.data.game.command.CommandParser;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.command.ProxyCommand;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

import java.util.*;

/**
 * Thanks to Bungeecord for implementing this packet as i used
 * it as an initial reference, however the end result is quite
 * different to the original code.
 *
 * To use this translator, modify the config to set `enable-commands` to `true`.
 *
 * Stil to do:
 * - Fix arguments being a child of the root node when the commands arent sent (hivemc.eu)
 * - Implement multiple overload support
 */
@Log4j2
@PCPacketTranslator(packetClass = ServerDeclareCommandsPacket.class)
public class PCDeclareCommandsTranslator extends PacketTranslator<ServerDeclareCommandsPacket> {
    private static Map<CommandParser, CommandParamData.Type> parserTypeMap = new HashMap<>();

    static {
        parserTypeMap.put(CommandParser.STRING, CommandParamData.Type.STRING);
        parserTypeMap.put(CommandParser.MESSAGE, CommandParamData.Type.MESSAGE);
        parserTypeMap.put(CommandParser.ENTITY, CommandParamData.Type.TARGET);
        parserTypeMap.put(CommandParser.BOOL, CommandParamData.Type.STRING);
        parserTypeMap.put(CommandParser.INTEGER, CommandParamData.Type.INT);
        parserTypeMap.put(CommandParser.FLOAT, CommandParamData.Type.FLOAT);
        parserTypeMap.put(CommandParser.DOUBLE, CommandParamData.Type.FLOAT);
        parserTypeMap.put(CommandParser.BLOCK_POS, CommandParamData.Type.POSITION);
        //parserTypeMap.put(CommandParser.COLUMN_POS, CommandParamData.Type.POSITION); // TODO: verify
        parserTypeMap.put(CommandParser.INT_RANGE, CommandParamData.Type.INT_RANGE);
        parserTypeMap.put(CommandParser.GAME_PROFILE, CommandParamData.Type.TARGET); // TODO
        //parserTypeMap.put(CommandParser.COMPONENT, CommandParamData.Type.JSON); // TODO: verify

        // TODO: somehow map appropriate strings to text
    }

    private ProxyCommandNode root = null;

    @Override
    public void translate(ProxySession session, ServerDeclareCommandsPacket packet) {
        if(!session.getProxy().getConfiguration().isCommandsEnabled()) {
            log.debug("Declare commands packet received, but command translation is not enabled in the config.");
            return;
        }

        ProxyCommandNode[] nodes = new ProxyCommandNode[packet.getNodes().length];
        Deque<ProxyCommandNode> nodeQueue = new ArrayDeque<>( nodes.length );

        for(int i = 0; i < packet.getNodes().length; i++) {
            CommandNode node = packet.getNodes()[i];
            ProxyCommandNode proxyNode = new ProxyCommandNode(node, node.getName());

            if(node.getType().equals(CommandType.ROOT)) {
                root = proxyNode;
            }

            nodes[i] = proxyNode;
            nodeQueue.add(proxyNode);
        }

        nodeQueue.removeIf(node -> buildNode(node, nodes));

        AvailableCommandsPacket availableCommandsPacket = new AvailableCommandsPacket();

        final Map<String, List<ProxyCommandArgument>> overloadsMap = new LinkedHashMap<>();

        for(ProxyCommandNode pnode : root.getChildren()) {
            final ArrayList<ProxyCommandArgument> result = new ArrayList<>();
            getArguments(pnode.getName(), pnode, result);
            overloadsMap.put(pnode.getName(), result);
        }

        for (Map.Entry<String, List<ProxyCommandArgument>> entry : overloadsMap.entrySet()) {
            int index = 0;
            CommandParamData[][] overloads = new CommandParamData[1][]; // TODO: overloads!
            CommandParamData[] params = new CommandParamData[entry.getValue().size()];

            for (int i2 = 0; i2 < params.length; i2++) {
                //log.info("PARAM: " + i2 + "  " + entry.getValue().get(i2).getName());
                ProxyCommandArgument argument = entry.getValue().get(i2);
                CommandParamData.Type type = parserTypeMap.get(argument.getParser());
                if (type == null) {
                    type = CommandParamData.Type.STRING;
                }

                CommandEnumData enumData = null;

                // If it is a subcommand
                if(argument.getParser() == null) {
                    enumData = new CommandEnumData(argument.getName(), new String[]{argument.getName()}, false);
                }

                params[i2] = new CommandParamData(argument.getName(), false, enumData, type, null, Collections.emptyList());
            }

            overloads[index++] = params;

            String commandName = entry.getKey();
            String[] aliasesEnum = new String[]{commandName.toLowerCase()};
            CommandEnumData aliases = new CommandEnumData(commandName + "Aliases", aliasesEnum, false);

            availableCommandsPacket.getCommands().add(
                new CommandData(commandName, "Remote server command", Collections.emptyList(),
                    (byte) 0, aliases, overloads));
        }

        for(CommandData d : availableCommandsPacket.getCommands()) {
            log.info(d.getName() + " " + d.getOverloads().length + " // " + Arrays.deepToString(d.getOverloads()));
        }

        session.sendPacket(availableCommandsPacket);
    }


    private boolean buildNode(ProxyCommandNode command, ProxyCommandNode[] nodes) {
        for(int childIndex : command.getProtocolLibNode().getChildIndices()) {
            command.getChildren().add(nodes[childIndex]);
        }
        return true;
    }

    private void getArguments(String originalCommandName, ProxyCommandNode node, final ArrayList<ProxyCommandArgument> result) {
        if(!node.getName().equalsIgnoreCase(originalCommandName)) {
            // Dont add the original command to the result
            boolean optional = !node.getChildren().isEmpty();
            result.add(new ProxyCommandArgument(node.getName(), node.getProtocolLibNode().getParser(), optional));
        }

        for(ProxyCommandNode child : node.getChildren()) {
            getArguments(originalCommandName, child, result);
        }
    }

    @RequiredArgsConstructor
    @Getter
    private static class ProxyCommandNode {
        private final CommandNode protocolLibNode;
        private final String name;
        private List<ProxyCommandNode> children = new ArrayList<>();
    }

    @RequiredArgsConstructor
    @Getter
    private static class ProxyCommandArgument {
        private final String name;
        private final CommandParser parser;
        private final boolean optional;
    }
}
