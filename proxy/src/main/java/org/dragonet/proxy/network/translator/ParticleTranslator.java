package org.dragonet.proxy.network.translator;

import java.util.HashMap;

import org.dragonet.common.data.ParticleEffects;

import com.github.steveice10.mc.protocol.data.game.world.Particle;

public class ParticleTranslator {
    private static ParticleTranslator instance;

    private HashMap<Particle, Integer> pctope = new HashMap<Particle, Integer>();

    public static ParticleTranslator getInstance() {
        if (instance == null) {
            instance = new ParticleTranslator();
        }
        return instance;
    }

    public ParticleTranslator() {
        register(Particle.EXPLOSION_NORMAL, ParticleEffects.TYPE_EXPLODE);
        // register(Particle.EXPLOSION_LARGE,);
        register(Particle.EXPLOSION_HUGE, ParticleEffects.TYPE_HUGE_EXPLODE);
        // register(Particle.FIREWORKS_SPARK,);
        register(Particle.WATER_BUBBLE, ParticleEffects.TYPE_BUBBLE);
        register(Particle.WATER_SPLASH, ParticleEffects.TYPE_SPLASH);
        register(Particle.WATER_WAKE, ParticleEffects.TYPE_WATER_WAKE);
        // register(Particle.SUSPENDED, ); // It's underwater but in MCBE?
        // register(Particle.SUSPENDED_DEPTH, ); // Removed in upcoming and not in use
        register(Particle.CRIT, ParticleEffects.TYPE_CRITICAL);
        register(Particle.CRIT_MAGIC, ParticleEffects.TYPE_CRITICAL); // do same as crit (magic crit maybe isn't in
                                                                      // MCBE)
        register(Particle.SMOKE_NORMAL, ParticleEffects.TYPE_SMOKE);
        register(Particle.SMOKE_LARGE, ParticleEffects.TYPE_LARGE_SMOKE);
        // register(Particle.SPELL, );
        // register(Particle.SPELL_INSTANT, );
        register(Particle.SPELL_MOB, ParticleEffects.TYPE_MOB_SPELL);
        register(Particle.SPELL_MOB_AMBIENT, ParticleEffects.TYPE_MOB_SPELL_AMBIENT);
        register(Particle.SPELL_WITCH, ParticleEffects.TYPE_WITCH_SPELL);
        register(Particle.DRIP_WATER, ParticleEffects.TYPE_DRIP_WATER);
        register(Particle.DRIP_LAVA, ParticleEffects.TYPE_DRIP_LAVA);
        register(Particle.VILLAGER_ANGRY, ParticleEffects.TYPE_VILLAGER_ANGRY);
        register(Particle.VILLAGER_HAPPY, ParticleEffects.TYPE_VILLAGER_HAPPY);
        register(Particle.TOWN_AURA, ParticleEffects.TYPE_SUSPENDED_TOWN);
        register(Particle.NOTE, ParticleEffects.TYPE_NOTE);
        register(Particle.PORTAL, ParticleEffects.TYPE_PORTAL);
        register(Particle.ENCHANTMENT_TABLE, ParticleEffects.TYPE_ENCHANTMENT_TABLE);
        register(Particle.FLAME, ParticleEffects.TYPE_FLAME);
        register(Particle.LAVA, ParticleEffects.TYPE_LAVA);
        // register(Particle.FOOTSTEP,); // Removed in upcoming and not in use
        register(Particle.CLOUD, ParticleEffects.TYPE_EVAPORATION);
        register(Particle.REDSTONE, ParticleEffects.TYPE_REDSTONE);
        register(Particle.SNOWBALL, ParticleEffects.TYPE_SNOWBALL_POOF);
        register(Particle.SNOW_SHOVEL, ParticleEffects.TYPE_EXPLODE);
        register(Particle.SLIME, ParticleEffects.TYPE_SLIME);
        register(Particle.HEART, ParticleEffects.TYPE_HEART);
        //register(Particle.BARRIER,);  // No barrier
        register(Particle.ITEM_CRACK, ParticleEffects.TYPE_ITEM_BREAK);
        //register(Particle.BLOCK_CRACK,); // Not in this file, look into PCSpawnParticlePacketTranslator
        //register(Particle.BLOCK_DUST,); // Not in this file, look into PCSpawnParticlePacketTranslator
        register(Particle.WATER_DROP, ParticleEffects.TYPE_RAIN_SPLASH); // Maybe
        //register(Particle.ITEM_TAKE,); // This has not any use
        //register(Particle.MOB_APPEARANCE,); // I not find anything
        register(Particle.DRAGON_BREATH, ParticleEffects.TYPE_DRAGONS_BREATH);
        register(Particle.END_ROD, ParticleEffects.TYPE_END_ROD);
        //register(Particle.DAMAGE_INDICATOR,); // Not in MCBE but maybe exists equivalent in another packet
        //register(Particle.SWEEP_ATTACK,); // Not in MCBE but maybe exists equivalent in another packet
        register(Particle.FALLING_DUST, ParticleEffects.TYPE_FALLING_DUST);
        //register(Particle.SPIT,); // Not in MCBE but maybe exists equivalent in another packet
        //register(Particle.TOTEM,);  // Not in MCBE but maybe exists equivalent in another packet
        
    }

    public int translate(Particle particle) {
        if (!pctope.containsKey(particle)) {
            return -1;
        }
        return pctope.get(particle);
    }

    private void register(Particle pc, ParticleEffects pe) {
        pctope.put(pc, pe.id);
    }
}
