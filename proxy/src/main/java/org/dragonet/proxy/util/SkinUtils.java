package org.dragonet.proxy.util;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.property.PropertyException;
import com.github.steveice10.mc.auth.service.SessionService;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Log4j2
public class SkinUtils {
    private static final SessionService service = new SessionService();

    /**
     * Fetches a skin from the Mojang session server
     */
    public static byte[] fetchSkin(GameProfile profile) {
        try {
            service.fillProfileTextures(profile, false);
        } catch (PropertyException e) {
            log.trace("Failed to fill profile with textures: " + e.getMessage());
        }

        GameProfile.Texture texture = profile.getTexture(GameProfile.TextureType.SKIN);
        if(texture != null) {
            try {
                BufferedImage image = ImageIO.read(new URL(texture.getURL()));
                return convertToByteArray(image);
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
    public static byte[] fetchOptifineCape(GameProfile profile) {
        try {
            URL url = new URL("http://s.optifine.net/capes/" + profile.getName() + ".png");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if(connection.getResponseCode() == 404) {
                //log.info("Player " + profile.getName() + " does not have an optifine cape");
                return null;
            }
            //log.warn("Player " + profile.getName() + " does have an optifine cape");

            BufferedImage image = ImageIO.read(connection.getInputStream());
            return convertToByteArray(image);
        } catch (IOException e) {
            log.warn("Failed to fetch optifine cape for player " + profile.getName() + ": " + e.getMessage());
        }
        return null;
    }

    private static byte[] convertToByteArray(BufferedImage image) {
        FastByteArrayOutputStream output = new FastByteArrayOutputStream();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                output.write(color.getRed());
                output.write(color.getGreen());
                output.write(color.getBlue());
                output.write(color.getAlpha());
            }
        }
        image.flush();
        return output.array;
    }
}
