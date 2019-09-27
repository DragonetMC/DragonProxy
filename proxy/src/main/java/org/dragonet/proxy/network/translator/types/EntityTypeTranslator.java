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
package org.dragonet.proxy.network.translator.types;

import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class EntityTypeTranslator {
    // Java to Bedrock entity type map
    private static Map<MobType, EntityType> entityMap = new HashMap<>();

    static {
        // TODO: finish these
        entityMap.put(MobType.PLAYER, EntityType.PLAYER);
        entityMap.put(MobType.SHEEP, EntityType.SHEEP);
        entityMap.put(MobType.BAT, EntityType.BAT);
        entityMap.put(MobType.ZOMBIE, EntityType.ZOMBIE);
        entityMap.put(MobType.ENDERMITE, EntityType.ENDERMITE);
        entityMap.put(MobType.CREEPER, EntityType.CREEPER);
        entityMap.put(MobType.VILLAGER, EntityType.VILLAGER);

        entityMap.put(MobType.ARMOR_STAND, EntityType.ARMOR_STAND);
    }

    /**
     * This method translates a Java mob type to a Bedrock entity type.
     */
    public static EntityType translateToBedrock(MobType mobType) {
        if(entityMap.containsKey(mobType)) {
            return entityMap.get(mobType);
        }
        return null;
    }
}
