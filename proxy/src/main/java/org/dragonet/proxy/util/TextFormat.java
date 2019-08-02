package org.dragonet.proxy.util;

public enum TextFormat {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    OBFUSCATED('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINE('n'),
    ITALIC('o'),
    RESET('r');

    public static final char ESCAPE = '\u00a7';

    private final char id;
    private final String precompiledToString;

    TextFormat(char id) {
        this.id = id;
        this.precompiledToString = new String(new char[] { ESCAPE, id });
    }

    public char getId() {
        return id;
    }

    @Override
    public String toString() {
        return precompiledToString;
    }
}
