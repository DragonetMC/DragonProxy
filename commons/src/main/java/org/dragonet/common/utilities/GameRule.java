package org.dragonet.common.utilities;

/**
 * Created on 2017/10/21.
 */
public class GameRule {

    public String name;
    public GameRuleType type;
    public Object value;

    public enum GameRuleType {
        UNKNOWN, BOOLEAN, VARUINT, FLOAT
    }

    public GameRule(String name, GameRuleType type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public static GameRule read(BinaryStream source) {
        String name = source.getString();
        GameRuleType type = GameRuleType.values()[(int) source.getUnsignedVarInt()];
        Object value;
        switch (type) {
            case BOOLEAN:
                value = source.getBoolean();
                break;
            case VARUINT:
                value = source.getUnsignedVarInt();
                break;
            case FLOAT:
                value = source.getLFloat();
                break;
            default:
                value = null;
                break;
        }
        return new GameRule(name, type, value);
    }

    public void write(BinaryStream out) {
        if (type.ordinal() == 0) {
            return; // wtf
        }
        out.putString(name);
        out.putUnsignedVarInt(type.ordinal());
        switch (type) {
            case BOOLEAN:
                out.putBoolean((boolean) value);
                break;
            case VARUINT:
                out.putUnsignedVarInt((int) value);
                break;
            case FLOAT:
                out.putLFloat((Float) value);
                break;
        }
    }
}
