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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

/**
 * author: MagicDroidX Nukkit Project
 */
public class MCPESkin {

    public static final int SINGLE_SKIN_SIZE = 64 * 32 * 4;
    public static final int DOUBLE_SKIN_SIZE = 64 * 64 * 4;
    public static final String MODEL_STEVE = "Standard_Steve";
    public static final String MODEL_ALEX = "Standard_Alex";
    private byte[] data = new byte[SINGLE_SKIN_SIZE];
    private String model;

    public MCPESkin(byte[] data) {
        this(data, MODEL_STEVE);
    }

    public MCPESkin(InputStream inputStream) {
        this(inputStream, MODEL_STEVE);
    }

    public MCPESkin(ImageInputStream inputStream) {
        this(inputStream, MODEL_STEVE);
    }

    public MCPESkin(File file) {
        this(file, MODEL_STEVE);
    }

    public MCPESkin(URL url) {
        this(url, MODEL_STEVE);
    }

    public MCPESkin(BufferedImage image) {
        this(image, MODEL_STEVE);
    }

    public MCPESkin(byte[] data, String model) {
        this.setData(data);
        this.setModel(model);
    }

    public MCPESkin(InputStream inputStream, String model) {
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.parseBufferedImage(image);
        this.setModel(model);
    }

    public MCPESkin(ImageInputStream inputStream, String model) {
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.parseBufferedImage(image);
        this.setModel(model);
    }

    public MCPESkin(File file, String model) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.parseBufferedImage(image);
        this.setModel(model);
    }

    public MCPESkin(URL url, String model) {
        BufferedImage image;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.parseBufferedImage(image);
        this.setModel(model);
    }

    public MCPESkin(BufferedImage image, String model) {
        this.parseBufferedImage(image);
        this.setModel(model);
    }

    public MCPESkin(String base64) {
        this(Base64.getDecoder().decode(base64));
    }

    public MCPESkin(String base64, String model) {
        this(Base64.getDecoder().decode(base64), model);
    }

    public void parseBufferedImage(BufferedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                outputStream.write(color.getRed());
                outputStream.write(color.getGreen());
                outputStream.write(color.getBlue());
                outputStream.write(color.getAlpha());
            }
        }
        image.flush();
        this.setData(outputStream.toByteArray());
    }

    public byte[] getData() {
        return data;
    }

    public String getModel() {
        return model;
    }

    public void setData(byte[] data) {
        if (data.length != SINGLE_SKIN_SIZE && data.length != DOUBLE_SKIN_SIZE) {
            throw new IllegalArgumentException("Invalid skin");
        }
        this.data = data;
    }

    public void setModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            model = MODEL_STEVE;
        }

        this.model = model;
    }

    public boolean isValid() {
        return this.data.length == SINGLE_SKIN_SIZE || this.data.length == DOUBLE_SKIN_SIZE;
    }
}
