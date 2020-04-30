package org.dragonet.proxy.hybrid.messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
//import com.nukkitx.math.vector.Vector3f;
//import com.nukkitx.math.vector.Vector3i;
//import com.nukkitx.protocol.bedrock.data.EntityData;
//import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
//import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import org.dragonet.proxy.hybrid.AbstractHybridMessageHandler;
import org.dragonet.proxy.hybrid.HybridMessage;
//import org.dragonet.proxy.network.hybrid.HybridMessageHandler;

@Getter
public class SetEntityDataMessage implements HybridMessage {
//    private static final EntityData[] ENTITY_DATA = EntityData.values();

    private int entityId;
//    private Object2ObjectMap<EntityData, Object> entityDataMap = new Object2ObjectOpenHashMap<>();

    @Override
    public ByteArrayDataOutput encode(ByteArrayDataOutput out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decode(ByteArrayDataInput in) {
        entityId = in.readInt();
        String name = in.readUTF(); // TODO: change this to use integers

        Object value = null;
        switch(in.readInt()) { // Type
            case 0: // byte
                value = in.readByte();
                break;
            case 1: // short
                value = in.readShort();
                break;
            case 2: // int
                value = in.readInt();
                break;
            case 3: // float
                value = in.readFloat();
                break;
            case 4: // string
                value = in.readUTF();
                break;
            case 5: // nbt
                // TODO
                break;
            case 6: // vector3i
//                value = Vector3i.from(in.readInt(), in.readInt(), in.readInt());
                break;
            case 7: // long
                value = in.readLong();
                break;
            case 8: // vector3f
//                value = Vector3f.from(in.readFloat(), in.readFloat(), in.readFloat());
                break;
        }

        if(value == null) {
            return;
        }

//        for(EntityData entityData : ENTITY_DATA) {
//            if(entityData.name().equalsIgnoreCase(name)) {
//                entityDataMap.put(entityData, value);
//            }
//        }
    }

    @Override
    public void handle(AbstractHybridMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String getId() {
        return "SetEntityData";
    }
}
