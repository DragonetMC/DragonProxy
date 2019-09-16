package org.dragonet.proxy.network.translator.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.util.PaletteManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class BlockTranslator {
    private static final Gson gson = new GsonBuilder().create();
    public static final Map<String, Integer> JAVA_BLOCKS = new HashMap<>();

    public BlockTranslator() {
        loadJavaItems();
    }

    private void loadJavaItems() {
        log.info("Loading java blocks");
        // Load java items
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("java_blocks.json");
        if (stream == null) {
            throw new AssertionError("Java block table not found");
        }

        JsonObject object = gson.fromJson(new InputStreamReader(stream), JsonObject.class);

        for(String key : object.keySet()) {
            JAVA_BLOCKS.put(key, object.get(key).getAsInt());
        }
    }

    public static int translateToBedrock(int blockId) {
        for (Map.Entry<String, Integer> entry : JAVA_BLOCKS.entrySet()) {
            if (entry.getValue() != blockId) continue;

            String identifier = getBedrockIdentifier(entry.getKey());
            PaletteManager.RuntimeEntry entry1 = PaletteManager.getRuntimeEntry(identifier);

            if (entry1 != null) {
                return entry1.getId() << 4 | entry1.getData();
            }
        }
        return 0; // air
    }

    private static String getBedrockIdentifier(String javaIdentifier) {
        //todo some
        return javaIdentifier;
    }
}
