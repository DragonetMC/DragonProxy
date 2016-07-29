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

import java.io.*;

public class DefaultSkin {

    protected static String NAME;
    protected static String SKIN_BASE64_ENCODED;
    
    protected static MCPESkin SKIN;

    static {
        loadSkin();
    }

    public static String getDefaultSkinName() {
        return NAME;
    }
    
    public static String getDefaultSkinBase64Encoded() {
        return SKIN_BASE64_ENCODED;
    }
    
    public static MCPESkin getDefaultSkin() {
        return SKIN;
    }

    private static void loadSkin() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream ins = DefaultSkin.class.getResourceAsStream("defaults/SKIN.BIN");
            int d = -1;
            while((d = ins.read()) != -1){
                if(d == ':') {
                    NAME = new String(bos.toByteArray(), "UTF-8");
                    bos.reset();
                } else {
                    bos.write(d);
                }
                
            }
            ins.close();
            SKIN_BASE64_ENCODED = new String(bos.toByteArray(), "UTF-8");
            
            SKIN = new MCPESkin(SKIN_BASE64_ENCODED, NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
