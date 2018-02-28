package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class TextPacket extends PEPacket {

    public static final int TYPE_RAW = 0;
    public static final int TYPE_CHAT = 1;
    public static final int TYPE_TRANSLATION = 2;
    public static final int TYPE_POPUP = 3;
    public static final int TYPE_JUKEBOX_POPUP = 4;
    public static final int TYPE_TIP = 5;
    public static final int TYPE_SYSTEM = 6;
    public static final int TYPE_WHISPER = 7;
    public static final int TYPE_ANNOUNCEMENT = 8;

    public int type;
    public boolean needsTranslation;
    public String source;
//    public String sourceThirdPartyName = "";
//    public int sourcePlatform = 0;
    public String message;
    public String[] params;
    public String xboxUserId;
//    public String unk1;

    public TextPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.TEXT_PACKET;
    }

    @Override
    public void decodePayload() {
        type = getByte();
        needsTranslation = getBoolean();
        switch (type) {
            case TYPE_CHAT:
            case TYPE_WHISPER:
            case TYPE_ANNOUNCEMENT:
                source = getString();
//                sourceThirdPartyName = getString();
//                sourcePlatform = getVarInt();
            case TYPE_RAW:
            case TYPE_TIP:
            case TYPE_SYSTEM:
                message = getString();
                break;

            case TYPE_TRANSLATION:
            case TYPE_POPUP:
            case TYPE_JUKEBOX_POPUP:
                message = getString();
                int count = (int) getUnsignedVarInt();
                params = new String[count];
                for (int i = 0; i < count; i++) {
                    params[i] = getString();
                }
                break;
        }

        xboxUserId = getString();
//        unk1 = getString();
    }

    @Override
    public void encodePayload() {
        putByte((byte) (type & 0xFF));
        putBoolean(needsTranslation);
        switch (type) {
            case TYPE_CHAT:
            case TYPE_WHISPER:
                /**
                 * @noinspection PhpMissingBreakStatementInspection
                 */
            case TYPE_ANNOUNCEMENT:
                putString(source);
//                putString(sourceThirdPartyName);
//                putVarInt(sourcePlatform);
            case TYPE_RAW:
            case TYPE_TIP:
            case TYPE_SYSTEM:
                putString(message);
                break;

            case TYPE_TRANSLATION:
            case TYPE_POPUP:
            case TYPE_JUKEBOX_POPUP:
                putString(message);
                if (params != null && params.length > 0) {
                    putUnsignedVarInt(params.length);
                    for (String s : params) {
                        putString(s);
                    }
                } else {
                    putUnsignedVarInt(0);
                }
                break;
        }

        if (xboxUserId != null) {
            putString(xboxUserId);
        } else {
            putString("");
        }

//        if (unk1 != null) {
//            putString(unk1);
//        } else {
//            putString("");
//        }
    }
}
