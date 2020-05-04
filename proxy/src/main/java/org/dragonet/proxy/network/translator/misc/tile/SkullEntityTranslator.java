package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;

@Log4j2
@BlockEntityRegisterInfo(bedrockId = "Skull")
public class SkullEntityTranslator implements IBlockEntityTranslator {

    @Override
    public void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag, String javaId) {
        if(!javaId.contains("head") && !javaId.contains("skull"))
            return;

        String skullType = javaId.substring(0, javaId.indexOf("[")).replace("minecraft:", "");
        if(javaId.contains("wall")) {
            skullType = skullType.replace("_wall_skull", "_skull");
            skullType = skullType.replace("_wall_head", "");
            builder.byteTag("SkullType", (byte) SkullType.valueOf(skullType.toUpperCase()).getId());
        } else {
            skullType = skullType.replace("_head", "").toUpperCase();
            float rotation = getRotation(Float.parseFloat(javaId.substring(javaId.indexOf("rotation=") + 9, javaId.indexOf("]"))));
            builder.byteTag("SkullType", (byte) SkullType.valueOf(skullType).getId());
            builder.floatTag("Rotation", rotation);
        }
    }

    private float getRotation(float javaRotation) {
        return (float) (javaRotation * 22.5);
    }

    private enum SkullType {
        SKELETON_SKULL(0),
        WITHER_SKELETON_SKULL(1),
        ZOMBIE(2),
        PLAYER(3),
        CREEPER(4),
        DRAGON(5);

        @Getter
        private int id;

        SkullType(int id) {
            this.id = id;
        }

    }
}
