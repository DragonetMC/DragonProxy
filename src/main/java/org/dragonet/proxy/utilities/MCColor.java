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
package org.dragonet.proxy.utilities;

public enum MCColor {
    BLACK("0"),
    DARK_BLUE("1"),
    DARK_GREEN("2"),
    DARK_AQUA("3"),
    DARK_RED("4"),
    DARK_PURPLE("5"),
    GOLD("6"),
    GRAY("7"),
    DARK_GRAY("8"),
    BLUE("9"),
    GREEN("a"),
    AQUA("b"),
    RED("c"),
    LIGHT_PURPLE("d"),
    YELLOW("e"),
    WHITE("f"),
    OBFUSCATED("k"),
    BOLD("l"),
    STRIKETHROUGH("m"),
    UNDERLINE("n"),
    ITALIC("o"),
    RESET("r");

    public final static String COLOR_PREFIX_PC = new String(new byte[]{(byte) 0xc2, (byte) 0xa7});
    public final static String COLOR_PREFIX_PE = "§"; //§

    private String suffix;

    private MCColor(String suffix) {
        this.suffix = suffix;
    }

    public String getPECode() {
        return COLOR_PREFIX_PE + suffix;
    }

    public String getPCCode() {
        return COLOR_PREFIX_PC + suffix;
    }

    public static String cleanAll(String text) {
        String ret = new String(text);
        for (MCColor c : MCColor.values()) {
            ret = ret.replace(c.getPECode(), "");
            ret = ret.replace(c.getPCCode(), "");
        }
        return ret;
    }
}