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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONValue;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.PlayerListCache;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.UUID;

@Log4j2
public class SkinUtils {
    private static final SessionService service = new SessionService();

    public static ImageData STEVE_SKIN;

    static {
        try {
            STEVE_SKIN = parseBufferedImage(ImageIO.read(DragonProxy.class.getClassLoader().getResource("skin_steve.png")), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SerializedSkin createSkinEntry(ImageData skinImage, GameProfile.TextureModel model, ImageData capeImage) {
        String randomId = UUID.randomUUID().toString();
        return SerializedSkin.of(
            randomId,
            convertLegacyGeometryName((model == GameProfile.TextureModel.SLIM) ? "Slim" : ""),
            skinImage,
            Collections.emptyList(),
            capeImage,
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
    public static ImageData fetchSkin(ProxySession session, GameProfile profile) {
        // TODO: HANDLE RATE LIMITING
        PlayerListCache playerListCache = session.getPlayerListCache();

        // Check if the skin is already cached
        if(playerListCache.getRemoteSkinCache().containsKey(profile.getId())) {
            //log.warn("Retrieving from cache: " + profile.getName());
            return playerListCache.getRemoteSkinCache().get(profile.getId());
        }

        GameProfile.Texture texture;
        try {
            texture = profile.getTexture(GameProfile.TextureType.SKIN);
        } catch (PropertyException e) {
            log.warn("Failed to get skin for player " + profile.getName(), e);
            return null;
        }
        if(texture != null) {
            try {
                ImageData skin = parseBufferedImage(ImageIO.read(new URL(texture.getURL())), false);
                playerListCache.getRemoteSkinCache().put(profile.getId(), skin); // Cache the skin
                return skin;
            } catch (IOException e) {
                log.warn("Failed to fetch skin for player " + profile.getName() + ": " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Checks if a player has a mojang cape or unofficial cape and if so downloads it from
     * their servers
     */
    public static ImageData fetchCape(ProxySession session, GameProfile profile) {
        // TODO: HANDLE RATE LIMITING
        PlayerListCache playerListCache = session.getPlayerListCache();

        // Check if the cape is already cached
        if(playerListCache.getRemoteCapeCache().containsKey(profile.getId())) {
            //log.warn("Retrieving from cache: " + profile.getName());
            return playerListCache.getRemoteCapeCache().get(profile.getId());
        }

        GameProfile.Texture texture;
        try {
            texture = profile.getTexture(GameProfile.TextureType.CAPE);
        } catch (PropertyException e) {
            return null;
        }

        if(texture != null) {
            try {
                ImageData cape = parseBufferedImage(ImageIO.read(new URL(texture.getURL())), false);
                playerListCache.getRemoteCapeCache().put(profile.getId(), cape); // Cache the cape
                return cape;
            } catch (IOException e) {
                log.warn("Failed to fetch cape for player " + profile.getName() + ": " + e.getMessage());
            }
        } else {
            for (CapeServers server : CapeServers.values()) {
                try {
                    URL url = new URL(server.getUrl(profile));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    if (connection.getResponseCode() != 404) {
                        log.warn(String.format("%s has cape at %s", profile.getName(), texture.getURL()));
                        return parseBufferedImage(ImageIO.read(connection.getInputStream()), true);
                    }
                } catch (IOException e) {
                    log.warn("Failed to fetch cape for player " + profile.getName() + ": " + e.getMessage());
                }
            }
        }
        return ImageData.EMPTY;
    }

    @RequiredArgsConstructor
    private enum CapeServers {
        OPTIFINE("http://s.optifine.net/capes/%s.png", CapeUrlType.USERNAME),
        MINECRAFTCAPES("https://minecraftcapes.co.uk/getCape/%s", CapeUrlType.UUID);

        private final String url;
        private final CapeUrlType type;

        private String getUrl(GameProfile profile) {
            switch(type) {
                case UUID:
                    return String.format(url, profile.getId().toString().replace("-", ""));
                case USERNAME:
                    return String.format(url, profile.getName());
            }
            return null;
        }
    }

    private enum CapeUrlType {
        UUID,
        USERNAME
    }

    private static ImageData parseBufferedImage(BufferedImage image, boolean cape) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int bedrockSkinSize = (imageWidth * imageHeight) * 4;

        //Capes need to be 64x32, 128x64 etc otherwise they will render weird. This is an issue i had on MinecratCapes
        if(cape) {
            imageWidth = 64;
            imageHeight = 32;
            while((imageWidth < image.getWidth()) || (imageHeight < image.getHeight())) {
                imageWidth *= 2;
                imageHeight *= 2;
            }
        }

        FastByteArrayOutputStream out = new FastByteArrayOutputStream(bedrockSkinSize);
        for(int y = 0; y < image.getHeight(); ++y) {
            for(int x = 0; x < image.getWidth(); ++x) {
                Color color = new Color(image.getRGB(x, y), true);
                out.write(color.getRed());
                out.write(color.getGreen());
                out.write(color.getBlue());
                out.write(color.getAlpha());
            }
        }

        image.flush();

        return ImageData.of(image.getWidth(), image.getHeight(), out.array);
    }

    private static String convertLegacyGeometryName(String geometryModel) {
        return "{\"geometry\" : {\"default\" : \"geometry.humanoid.custom" + JSONValue.escape(geometryModel) + "\"}}";
    }
}
