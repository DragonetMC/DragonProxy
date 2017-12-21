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
package org.dragonet.proxy.network.translator;

import com.github.steveice10.mc.protocol.data.message.ChatColor;
import com.github.steveice10.mc.protocol.data.message.ChatFormat;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

public final class MessageTranslator {

    public static String translate(Message message) {
        JsonParser parser = new JsonParser();
        if(isMessage(message.getText())){
            JsonObject o = parser.parse(message.getText()).getAsJsonObject();
            editJson(o);
            message = Message.fromJson((JsonElement) o);
        }
        StringBuilder build = new StringBuilder(message.getText());
        for (Message msg : message.getExtra()) {
            build.append(toMinecraftColor(msg.getStyle().getColor()));
            build.append(toMinecraftFormat(msg.getStyle().getFormats()));
            if(!(msg.getText()==null)){
                build.append(translate(msg));
            }
        }
        return build.toString();

    }
    
    public static boolean isMessage(String text) {
        JsonParser parser = new JsonParser();
        try{
                JsonObject o = parser.parse(text).getAsJsonObject();
                editJson(o);
                try{
                        Message.fromJson((JsonElement) o);
                }catch(Exception e){
                        return false;
                }
        }catch(Exception e){
                return false;
        }
        return true;
    }
    
    public static void editJson(JsonObject o) {
        if(o.has("hoverEvent")) {
            JsonObject sub = (JsonObject) o.get("hoverEvent");
            JsonElement e = sub.get("value");
            if(e instanceof JsonArray){
                JsonObject newobj = new JsonObject();
                newobj.add("extra", e);
                newobj.addProperty("text", "");
                sub.remove("value");
                sub.add("value", newobj);
            }
        }
        if(o.has("extra")) {
            JsonArray a = o.getAsJsonArray("extra");
            for(int i = 0; i<a.size();i++) {
                editJson((JsonObject)a.get(i));
            }
        }
    }
    

    public static String toMinecraftColor(ChatColor color) {
        String base = "\u00a7";
        switch (color) {
            case AQUA:
                base += "b";
                break;
            case BLACK:
                base += "0";
                break;
            case BLUE:
                base += "9";
                break;
            case DARK_AQUA:
                base += "3";
                break;
            case DARK_BLUE:
                base += "1";
                break;
            case DARK_GRAY:
                base += "8";
                break;
            case DARK_GREEN:
                base += "2";
                break;
            case DARK_PURPLE:
                base += "5";
                break;
            case DARK_RED:
                base += "4";
                break;
            case GOLD:
                base += "6";
                break;
            case GRAY:
                base += "7";
                break;
            case GREEN:
                base += "a";
                break;
            case LIGHT_PURPLE:
                base += "d";
                break;
            case RED:
                base += "c";
                break;
            case RESET:
                base += "r";
                break;
            case WHITE:
                base += "f";
                break;
            case YELLOW:
                base += "e";
                break;
            default:
                break;
        }
        return base;
    }

    private static String toMinecraftFormat(List<ChatFormat> formats) {
        String superBase = "";
        for (ChatFormat cf : formats) {
            String base = "\u00a7";
            switch (cf) {
                case BOLD:
                    base += "l";
                    break;
                case ITALIC:
                    base += "o";
                    break;
                case OBFUSCATED:
                    base += "k";
                    break;
                case STRIKETHROUGH:
                    base += "m";
                    break;
                case UNDERLINED:
                    base += "n";
                    break;
                default:
                    break;
            }
            superBase += base;
        }
        return superBase;
    }
}
