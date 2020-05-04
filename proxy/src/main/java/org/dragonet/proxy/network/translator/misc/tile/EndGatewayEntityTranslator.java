package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.tag.IntTag;
import com.nukkitx.nbt.tag.ListTag;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

@Log4j2
@BlockEntityRegisterInfo(bedrockId = "EndGateway")
public class EndGatewayEntityTranslator implements IBlockEntityTranslator {

    @Override
    public void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag, String javaId) {
        builder.intTag("Age", Long.valueOf(javaTag.get("Age").getValue().toString()).intValue());
        builder.byteTag("ExactLocation", (byte) 1);

        // Values not needed as java manages it all
        List<IntTag> exitPortal = Arrays.asList(new IntTag("x", 0), new IntTag("y", 0), new IntTag("z", 0));
        builder.tag(new ListTag("ExitPortal", IntTag.class, exitPortal));
    }
}
