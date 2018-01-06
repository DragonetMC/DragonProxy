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
package org.dragonet.common.mcbedrock.data;

import java.util.HashMap;

public class PocketPotionEffect {
    // vars

    public static final int SPEED = 1;
    public static final int SLOWNESS = 2;
    public static final int HASTE = 3;
    public static final int SWIFTNESS = 3;
    public static final int FATIGUE = 4;
    public static final int MINING_FATIGUE = 4;
    public static final int STRENGTH = 5;
    // TODO: public static final int HEALING = 6;
    // TODO: public static final int HARMING = 7;
    public static final int JUMP = 8;
    public static final int NAUSEA = 9;
    public static final int CONFUSION = 9;
    public static final int REGENERATION = 10;
    public static final int DAMAGE_RESISTANCE = 11;
    public static final int FIRE_RESISTANCE = 12;
    public static final int WATER_BREATHING = 13;
    public static final int INVISIBILITY = 14;
    // public static final int BLINDNESS = 15;
    // public static final int NIGHT_VISION = 16;
    // public static final int HUNGER = 17;
    public static final int WEAKNESS = 18;
    public static final int POISON = 19;
    public static final int WITHER = 20;
    public static final int HEALTH_BOOST = 21;
    // public static final int ABSORPTION = 22;
    // public static final int SATURATION = 23;

    private static final HashMap<Integer, PocketPotionEffect> EFFECTS = new HashMap<>();

    static {
        EFFECTS.put(SPEED, new PocketPotionEffect(SPEED));
        EFFECTS.put(SLOWNESS, new PocketPotionEffect(SLOWNESS));
        EFFECTS.put(HASTE, new PocketPotionEffect(HASTE));
        EFFECTS.put(SWIFTNESS, new PocketPotionEffect(SWIFTNESS));
        EFFECTS.put(FATIGUE, new PocketPotionEffect(FATIGUE));
        EFFECTS.put(MINING_FATIGUE, new PocketPotionEffect(MINING_FATIGUE));
        EFFECTS.put(STRENGTH, new PocketPotionEffect(STRENGTH));
        EFFECTS.put(JUMP, new PocketPotionEffect(JUMP));
        EFFECTS.put(NAUSEA, new PocketPotionEffect(NAUSEA));
        EFFECTS.put(CONFUSION, new PocketPotionEffect(CONFUSION));
        EFFECTS.put(REGENERATION, new PocketPotionEffect(REGENERATION));
        EFFECTS.put(DAMAGE_RESISTANCE, new PocketPotionEffect(DAMAGE_RESISTANCE));
        EFFECTS.put(FIRE_RESISTANCE, new PocketPotionEffect(FIRE_RESISTANCE));
        EFFECTS.put(WATER_BREATHING, new PocketPotionEffect(WATER_BREATHING));
        EFFECTS.put(INVISIBILITY, new PocketPotionEffect(INVISIBILITY));
        EFFECTS.put(WEAKNESS, new PocketPotionEffect(WEAKNESS));
        EFFECTS.put(POISON, new PocketPotionEffect(POISON));
        EFFECTS.put(WITHER, new PocketPotionEffect(WITHER));
        EFFECTS.put(HEALTH_BOOST, new PocketPotionEffect(HEALTH_BOOST));
    }

    public int amplifier;
    public int duration;
    public boolean particles;
    private final int effect;

    // constructor
    public PocketPotionEffect(int effect) {
        this.effect = effect;
    }

    // public
    public static PocketPotionEffect getByID(int id) {
        if (EFFECTS.containsKey(id)) {
            return EFFECTS.get((Integer) id).clone();
        } else {
            return null;
        }
    }

    public int getEffect() {
        return effect;
    }

    // private
    protected PocketPotionEffect clone() {
        PocketPotionEffect eff = new PocketPotionEffect(effect);
        eff.amplifier = amplifier;
        eff.particles = particles;
        eff.duration = duration;
        return eff;
    }
}
