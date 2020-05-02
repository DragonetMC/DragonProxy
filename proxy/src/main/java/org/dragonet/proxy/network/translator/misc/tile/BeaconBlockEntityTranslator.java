package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;

@BlockEntityRegisterInfo(bedrockId = "Beacon")
public class BeaconBlockEntityTranslator implements IBlockEntityTranslator {

    @Override
    public void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag, String javaId) {
        // TODO: validation
        builder.intTag("Primary", (int) javaTag.get("Primary").getValue());
        builder.intTag("Secondary", (int) javaTag.get("Secondary").getValue());
        builder.intTag("Levels", (int) javaTag.get("Levels").getValue());
        builder.stringTag("Lock", "");
    }
}
