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
package org.dragonet.proxy.network.translator.types.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class ItemEntry {
    @Getter
    public static class BedrockItem {
        @JsonProperty("name")
        private String identifier;

        @JsonProperty
        private int id;

        public BedrockItem() {}
        public BedrockItem(String identifier, int id) {
            this.identifier = identifier;
            this.id = id;
        }
    }

    @Getter
    public static class JavaItem {
        @JsonProperty
        private String identifier;
        @JsonProperty("protocol_id")
        private int protocolId;
    }
}
