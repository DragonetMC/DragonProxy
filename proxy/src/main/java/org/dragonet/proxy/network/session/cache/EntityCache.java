/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class EntityCache implements Cache {
    @Getter
    private Map<Long, CachedEntity> entities = new HashMap<>();

    private int fakePlayersIds;

    public CachedEntity getById(long entityId) {
        // TODO: convert to proxy entity id first?
        return entities.get(entityId);
    }

    public int nextFakePlayerid() {
        return fakePlayersIds++;
    }

    @Override
    public void purge() {
        entities.clear();
    }
}
