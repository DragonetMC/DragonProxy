package org.dragonet.proxy.network.translator;

import java.util.HashMap;

import org.dragonet.common.data.ParticleEffects;

import com.github.steveice10.mc.protocol.data.game.world.Particle;

public class ParticleTranslator {
	private static ParticleTranslator instance;

	private HashMap<Particle, Integer> pctope = new HashMap<Particle, Integer>();
	
	public static ParticleTranslator getInstance() {
		if(instance == null) {
			instance = new ParticleTranslator();
		}
		return instance;
	}
	
	public ParticleTranslator(){

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
