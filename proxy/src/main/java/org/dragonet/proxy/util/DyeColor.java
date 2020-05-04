package org.dragonet.proxy.util;

import com.github.steveice10.mc.protocol.data.message.ChatColor;
import lombok.Getter;

public enum DyeColor {
    WHITE(0, "white", ChatColor.WHITE),
    ORANGE(1, "orange", ChatColor.GOLD),
    MAGENTA(2, "magenta", ChatColor.LIGHT_PURPLE),
    LIGHT_BLUE(3, "light_blue", ChatColor.BLUE),
    YELLOW(4, "yellow", ChatColor.YELLOW),
    LIME(5, "lime", ChatColor.GREEN),
    PINK(6, "pink", ChatColor.LIGHT_PURPLE),
    GRAY(7, "gray", ChatColor.DARK_GRAY),
    LIGHT_GRAY(8, "light_gray", ChatColor.GRAY),
    CYAN(9, "cyan", ChatColor.DARK_AQUA),
    PURPLE(10, "purple", ChatColor.DARK_PURPLE),
    BLUE(11, "blue", ChatColor.DARK_BLUE),
    BROWN(12, "brown", ChatColor.BLACK),
    GREEN(13, "green", ChatColor.DARK_GREEN),
    RED(14, "red", ChatColor.RED),
    BLACK(15, "black", ChatColor.BLACK);

    @Getter
    private int id;
    private String name;

    @Getter
    private ChatColor chatColor;

    DyeColor(int id, String name, ChatColor chatColor) {
        this.id = id;
        this.name = name;
        this.chatColor = chatColor;
    }
}
