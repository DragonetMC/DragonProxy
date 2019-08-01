package org.dragonet.proxy.network.cache;

import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ChunkCache implements Cache {
    @Getter
    private Map chunks = new HashMap<>(); // TODO

    private ProxySession session;

    @Override
    public void purge() {
        chunks.clear();
    }
}
