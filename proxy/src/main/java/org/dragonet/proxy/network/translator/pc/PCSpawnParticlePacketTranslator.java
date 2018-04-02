package org.dragonet.proxy.network.translator.pc;

import java.util.ArrayList;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.LevelEventPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ParticleTranslator;

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
            pk.data = packet.getData()[0];
            packets.add(pk);
        } else {
            int num = ParticleTranslator.getInstance().translate(packet.getParticle());
            if (num != -1) {
                LevelEventPacket pk = new LevelEventPacket();
                pk.eventId = LevelEventPacket.EVENT_ADD_PARTICLE_MASK;
                pk.position = new Vector3F(packet.getX(), packet.getY(), packet.getZ());
                pk.data = num;
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
