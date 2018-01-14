package org.dragonet.protocol.packets;

import com.github.steveice10.mc.protocol.data.game.TitleAction;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

public class SetTitlePacket extends PEPacket {

    public static int HIDE = 0;
    public static int RESET = 1;
    public static int SET_TITLE = 2;
    public static int SET_SUBTITLE = 3;
    public static int SET_ACTIONBAR = 4;
    public static int SET_TIMINGS = 5;
    
    
    public int action;
    public String text;
    public int fadeIn;
    public int stay;
    public int fadeOut;

    @Override
    public int pid() {
        return ProtocolInfo.SET_TITLE_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarInt(this.action);
        putString(this.text);
        putVarInt(this.fadeIn);
        putVarInt(this.stay);
        putVarInt(this.fadeOut);
    }

    @Override
    public void decodePayload() {
        this.action = getVarInt();
        this.text = getString();
        this.fadeIn = getVarInt();
        this.stay = getVarInt();
        this.fadeOut = getVarInt();
    }
}
