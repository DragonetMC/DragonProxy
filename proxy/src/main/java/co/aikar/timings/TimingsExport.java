/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import static co.aikar.timings.TimingsManager.HISTORY;
import com.google.gson.JsonArray;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.common.utilities.JsonUtil;

public class TimingsExport extends Thread {

    private final UpstreamSession sender;
    private final JsonObject out;
    private final TimingsHistory[] history;

    private TimingsExport(UpstreamSession sender, JsonObject out, TimingsHistory[] history) {
        super("Timings paste thread");
        this.sender = sender;
        this.out = out;
        this.history = history;
    }

    /**
     * Builds a JSON timings report and sends it to Aikar's viewer
     *
     * @param sender Sender that issued the command
     */
    public static void reportTimings(UpstreamSession sender) {
        JsonObject out = new JsonObject();
        out.addProperty("version", DragonProxy.getInstance().getVersion());
        out.addProperty("maxplayers", 2); //TODO
        out.addProperty("start", TimingsManager.timingStart / 1000);
        out.addProperty("end", System.currentTimeMillis() / 1000);
        out.addProperty("sampletime", (System.currentTimeMillis() - TimingsManager.timingStart) / 1000);

        if (!Timings.isPrivacy()) {
            out.addProperty("server", "DragonProxy : name"); //TODO
            out.addProperty("motd", DragonProxy.getInstance().getMotd());
            out.addProperty("online-mode", DragonProxy.getInstance().getAuthMode().equals("online"));
            out.addProperty("icon", ""); //"data:image/png;base64,"
        }

        final Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

        JsonObject system = new JsonObject();
        system.addProperty("timingcost", getCost());
        system.addProperty("name", System.getProperty("os.name"));
        system.addProperty("version", System.getProperty("os.version"));
        system.addProperty("jvmversion", System.getProperty("java.version"));
        system.addProperty("arch", System.getProperty("os.arch"));
        system.addProperty("maxmem", runtime.maxMemory());
        system.addProperty("cpu", runtime.availableProcessors());
        system.addProperty("runtime", ManagementFactory.getRuntimeMXBean().getUptime());
        system.addProperty("flags", String.join(" ", runtimeBean.getInputArguments()));
        system.add("gc", JsonUtil.mapToObject(ManagementFactory.getGarbageCollectorMXBeans(), (input)
                -> new JsonUtil.JSONPair(input.getName(), JsonUtil.toArray(input.getCollectionCount(), input.getCollectionTime()))));
        out.add("system", system);

        TimingsHistory[] history = HISTORY.toArray(new TimingsHistory[HISTORY.size() + 1]);
        history[HISTORY.size()] = new TimingsHistory(); //Current snapshot

        JsonObject timings = new JsonObject();
        for (TimingIdentifier.TimingGroup group : TimingIdentifier.GROUP_MAP.values())
        {
            for (Timing id : group.timings.stream().toArray(Timing[]::new)) {
                if (!id.timed && !id.isSpecial())
                    continue;

                timings.add(String.valueOf(id.id), JsonUtil.toArray(group.id, id.name));
            }
        }

        JsonObject idmap = new JsonObject();
        idmap.add("groups", JsonUtil.mapToObject(TimingIdentifier.GROUP_MAP.values(), (group)
                -> new JsonUtil.JSONPair(group.id, group.name)));
        idmap.add("handlers", timings);
        idmap.add("worlds", JsonUtil.mapToObject(TimingsHistory.levelMap.entrySet(), (entry)
                -> new JsonUtil.JSONPair(entry.getValue(), entry.getKey())));
        idmap.add("tileentity", JsonUtil.mapToObject(TimingsHistory.blockEntityMap.entrySet(), (entry)
                -> new JsonUtil.JSONPair(entry.getKey(), entry.getValue())));
        idmap.add("entity", JsonUtil.mapToObject(TimingsHistory.entityMap.entrySet(), (entry)
                -> new JsonUtil.JSONPair(entry.getKey(), entry.getValue())));
        out.add("idmap", idmap);

        //Information about plugins, needed but empty
        out.add("plugins", new JsonArray());

        //Information on the proxy Config
        JsonObject config = new JsonObject();
        if (!Timings.getIgnoredConfigSections().contains("all")) {
            JsonObject jsonConfig = JsonUtil.toObject(DragonProxy.getInstance().getConfig());
            Timings.getIgnoredConfigSections().forEach(jsonConfig::remove);
            config.add("DragonProxy", jsonConfig);
        } else {
            config.add("DragonProxy", null);
        }
        out.add("config", config);

        new TimingsExport(sender, out, history).start();
    }

