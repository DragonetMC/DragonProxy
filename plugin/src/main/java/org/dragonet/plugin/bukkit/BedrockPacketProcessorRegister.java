package org.dragonet.plugin.bukkit;

import org.dragonet.plugin.bukkit.processors.ContainerCloseProcessor;
import org.dragonet.plugin.bukkit.processors.ModalFormResponseProcessor;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.ContainerClosePacket;
import org.dragonet.protocol.packets.InventoryTransactionPacket;
import org.dragonet.protocol.packets.ModalFormResponsePacket;

import java.util.HashMap;
import java.util.Map;
import org.dragonet.plugin.bukkit.processors.InventoryTransactionProcessor;

public final class BedrockPacketProcessorRegister {

    private final static Map<Class<? extends PEPacket>, BedrockPacketProcessor<? extends PEPacket>> processors = new HashMap<>();

    static {
        processors.put(ContainerClosePacket.class, new ContainerCloseProcessor());
        processors.put(InventoryTransactionPacket.class, new InventoryTransactionProcessor());
        processors.put(ModalFormResponsePacket.class, new ModalFormResponseProcessor());
    }

    public static <P extends PEPacket> BedrockPacketProcessor<P> getHandler(PEPacket packet) {
        if(packet == null) return null;
        if(processors.containsKey(packet.getClass())) {
            return (BedrockPacketProcessor<P>) processors.get(packet.getClass());
        }
        return null;
    }

    public static void setProcessor(Class<? extends PEPacket> clazz, BedrockPacketProcessor<? extends PEPacket> processor) {
        processors.put(clazz, processor);
    }

}
