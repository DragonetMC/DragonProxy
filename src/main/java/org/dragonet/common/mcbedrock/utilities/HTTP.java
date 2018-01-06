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
package org.dragonet.common.mcbedrock.utilities;

import io.netty.util.CharsetUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTP {

    public HTTP() {

    }

    public static String performGetRequest(String url) {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }

        InputStream in = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                in = connection.getInputStream();
            } else {
                in = connection.getErrorStream();
            }

            if (in != null) {
                int data = -1;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                while ((data = in.read()) != -1) {
                    bos.write(data);
                }

                return new String(bos.toByteArray(), CharsetUtil.UTF_8);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
