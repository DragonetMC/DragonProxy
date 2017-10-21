package org.dragonet.proxy.entity;

import java.util.HashMap;

/**
 * Created on 2017/10/21.
 */
public class PEEntityAttribute {

    public final static int ABSORPTION = 0;
    public final static int SATURATION = 1;
    public final static int EXHAUSTION = 2;
    public final static int KNOCKBACK_RESISTANCE = 3;
    public final static int HEALTH = 4;
    public final static int MOVEMENT_SPEED = 5;
    public final static int FOLLOW_RANGE = 6;
    public final static int HUNGER = 7;
    public final static int FOOD = 7;
    public final static int ATTACK_DAMAGE = 8;
    public final static int EXPERIENCE_LEVEL = 9;
    public final static int EXPERIENCE = 10;

    private final static HashMap<Integer, PEEntityAttribute> attributes = new HashMap<>();
    private final static HashMap<String, PEEntityAttribute> nameMap = new HashMap<>();

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

    public final static void addAttribute(int id, String name, float min, float max, float defaultValue) {
        PEEntityAttribute attr = new PEEntityAttribute(id, name, min, max, defaultValue);
        attributes.put(id, attr);
        nameMap.put(name, attr);
    }

    public final static PEEntityAttribute findAttribute(int id) {
        if(!attributes.containsKey(id)) return null;
        return attributes.get(id).clone();
    }

    public final static PEEntityAttribute findAttribute(String name) {
        if(!nameMap.containsKey(name)) return null;
        return nameMap.get(name).clone();
    }

    public int id;
    public String name;
    public float min;
    public float max;
    public float defaultValue;
    public float currentValue;

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

    @Override
    public PEEntityAttribute clone() {
        return new PEEntityAttribute(id, name, min, max, defaultValue, currentValue);
    }
}
