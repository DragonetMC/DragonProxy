package org.dragonet.proxy.network.session.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.stats.StatInfo;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class WorldCache implements Cache {
    private Map<StatInfo, Integer> statistics = new HashMap();
    private double rainLevel = 0.0;

    @Override
    public void purge() {

    }
}
