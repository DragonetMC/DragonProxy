package org.dragonet.common.data.blocks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.dragonet.common.utilities.BinaryStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalBlockPalette {
    private static final IntIntHashMap legacyToRuntimeId = new IntIntHashMap();
    private static final IntIntHashMap runtimeIdToLegacy = new IntIntHashMap();
    private static final AtomicInteger runtimeIdAllocator = new AtomicInteger(0);
    private static final byte[] compiledMappings;

    static {
        legacyToRuntimeId.setDefaultReturnValue(-1);
        runtimeIdToLegacy.setDefaultReturnValue(-1);

        Gson gson = new Gson();
        Reader reader = new InputStreamReader(GlobalBlockPalette.class.getResourceAsStream("/runtime_ids.json"), StandardCharsets.UTF_8);
        Type collectionType = new TypeToken<Collection<TableEntry>>(){}.getType();
        Collection<TableEntry> entries = gson.fromJson(reader, collectionType);
        BinaryStream stream = new BinaryStream();

        stream.putUnsignedVarInt(entries.size());
        for (TableEntry entry : entries) {
            registerMapping((entry.id << 4) | entry.data);
            stream.putString(entry.name);
            stream.putLShort(entry.data);
        }

        compiledMappings = stream.getBuffer();
    }

    public static int getOrCreateRuntimeId(int id, int meta) {
        return getOrCreateRuntimeId((id << 4) | meta);
    }

    public static int getOrCreateRuntimeId(int legacyId) {
        int runtimeId = legacyToRuntimeId.get(legacyId);
        if (runtimeId == -1) {
            runtimeId = registerMapping(legacyId);
            //System.out.println("Unmapped block registered. May not be recognised client-side");
        }
        return runtimeId;
    }

    public static byte[] getCompiledMappings() {
        return compiledMappings;
    }

    private static int registerMapping(int legacyId) {
        int runtimeId = runtimeIdAllocator.getAndIncrement();
        runtimeIdToLegacy.put(runtimeId, legacyId);
        legacyToRuntimeId.put(legacyId, runtimeId);
        return runtimeId;
    }

    private static class TableEntry {
        private int id;
        private int data;
        private int runtimeID;
        private String name;
    }

    public static class IntIntHashMap extends HashMap<Integer, Integer> {
        private final HashMap<Integer, Integer> intToInt = new HashMap<Integer, Integer>();
        private int defaultInt = -1;

        public int get(int key) {
            Integer val = super.get(key);
            if (val == null) {
                return defaultInt;
            }
            return val;
        }

        public void setDefaultReturnValue(int defaultInt) {
            this.defaultInt = defaultInt;
        }

        public int getDefaultReturnValue() {
            return defaultInt;
        }
    }
}
