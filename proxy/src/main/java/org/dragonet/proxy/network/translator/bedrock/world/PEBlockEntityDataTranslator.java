package org.dragonet.proxy.network.translator.bedrock.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientSetBeaconEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientUpdateSignPacket;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PEPacketTranslator;

@Log4j2
@PEPacketTranslator(packetClass = BlockEntityDataPacket.class)
public class PEBlockEntityDataTranslator extends PacketTranslator<BlockEntityDataPacket> {

    @Override
    public void translate(ProxySession session, BlockEntityDataPacket packet) {
        if(!(packet.getData() instanceof CompoundTag)) {
            return;
        }
        CompoundTag tag = (CompoundTag) packet.getData();

        log.warn(tag);

        switch(tag.getString("id")) {
            case "Sign":
                //ClientUpdateSignPacket clientUpdateSignPacket = new ClientUpdateSignPacket(new Position());
                break;
            case "Beacon":
                session.sendRemotePacket(new ClientSetBeaconEffectPacket(tag.getInt("primary"), tag.getInt("secondary")));
                break;
        }
    }
}
