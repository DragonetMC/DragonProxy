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

        public BedrockItem() {}
        public BedrockItem(String identifier, int runtimeId) {
            this.identifier = identifier;
            this.runtimeId = runtimeId;
        }
    }

    @Getter
    public static class JavaItem {
        @JsonProperty
        private String identifier;
        @JsonProperty("protocol_id")
        private int runtimeId;
    }
}
