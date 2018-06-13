/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.caches;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import org.dragonet.common.maths.BlockPosition;

/**
 *
 * @author Epic
 */
public interface IJukeboxCache {

    public void registerJukebox(BlockPosition position, BuiltinSound record);

    public BuiltinSound unregisterJukebox(BlockPosition position);
}
