package org.dragonet.proxy.network.translator.pc;

import java.util.ArrayList;

import org.dragonet.common.data.blocks.GlobalBlockPalette;
import org.dragonet.common.data.itemsblocks.ItemEntry;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.LevelEventPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.ParticleTranslator;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.Particle;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;

public class PCSpawnParticlePacketTranslator implements IPCPacketTranslator<ServerSpawnParticlePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnParticlePacket packet) {
        ArrayList<PEPacket> packets = new ArrayList<PEPacket>();
        if (packet.getParticle() == Particle.BLOCK_CRACK || packet.getParticle() == Particle.BLOCK_DUST) {
            LevelEventPacket pk = new LevelEventPacket();
            pk.eventId = LevelEventPacket.EVENT_PARTICLE_DESTROY;
            pk.position = new Vector3F(packet.getX(), packet.getY(), packet.getZ());
            Position pos = new Position((int)packet.getX(), (int)packet.getY(), (int)packet.getZ());
            ItemEntry entry = ItemBlockTranslator.translateToPE(session.getChunkCache().getBlock(pos).getId(), session.getChunkCache().getBlock(pos).getData());
            if (session.getChunkCache().getBlock(pos) != null)
                pk.data = GlobalBlockPalette.getOrCreateRuntimeId(entry.getId(), entry.getPEDamage());
            else
                pk.data = GlobalBlockPalette.getOrCreateRuntimeId(1);
            packets.add(pk);
        } else {
            int num = ParticleTranslator.getInstance().translate(packet.getParticle());
            session.getProxy().getLogger().info(Integer.toString(num));
            if (num != -1) {
                LevelEventPacket pk = new LevelEventPacket();
                pk.eventId = (short) (LevelEventPacket.EVENT_ADD_PARTICLE_MASK | num);
                pk.position = new Vector3F(packet.getX(), packet.getY(), packet.getZ());
                pk.data = 0;  // TODO ?
                packets.add(pk);
            }
        }
        if (!packets.isEmpty()) {
            return packets.toArray(new PEPacket[packets.size()]);
        } else {
            return null;
        }
    }

}
