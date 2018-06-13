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
package org.dragonet.proxy.utilities;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.data.GameProfile.Property;
import com.github.steveice10.mc.auth.service.ProfileService;
import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.dragonet.api.skins.ISkinFetcher;
import org.dragonet.common.data.entity.Skin;
import org.dragonet.common.data.entity.Skin.Cape;

/**
 *
 * @author Epic
 */
public class SkinFetcher implements ISkinFetcher {

    private final Gson gson = new Gson();
    private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private final Cache<UUID, PlayerProfile> profilesCache = CacheBuilder.newBuilder().
            maximumSize(5000).
            expireAfterWrite(4, TimeUnit.HOURS).
            build();
    private ProfileService profileService = new ProfileService();

    public SkinFetcher() {

    }

    //TODO : make tha part async
//    public Skin getSkin(String name) {
//        GameProfile gameProfile = null;
//        profileService.findProfilesByName(new String[]{name}, new ProfileLookupCallback() {
//            public void onProfileLookupSucceeded(GameProfile profile) {
//            }
//
//            @Override
//            public void onProfileLookupFailed(GameProfile profile, Exception e) {
//                
//            }
//        });
//        if (gameProfile.isComplete()) {
//            PlayerProfile playerProfile = getPlayerProfile(gameProfile);
//            if (playerProfile != null && playerProfile.getSkin() != null)
//                return playerProfile.getSkin();
//        }
//        return Skin.DEFAULT_SKIN_STEVE;
//    }

    public Skin getSkin(GameProfile gameProfile) {
        if (gameProfile.isComplete()) {
            PlayerProfile playerProfile = getPlayerProfile(gameProfile);
            if (playerProfile != null && playerProfile.getSkin() != null)
                return playerProfile.getSkin();
        }
        return Skin.DEFAULT_SKIN_STEVE;
    }

    private PlayerProfile getPlayerProfile(GameProfile gameProfile) {
        try {
            return profilesCache.get(gameProfile.getId(), () -> {
                Property property = getProfileProperty(gameProfile.getId());
                System.out.println(property.toString());
                Skin skin = Skin.DEFAULT_SKIN_STEVE;
                if (gameProfile.getTextures().containsKey(GameProfile.TextureType.SKIN)) {
                    System.out.println(gameProfile.getTexture(GameProfile.TextureType.SKIN).getURL());
                    System.out.println(Skin.getModelFromJava(gameProfile.getTexture(GameProfile.TextureType.SKIN).getModel()));
                    skin = new Skin(new URL(gameProfile.getTexture(GameProfile.TextureType.SKIN).getURL()), Skin.getModelFromJava(gameProfile.getTexture(GameProfile.TextureType.SKIN).getModel()));
                }
                if (gameProfile.getTextures().containsKey(GameProfile.TextureType.CAPE))
                    skin.setCape(skin.new Cape(new URL(gameProfile.getTexture(GameProfile.TextureType.CAPE).getURL())));
                PlayerProfile profile = new PlayerProfile(gameProfile.getId(), property, skin);
                if (property != null) {
//                    profilesCache.put(gameProfile.getId(), profile);
                    return profile;
                }
                return null;
            });
        } catch (CacheLoader.InvalidCacheLoadException | ExecutionException ex) {
            return null;
        }
    }

    // This will be cached by Guava
    private Property getProfileProperty(UUID Uuid) throws IOException, ParseException {
        final URL url = new URL(PROFILE_URL + Uuid.toString().replace("-", "") + "?unsigned=false");
        final HttpURLConnection URLConnection = (HttpURLConnection) url.openConnection();

        System.out.println("ResponseCode : " + URLConnection.getResponseCode() + " UUID : " + Uuid.toString());

        Property property = null;

        if (URLConnection.getResponseCode() == 200 || URLConnection.getResponseCode() == 204) {
            JsonObject jsonObject = gson.fromJson(new InputStreamReader(URLConnection.getInputStream(), Charsets.UTF_8), JsonObject.class);
            JsonArray jsonArray = jsonObject.get("properties").getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject rawProperty = jsonArray.get(i).getAsJsonObject();
                String name = rawProperty.get("name").getAsString();
                String value = rawProperty.get("value").getAsString();
                String signature = rawProperty.get("signature").getAsString();

                property = new Property(name, value, signature);
            }
            System.out.println("Fetch Profile Property : " + Uuid + " OK!");
        }
        return property;
    }

    public class PlayerProfile {

        private final UUID uuid;
        private final Property propertie;
        private final Skin skin;

        PlayerProfile(UUID uuid, Property propertie, Skin skin) {
            this.uuid = uuid;
            this.propertie = propertie;
            this.skin = skin;
        }

        public UUID getUuid() {
            return uuid;
        }

        public Property getPropertie() {
            return propertie;
        }

        public Skin getSkin() {
            return skin;
        }
    }
}
