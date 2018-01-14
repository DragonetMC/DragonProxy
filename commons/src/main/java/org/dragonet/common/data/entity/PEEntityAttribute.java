package org.dragonet.common.data.entity;

import java.util.HashMap;

/**
 * Created on 2017/10/21.
 */
public class PEEntityAttribute {

    public static final int ABSORPTION = 0;
    public static final int SATURATION = 1;
    public static final int EXHAUSTION = 2;
    public static final int KNOCKBACK_RESISTANCE = 3;
    public static final int HEALTH = 4;
    public static final int MOVEMENT_SPEED = 5;
    public static final int FOLLOW_RANGE = 6;
    public static final int HUNGER = 7;
    public static final int FOOD = 7;
    public static final int ATTACK_DAMAGE = 8;
    public static final int EXPERIENCE_LEVEL = 9;
    public static final int EXPERIENCE = 10;

    private static final HashMap<Integer, PEEntityAttribute> attributes = new HashMap<>();
    private static final HashMap<String, PEEntityAttribute> nameMap = new HashMap<>();

    static {
        addAttribute(ABSORPTION, "minecraft:absorption", 0.00f, Float.MAX_VALUE, 0.00f);
        addAttribute(SATURATION, "minecraft:player.saturation", 0.00f, 20.00f, 20.00f);
        addAttribute(EXHAUSTION, "minecraft:player.exhaustion", 0.00f, 5.00f, 0.00f);
        addAttribute(KNOCKBACK_RESISTANCE, "minecraft:knockback_resistance", 0.00f, 1.00f, 0.00f);
        addAttribute(HEALTH, "minecraft:health", 0.00f, 20.00f, 20.00f);
        addAttribute(MOVEMENT_SPEED, "minecraft:movement", 0.00f, Float.MAX_VALUE, 0.10f);
        addAttribute(FOLLOW_RANGE, "minecraft:follow_range", 0.00f, 2048.00f, 16.00f);
        addAttribute(HUNGER, "minecraft:player.hunger", 0.00f, 20.00f, 20.00f);
        addAttribute(ATTACK_DAMAGE, "minecraft:attack_damage", 0.00f, Float.MAX_VALUE, 1.00f);
        addAttribute(EXPERIENCE_LEVEL, "minecraft:player.level", 0.00f, 24791.00f, 0.00f);
        addAttribute(EXPERIENCE, "minecraft:player.experience", 0.00f, 1.00f, 0.00f);
    }

    public int id;
    public String name;
    public float min;
    public float max;
    public float defaultValue;
    public float currentValue;

    public PEEntityAttribute() {

    }

    // public
    public static final void addAttribute(int id, String name, float min, float max, float defaultValue) {
        PEEntityAttribute attr = new PEEntityAttribute(id, name, min, max, defaultValue);
        attributes.put(id, attr);
        nameMap.put(name, attr);
    }

    public static final PEEntityAttribute findAttribute(int id) {
        if (!attributes.containsKey(id)) {
            return null;
        }
        return attributes.get(id).clone();
    }

    public static final PEEntityAttribute findAttribute(String name) {
        if (!nameMap.containsKey(name)) {
            return null;
        }
        return nameMap.get(name).clone();
    }

    public PEEntityAttribute(int id, String name, float min, float max, float defaultValue) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;

        this.currentValue = defaultValue;
    }

    public PEEntityAttribute(int id, String name, float min, float max, float defaultValue, float currentValue) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.currentValue = currentValue;
    }

    public PEEntityAttribute setValue(float currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public PEEntityAttribute clone() {
        return new PEEntityAttribute(id, name, min, max, defaultValue, currentValue);
    }
}
