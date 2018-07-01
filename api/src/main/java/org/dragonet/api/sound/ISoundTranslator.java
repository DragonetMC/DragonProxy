package org.dragonet.api.sound;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;

/**
 * The sound translator interface.
 */
public interface ISoundTranslator {

    boolean isIgnored(BuiltinSound sound);

    boolean isTranslatable(BuiltinSound sound);

    String translate(BuiltinSound sound);
}
