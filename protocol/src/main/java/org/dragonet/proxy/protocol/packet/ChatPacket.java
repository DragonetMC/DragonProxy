/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.protocol.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.proxy.protocol.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class ChatPacket extends PEPacket {

    public enum TextType {

        RAW(0),
        CHAT(1),
        TRANSLATION(2),
        POPUP(3),
        TIP(4),
        SYSTEM(5);

        private int type;

        TextType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }

        public static TextType fromNum(int num) {
            switch (num) {
                case 0:
                    return RAW;
                case 1:
                    return CHAT;
                case 2:
                    return TRANSLATION;
                case 3:
                    return POPUP;
                case 4:
                    return TIP;
                case 5:
                    return SYSTEM;
                default:
                    return RAW;
            }
        }
    }

    public TextType type;
    public String source;
    public String message;
    public String[] params;

    public ChatPacket() {
    }

    public ChatPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.TEXT_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_TEXT);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeByte((byte) (this.type.getType() & 0xFF));
            switch (this.type) {
                case POPUP:
                case CHAT:
                    writer.writeString(this.source);
                case RAW:
                case TIP:
                case SYSTEM:
                    writer.writeString(this.message);
                    break;
                case TRANSLATION:
                    writer.writeString(this.message);
                    if (this.params == null) {
                        writer.writeByte((byte) 0);
                        break;
                    }
                    writer.writeByte((byte) (this.params.length & 0xFF));
                    for (int i = 0; i < this.params.length; i++) {
                        writer.writeString(this.params[i]);
                    }
                    break;
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.type = TextType.fromNum(reader.readByte());
            switch (this.type) {
                case POPUP:
                case CHAT:
                    this.source = reader.readString();
                case RAW:
                case TIP:
                case SYSTEM:
                    this.message = reader.readString();
                    break;
                case TRANSLATION:
                    this.message = reader.readString();
                    int cnt = reader.readByte();
                    this.params = new String[cnt];
                    for (int i = 0; i < cnt; i++) {
                        this.params[i] = reader.readString();
                    }
                    break;
            }
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
