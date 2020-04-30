package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.mc.protocol.data.message.ChatColor;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageStyle;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;

@BlockEntityRegisterInfo(bedrockId = "Sign")

public class SignBlockEntityTranslator implements IBlockEntityTranslator {

    @Override
    public void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag) {
        StringBuilder signText = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            int currentLine = i+1;

            //Signs have different color names than chat color ugh
            String color = ChatColor.BLACK.toString();
            if(javaTag.get("color") != null) {
                color = javaTag.get("Color").getValue().toString()
                    .replaceAll("\\bblue\\b", "dark_blue")
                    .replaceAll("\\bgray\\b", "dark_gray")
                    .replaceAll("\\blight_blue\\b", "blue")
                    .replaceAll("\\blight_gray\\b", "gray");
            }

            //Lambda requiring stupid stuff
            String finalColor = color;

            Message message = Message.fromString(javaTag.get("Text" + currentLine).getValue().toString());
            message.getExtra().forEach(messageExtra -> {
                messageExtra.setStyle(new MessageStyle().setColor(ChatColor.byName(finalColor)));
            });
            signText.append(MessageTranslator.translate(message)).append("\n");
        }
        builder.stringTag("Text", signText.toString());
    }
}
