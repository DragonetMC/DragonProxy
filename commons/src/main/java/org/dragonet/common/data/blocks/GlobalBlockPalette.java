package org.dragonet.common.data.blocks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalBlockPalette {
    private static final IntToInt legacyToRuntimeId = new IntToInt();
    private static final IntToInt runtimeIdToLegacy = new IntToInt();
    private static final AtomicInteger runtimeIdAllocator = new AtomicInteger(0);
    
    public static class IntToInt {
        private final HashMap<Integer, Integer> intToInt = new HashMap<Integer, Integer>();
        private int defaultInt = -1;
        
        public int get(int firstInt) {
            if(!intToInt.containsKey(firstInt)) {
                return defaultInt;
            }
            return intToInt.get(firstInt);
        }
        
        public void put(int firstInt, int secondInt) {
            intToInt.put(firstInt, secondInt);
        }
        
        public void unregister(int firstInt) {
            if(intToInt.containsKey(firstInt)) {
                intToInt.remove(firstInt);
            }
        }
        
        public void defaultReturnValue(int defaultInt) {
            this.defaultInt = defaultInt;
        }
    }

    static {
        legacyToRuntimeId.defaultReturnValue(-1);
        runtimeIdToLegacy.defaultReturnValue(-1);

        try {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(GlobalBlockPalette.class.getResourceAsStream("/runtime_ids.json"), "UTF-8");
            Type collectionType = new TypeToken<Collection<TableEntry>>(){}.getType();
            Collection<TableEntry> entries = gson.fromJson(reader, collectionType);

            for (TableEntry entry : entries) {
                registerMapping(entry.runtimeID, (entry.id << 4) | entry.data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getOrCreateRuntimeId(int id, int meta) {
        return getOrCreateRuntimeId((id << 4) | meta);
    }

    public static int getOrCreateRuntimeId(int legacyId) {
        int runtimeId = legacyToRuntimeId.get(legacyId);
        if (runtimeId == -1) {
            runtimeId = registerMapping(runtimeIdAllocator.incrementAndGet(), legacyId);
            //System.out.println("Unmapped block registered. May not be recognised client-side");
        }
        return runtimeId;
    }

    private static int registerMapping(int runtimeId, int legacyId) {
        runtimeIdToLegacy.put(runtimeId, legacyId);
        legacyToRuntimeId.put(legacyId, runtimeId);
        runtimeIdAllocator.set(Math.max(runtimeIdAllocator.get(), runtimeId));
        return runtimeId;
    }

    private static class TableEntry {
        private int id;
        private int data;
        private int runtimeID;
        private String name;
    }
}