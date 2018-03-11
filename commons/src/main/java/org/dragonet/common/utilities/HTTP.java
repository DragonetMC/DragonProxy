/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details.
 *
 * @author The Dragonet Team
 */
package org.dragonet.common.utilities;

import com.google.gson.JsonObject;
import io.netty.util.CharsetUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class HTTP {

    public static String performGetRequest(String url) {
        if (url == null)
            throw new IllegalArgumentException("URL cannot be null.");

        InputStream in = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
                in = connection.getInputStream();
            else
                in = connection.getErrorStream();

            if (in != null) {
                int data = -1;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                while ((data = in.read()) != -1)
                    bos.write(data);

                return new String(bos.toByteArray(), CharsetUtil.UTF_8);
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
    }

    public static String performPostRequest(String url, Map<String, String> params) {
        if (url == null)
            throw new IllegalArgumentException("URL cannot be null.");

        InputStream in = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            StringJoiner sj = new StringJoiner("&");
            for (Map.Entry<String, String> entry : params.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            connection.setFixedLengthStreamingMode(length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.connect();
            try (OutputStream os = connection.getOutputStream()) {
                os.write(out);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
                in = connection.getInputStream();
            else
                in = connection.getErrorStream();

            if (in != null) {
                int data = -1;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                while ((data = in.read()) != -1)
                    bos.write(data);

                return new String(bos.toByteArray(), CharsetUtil.UTF_8);
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
    }

    public static String performPostRequest(String url, String params) {
        if (url == null)
            throw new IllegalArgumentException("URL cannot be null.");

        InputStream in = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            byte[] out = params.getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            connection.setFixedLengthStreamingMode(length);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.connect();
            try (OutputStream os = connection.getOutputStream()) {
                os.write(out);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
                in = connection.getInputStream();
            else
                in = connection.getErrorStream();

            if (in != null) {
                int data = -1;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                while ((data = in.read()) != -1)
                    bos.write(data);

                return new String(bos.toByteArray(), CharsetUtil.UTF_8);
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
    }
}
