/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.util;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.property.PropertyException;
import com.github.steveice10.mc.auth.service.SessionService;
import com.nukkitx.protocol.bedrock.data.ImageData;
import com.nukkitx.protocol.bedrock.data.SerializedSkin;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.ProxySession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

@Log4j2
public class SkinUtils {
    private static final SessionService service = new SessionService();

    public static ImageData STEVE_SKIN;

    static {
        try {
            STEVE_SKIN = parseBufferedImage(ImageIO.read(DragonProxy.class.getClassLoader().getResource("skin_steve.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SerializedSkin createSkinEntry(ProxySession session, ImageData skinImage) {
        String randomId = UUID.randomUUID().toString();
        return SerializedSkin.of(
            randomId,
            new String(Base64.getDecoder().decode(session.getClientData().getSkinGeometryName())),
            skinImage,
            Collections.emptyList(),
            ImageData.EMPTY,
            "",
            "",
            false,
            false,
            false,
            "",
            randomId);
    }

    /**
     * Fetches a skin from the Mojang session server
     */
    public static ImageData fetchSkin(GameProfile profile) {
        try {
            service.fillProfileTextures(profile, false);
        } catch (PropertyException e) {
            log.debug("Failed to fill profile with textures: " + e.getMessage());
            return null;
        }

        GameProfile.Texture texture = profile.getTexture(GameProfile.TextureType.SKIN);
        if(texture != null) {
            try {
                return parseBufferedImage(ImageIO.read(new URL(texture.getURL())));
            } catch (IOException e) {
                log.warn("Failed to fetch skin for player " + profile.getName() + ": " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Checks if a player has an optifine cape and if so downloads it from
     * optifine's servers
     */
    public static ImageData fetchOptifineCape(GameProfile profile) {
        try {
            URL url = new URL("http://s.optifine.net/capes/" + profile.getName() + ".png");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if(connection.getResponseCode() == 404) {
                log.info("Player " + profile.getName() + " does not have an optifine cape");
                return null;
            }
            log.warn("Player " + profile.getName() + " does have an optifine cape");

            return parseBufferedImage(ImageIO.read(connection.getInputStream()));
        } catch (IOException e) {
            log.warn("Failed to fetch optifine cape for player " + profile.getName() + ": " + e.getMessage());
        }
        return null;
    }

    private static ImageData parseBufferedImage(BufferedImage image) {
        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();

        for(int y = 0; y < image.getHeight(); ++y) {
            for(int x = 0; x < image.getWidth(); ++x) {
                Color color = new Color(image.getRGB(x, y), true);
                outputStream.write(color.getRed());
                outputStream.write(color.getGreen());
                outputStream.write(color.getBlue());
                outputStream.write(color.getAlpha());
            }
        }

        image.flush();
        return ImageData.of(image.getWidth(), image.getHeight(), outputStream.array);
    }
}
