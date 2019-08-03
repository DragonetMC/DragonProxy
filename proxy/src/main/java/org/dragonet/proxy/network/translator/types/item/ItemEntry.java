package org.dragonet.proxy.network.translator.types.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ItemEntry {
    @Getter
    @AllArgsConstructor
    public static class BedrockItem {
        private int runtimeId; // or legacy id?
        private int data;
        private String id;
    }

    @Getter
    @AllArgsConstructor
    public static class JavaItem {
        private String id;
        private int runtimeId; // or legacy id?
    }
}
