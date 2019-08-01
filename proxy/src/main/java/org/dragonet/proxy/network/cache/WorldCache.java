package org.dragonet.proxy.network.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WorldCache implements Cache {
    private double rainLevel = 0.0;

    @Override
    public void purge() {

    }
}
