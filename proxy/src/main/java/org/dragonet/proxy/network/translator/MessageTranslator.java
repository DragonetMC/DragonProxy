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

import java.util.List;

public final class MessageTranslator {
	// vars

	// constructor
	public MessageTranslator() {

	}

	// public
	public static String translate(Message message) {
		StringBuilder build = new StringBuilder(message.getText());
		for (Message msg : message.getExtra()) {
			build.append(toMinecraftColor(msg.getStyle().getColor()));
			build.append(toMinecraftFormat(msg.getStyle().getFormats()));
			build.append(msg.getFullText());
		}
		return build.toString();
	}

	public static String toMinecraftColor(ChatColor color) {
		String base = "§";
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

	// private
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