    private static long getCost() {
        int passes = 200;
        Timing SAMPLER1 = TimingsManager.getTiming(null, "Timings sampler 1", null);
        Timing SAMPLER2 = TimingsManager.getTiming(null, "Timings sampler 2", null);
        Timing SAMPLER3 = TimingsManager.getTiming(null, "Timings sampler 3", null);
        Timing SAMPLER4 = TimingsManager.getTiming(null, "Timings sampler 4", null);
        Timing SAMPLER5 = TimingsManager.getTiming(null, "Timings sampler 5", null);
        Timing SAMPLER6 = TimingsManager.getTiming(null, "Timings sampler 6", null);

        long start = System.nanoTime();
        for (int i = 0; i < passes; i++) {
            SAMPLER1.startTiming();
            SAMPLER2.startTiming();
            SAMPLER3.startTiming();
            SAMPLER4.startTiming();
            SAMPLER5.startTiming();
            SAMPLER6.startTiming();
            SAMPLER6.stopTiming();
            SAMPLER5.stopTiming();
            SAMPLER4.stopTiming();
            SAMPLER3.stopTiming();
            SAMPLER2.stopTiming();
            SAMPLER1.stopTiming();
        }

        long timingsCost = (System.nanoTime() - start) / passes / 6;

        SAMPLER1.reset(true);
        SAMPLER2.reset(true);
        SAMPLER3.reset(true);
        SAMPLER4.reset(true);
        SAMPLER5.reset(true);
        SAMPLER6.reset(true);

        return timingsCost;
    }

    @Override
    public void run() {
        if (this.sender != null)
            this.sender.sendChat("Starting upload timings !");
        this.out.add("data", JsonUtil.mapToArray(this.history, TimingsHistory::export));

        String response = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://timings.aikar.co/post").openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", "Nukkit/" + "test" + "/" + InetAddress.getLocalHost().getHostName()); //need to keep nukkit for system to work
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(false);

            OutputStream request = new GZIPOutputStream(con.getOutputStream()) {{ this.def.setLevel(7); }};

            request.write(new Gson().toJson(this.out).getBytes("UTF-8"));
            request.close();

            response = getResponse(con);

            if (con.getResponseCode() != 302) {
                if (this.sender != null)
                    this.sender.sendChat("Timings upload error " + con.getResponseCode() + con.getResponseMessage() + " !");
                if (response != null)
                    DragonProxy.getInstance().getLogger().fatal(response);
                return;
            }

            String location = con.getHeaderField("Location");
            if (this.sender != null)
                this.sender.sendChat("Timings report available at " + location);
            DragonProxy.getInstance().getLogger().info("Timings report available at " + location);

            if (response != null && !response.isEmpty())
                if (this.sender != null)
                    this.sender.sendChat("Timings response " + response);

            File timingFolder = new File("timings");
            timingFolder.mkdirs();
            String fileName = timingFolder + File.separator + new SimpleDateFormat("'timings-'yyyy-MM-dd-hh-mm'.txt'").format(new Date());

            FileWriter writer = new FileWriter(fileName);
            writer.write("Timings report available at " + location + "\n\n");
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(this.out));
            writer.close();

            DragonProxy.getInstance().getLogger().info("Timings file writed at " + fileName);
        } catch (IOException exception) {
            DragonProxy.getInstance().getLogger().info("Timings report error !");
            if (this.sender != null)
                this.sender.sendChat("Timings error !");
            if (response != null)
                DragonProxy.getInstance().getLogger().fatal(response);
            exception.printStackTrace();
//            DragonProxy.getInstance().getLogger().logException(exception);
        }
    }

    private String getResponse(HttpURLConnection con) throws IOException {
        InputStream is = null;
        try {
            is = con.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(b)) != -1)
                bos.write(b, 0, bytesRead);
            return bos.toString();

        } catch (IOException exception) {
            if (this.sender != null)
                this.sender.sendChat("Timings error !");
            DragonProxy.getInstance().getLogger().warning(con.getResponseMessage() + "\n" + exception);
            return null;
        } finally {
            if (is != null)
                is.close();
        }
    }
}
