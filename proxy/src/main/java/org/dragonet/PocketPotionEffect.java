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
package org.dragonet;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public class PocketPotionEffect {

    public final static int SPEED = 1;
    public final static int SLOWNESS = 2;
    public final static int HASTE = 3;
    public final static int SWIFTNESS = 3;
    public final static int FATIGUE = 4;
    public final static int MINING_FATIGUE = 4;
    public final static int STRENGTH = 5;
//TODO: public final static int HEALING = 6;
//TODO: public final static int HARMING = 7;
    public final static int JUMP = 8;
    public final static int NAUSEA = 9;
    public final static int CONFUSION = 9;
    public final static int REGENERATION = 10;
    public final static int DAMAGE_RESISTANCE = 11;
    public final static int FIRE_RESISTANCE = 12;
    public final static int WATER_BREATHING = 13;
    public final static int INVISIBILITY = 14;
//public final static int BLINDNESS = 15;
//public final static int NIGHT_VISION = 16;
//public final static int HUNGER = 17;
    public final static int WEAKNESS = 18;
    public final static int POISON = 19;
    public final static int WITHER = 20;
    public final static int HEALTH_BOOST = 21;
//public final static int  ABSORPTION = 22;
//public final static int SATURATION = 23;

    private final static HashMap<Integer, PocketPotionEffect> EFFECTS = new HashMap<>();

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

    public static PocketPotionEffect getByID(int id) {
        if (EFFECTS.containsKey(id)) {
            return EFFECTS.get((Integer) id).clone();
        } else {
            return null;
        }
    }

    @Getter
    private final int effect;

    @Getter
    @Setter
    private int ampilifier;

    @Getter
    @Setter
    private int duration;

    @Getter
    @Setter
    private boolean particles;

    public PocketPotionEffect(int effect) {
        this.effect = effect;
    }

    @Override
    protected PocketPotionEffect clone() {
        PocketPotionEffect eff = new PocketPotionEffect(effect);
        eff.setAmpilifier(ampilifier);
        eff.setParticles(particles);
        eff.setDuration(duration);
        return eff;
    }

}
