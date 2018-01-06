package org.dragonet.common.mcbedrock.protocol.packets;

import com.github.steveice10.mc.protocol.data.game.TitleAction;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;

public class SetTitlePacket extends PEPacket {

    public TitleAction action;
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
        putVarInt(this.toActionID(this.action));
        putString(this.text);
        putVarInt(this.fadeIn);
        putVarInt(this.stay);
        putVarInt(this.fadeOut);
    }

    @Override
    public void decodePayload() {
        this.action = this.fromActionID(getVarInt());
        this.text = getString();
        this.fadeIn = getVarInt();
        this.stay = getVarInt();
        this.fadeOut = getVarInt();
    }

    private int toActionID(TitleAction action) {
        switch (action) {
            case TITLE:
                return 2;
            case SUBTITLE:
                return 3;
            case ACTION_BAR:
                return 4;
            case TIMES:
                return 5;
            case CLEAR:
                return 0;
            case RESET:
                return 1;
            default:
                throw new UnsupportedOperationException("No action ID implemented for TitleAction " + action.name());
        }
    }

    private TitleAction fromActionID(int actionID) {
        switch (actionID) {
            case 2:
                return TitleAction.TITLE;
            case 3:
                return TitleAction.SUBTITLE;
            case 4:
                return TitleAction.ACTION_BAR;
            case 5:
                return TitleAction.TIMES;
            case 0:
                return TitleAction.CLEAR;
            case 1:
                return TitleAction.RESET;
            default:
                throw new UnsupportedOperationException("No TitleAction implemented for action ID " + actionID);
        }
    }

}
