package org.dragonet.proxy.network.translator.types.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class ItemEntry {
    @Getter
    public static class BedrockItem {
        @JsonProperty("name")
        private String id;

        @JsonProperty("id")
        private int runtimeId;

        public BedrockItem() {}
        public BedrockItem(String id, int runtimeId) {
            this.id = id;
            this.runtimeId = runtimeId;
        }
    }

    @Getter
    public static class JavaItem {
        @JsonProperty("identifier")
        private String id;
        @JsonProperty("protocol_id")
        private int runtimeId; // or legacy id?
    }
}
