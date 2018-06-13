/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.sound;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;

/**
 *
 * @author Epic
 */
public interface ISoundTranslator {

    public abstract boolean isIgnored(BuiltinSound sound);

    public abstract boolean isTranslatable(BuiltinSound sound);

    public abstract String translate(BuiltinSound sound);
}
