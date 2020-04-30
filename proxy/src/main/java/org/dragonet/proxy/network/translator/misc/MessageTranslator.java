/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.misc;

import com.github.steveice10.mc.protocol.data.message.*;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.configuration.lang.MinecraftLanguage;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class MessageTranslator {
    // Java to Bedrock chat color map
    private static final Map<ChatColor, String> colorMap = new HashMap<>();
    // Java to Bedrock chat format map
    private static final Map<ChatFormat, String> formatMap = new HashMap<>();

    static {
        // Colours
        colorMap.put(ChatColor.AQUA, "b");
        colorMap.put(ChatColor.BLACK, "0");
        colorMap.put(ChatColor.BLUE, "9");
        colorMap.put(ChatColor.DARK_AQUA, "3");
        colorMap.put(ChatColor.DARK_BLUE, "1");
        colorMap.put(ChatColor.DARK_GRAY, "8");
        colorMap.put(ChatColor.DARK_GREEN, "2");
        colorMap.put(ChatColor.DARK_PURPLE, "5");
        colorMap.put(ChatColor.DARK_RED, "4");
        colorMap.put(ChatColor.GOLD, "6");
        colorMap.put(ChatColor.GRAY, "7");
        colorMap.put(ChatColor.GREEN, "a");
        colorMap.put(ChatColor.LIGHT_PURPLE, "d");
        colorMap.put(ChatColor.RED, "c");
        colorMap.put(ChatColor.WHITE, "f");
        colorMap.put(ChatColor.YELLOW, "e");
        colorMap.put(ChatColor.RESET, "r");
        colorMap.put(ChatColor.NONE, "r");

        // Formats
        formatMap.put(ChatFormat.BOLD, "l");
        formatMap.put(ChatFormat.ITALIC, "o");
        formatMap.put(ChatFormat.OBFUSCATED, "k");
        formatMap.put(ChatFormat.STRIKETHROUGH, "m");
        formatMap.put(ChatFormat.UNDERLINED, "n");

    }

    public static String translate(Message message) {
        if(message instanceof TranslationMessage) {
            return translate((TranslationMessage) message);
        }
        StringBuilder builder = new StringBuilder();

        builder.append(toBedrockFormat(message.getStyle().getFormats()));
        builder.append(toBedrockColor(message.getStyle().getColor()));
        builder.append(message.getText());
        builder.append(toBedrockColor(ChatColor.NONE));

        for (Message msg : message.getExtra()) {
            if(msg instanceof TranslationMessage) {
                builder.append(MinecraftLanguage.translate(((TranslationMessage) msg).getTranslationKey()));
            }
            else if (!(msg.getText() == null)) {
                builder.append(translate(msg));
            }
        }
        //log.warn(clean(builder.toString()));
        return builder.toString();
    }

    public static String translateExtra(Message message) {
        StringBuilder builder = new StringBuilder(message.getText());
        for (Message msg : message.getExtra()) {
            if(msg instanceof TranslationMessage) {
                builder.append(MinecraftLanguage.translate(((TranslationMessage) msg).getTranslationKey()));
            }
            else if (!(msg.getText() == null)) {
                builder.append(translateExtra(msg));
            }
        }
        return builder.toString();
    }

    public static String translate(String message) {
        return translate(Message.fromString(message));
    }

    public static String translate(TranslationMessage message) {
        StringBuilder builder = new StringBuilder();
        builder.append(toBedrockFormat(message.getStyle().getFormats()));
        builder.append(toBedrockColor(message.getStyle().getColor()));

        builder.append(MinecraftLanguage.translate(message.getTranslationKey(), translateParams(message.getTranslationParams()).toArray()));
        return builder.toString();
    }

    public static List<String> translateParams(Message[] messages) {
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < messages.length; i++) {
            if (messages[i] instanceof TranslationMessage) {
                TranslationMessage tmsg = (TranslationMessage) messages[i];
//                strings.add(toBedrockFormat(org.dragonet.proxy.hybrid.messages[i].getStyle().getFormats()));
//                strings.add(toBedrockColor(org.dragonet.proxy.hybrid.messages[i].getStyle().getColor()));

                strings.add(MinecraftLanguage.translate(tmsg.getTranslationKey()));

                for (int j = 0; j < translateParams(tmsg.getTranslationParams()).size(); j++) {
                    strings.add(MinecraftLanguage.translate(translateParams(tmsg.getTranslationParams()).get(j)));
                }
            } else {
                Message message = messages[i];
                StringBuilder builder = new StringBuilder();

                builder.append(toBedrockFormat(messages[i].getStyle().getFormats()));
                builder.append(toBedrockColor(messages[i].getStyle().getColor()));
                builder.append(translateExtra(message));
                builder.append(toBedrockColor(ChatColor.NONE));

                strings.add(builder.toString());
            }
        }
        return strings;
    }

    public static String toBedrockColor(ChatColor color) {
        String base = "\u00a7";
        if(!colorMap.containsKey(color)) {
            log.warn("Unmapped chat colour: " + color.name());
            return base;
        }
        return base + colorMap.get(color);
    }

    public static String toBedrockFormat(List<ChatFormat> formats) {
        StringBuilder superBase = new StringBuilder();
        for(ChatFormat format : formats) {
            if(!formatMap.containsKey(format)) {
                log.warn("Unmapped chat format: " + format.name());
                continue;
            }
            superBase.append("\u00a7").append(formatMap.get(format));
        }
        return superBase.toString();
    }
}
