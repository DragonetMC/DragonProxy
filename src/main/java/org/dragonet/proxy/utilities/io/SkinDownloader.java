/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.proxy.utilities.io;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SkinDownloader {

    public SkinDownloader() {

    }

    public static byte[] download(String username) {
        try {
            URL url = new URL(String.format("http://s3.amazonaws.com/MinecraftSkins/%s.png", username));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
            connection.disconnect();
            return out.toByteArray();

        } catch (Exception e) {
            return null;
        }
    }
}
