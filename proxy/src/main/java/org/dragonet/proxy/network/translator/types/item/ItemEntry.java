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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.types.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class ItemEntry {
    @Getter
    public static class BedrockItem {
        @JsonProperty("name")
        private String identifier;

        @JsonProperty("id")
        private int runtimeId;

        private int data;

        public BedrockItem() {}
        public BedrockItem(String identifier, int runtimeId) {
            this(identifier, runtimeId, 0);
        }
        public BedrockItem(String identifier, int runtimeId, int data) {
            this.identifier = identifier;
            this.runtimeId = runtimeId;
            this.data = data;
        }
    }

    @Getter
    public static class JavaItem {
        @JsonProperty
        private String identifier;
        @JsonProperty("protocol_id")
        private int runtimeId;

        public JavaItem(String identifier, int runtimeId) {
            this.identifier = identifier;
            this.runtimeId = runtimeId;
        }
    }

    @Getter
    public static class ItemMap {
        @JsonProperty("java_identifier")
        private String javaIdentifier;
        @JsonProperty("java_protocol_id")
        private int javaProtocolId;

        @JsonProperty("bedrock_identifier")
        private String bedrockIdentifier;
        @JsonProperty("bedrock_runtime_id")
        private int bedrockRuntimeId;
        @JsonProperty("bedrock_data")
        private int bedrockData;
    }
}
