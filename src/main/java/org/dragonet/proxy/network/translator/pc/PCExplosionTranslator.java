package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.game.world.block.ExplodedBlockRecord;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.packets.ExplodePacket;
import org.dragonet.common.mcbedrock.utilities.BlockPosition;
import org.dragonet.common.mcbedrock.utilities.Vector3F;

import java.util.ArrayList;

public class PCExplosionTranslator implements IPCPacketTranslator<ServerExplosionPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerExplosionPacket packet) {

        ExplodePacket pk = new ExplodePacket();

        pk.position = new Vector3F(packet.getX(), packet.getY(), packet.getZ());
        pk.radius = packet.getRadius();
        pk.destroyedBlocks = new ArrayList<>(packet.getExploded().size());

        for (ExplodedBlockRecord record : packet.getExploded())
            pk.destroyedBlocks.add(new BlockPosition(record.getX(), record.getY(), record.getZ()));

        return new PEPacket[]{pk};
    }

}
