package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageStyle;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.util.DyeColor;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;

@Log4j2
@BlockEntityRegisterInfo(bedrockId = "Sign")
public class SignBlockEntityTranslator implements IBlockEntityTranslator {

    @Override
    public void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag, String javaId) {
        StringBuilder signText = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            int currentLine = i+1;
            Message message = Message.fromString(javaTag.get("Text" + currentLine).getValue().toString());

            String signColor = javaTag.get("Color").getValue().toString().toUpperCase();
            if(!signColor.equalsIgnoreCase("BLACK")) {
                message.getExtra().forEach(messageExtra -> {
                    messageExtra.setStyle(new MessageStyle().setColor(DyeColor.valueOf(signColor).getChatColor()));
                });
            }

            String signLine = MessageTranslator.translate(message);

            if(signLine.contains("="))
                signLine = signLine.substring(0, Math.min(signLine.length(), 18));

            signText.append(signLine).append("\n");
        }

        builder.stringTag("Text", signText.toString());
    }
}
