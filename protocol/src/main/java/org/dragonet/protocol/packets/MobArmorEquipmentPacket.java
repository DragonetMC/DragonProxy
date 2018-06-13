package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.data.inventory.Slot;

public class MobArmorEquipmentPacket extends PEPacket {

    public long rtid;
    public Slot helmet;
    public Slot chestplate;
    public Slot leggings;
    public Slot boots;

    @Override
    public int pid() {
        return ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        putSlot(helmet);
        putSlot(chestplate);
        putSlot(leggings);
        putSlot(boots);

    }

    @Override
    public void decodePayload() {
        rtid = getUnsignedVarLong();
        helmet = getSlot();
        chestplate = getSlot();
        leggings = getSlot();
        boots = getSlot();
    }

}
