package org.dragonet.proxy.network.cache;

import java.util.HashMap;

import org.dragonet.common.maths.BlockPosition;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import org.dragonet.api.caches.IJukeboxCache;

public class JukeboxCache implements IJukeboxCache {

    private HashMap<BlockPosition, BuiltinSound> jukebox = new HashMap<BlockPosition, BuiltinSound>();

    public JukeboxCache() {
    }

    @Override
    public void registerJukebox(BlockPosition position, BuiltinSound record) {
        jukebox.put(position, record);
    }

    @Override
    public BuiltinSound unregisterJukebox(BlockPosition position) {
        if (jukebox.containsKey(position)) {
            BuiltinSound sound = jukebox.get(position);
            jukebox.remove(position);
            return sound;
        }
        return null;
    }
}
