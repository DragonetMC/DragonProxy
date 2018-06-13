package org.dragonet.proxy.utilities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ItemsRuntimeIDTool {

    private final Gson gson = new Gson();
    private final String URL = "https://gist.githubusercontent.com/Tomcc/ad971552b024c7619e664d0377e48f58/raw/65549c12afa84bce76cd25696ac845b293d8ab13/runtimeid_table.json";
    private final String nukkitFile = "/home/vincent/git/Nukkit/creativeitems.json";

    public ItemsRuntimeIDTool() {
        try {
            Reader newBlocksReader = new InputStreamReader(new URL(URL).openStream()); //Read the json output
            Type newBlocksObjectsList = new TypeToken<ArrayList<BlockData>>() {}.getType();
            List<BlockData> newBlocks = gson.fromJson(newBlocksReader,  newBlocksObjectsList);

//            Type oldBlocksObjectsList = new TypeToken<ArrayList<OldBlocksData>>() {}.getType();
            JsonObject oldBlocksListObject = gson.fromJson(new FileReader(nukkitFile), JsonObject.class);
            JsonArray array = oldBlocksListObject.get("items").getAsJsonArray();
//            List<OldBlocksData> oldBlocksList = gson.fromJson(new FileReader(nukkitFile), oldBlocksObjectsList);
            Multimap<Integer,Integer> oldBlocks = ArrayListMultimap.create();
            array.forEach(object -> {
                if (object.isJsonObject()) {
                    int id = 0;
                    int damage = 0;
                    if (object.getAsJsonObject().has("id"))
                        id = object.getAsJsonObject().get("id").getAsInt();
                    if (object.getAsJsonObject().has("damage"))
                        damage = object.getAsJsonObject().get("damage").getAsInt();
                    oldBlocks.put(id, damage);
                }
            });

            for(BlockData block : newBlocks) {
                if (oldBlocks.containsKey(block.id)) {
                    if (oldBlocks.get(block.id).contains(block.data)) {
                        String NAME = block.name.split(":")[1].toUpperCase();
                        if (block.data != 0)
                            NAME += "_" + block.data;
                        String end = block.id != 255 ? "," : ";";
                        System.out.println(NAME + "(\"" + block.name + "\", " + block.id + ", " + block.data + ")" + end);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new ItemsRuntimeIDTool();
    }

    private class BlockData {

        private int runtimeID;
        private String name;
        private int id;
        private int data;

        @Override
        public String toString() {
            return name + " (" + runtimeID + ") " + id + ":" + data;
        }
    }
}
