package org.dragonet.proxy.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created on 2017/12/3.
 */
public class DebugTools {

    public static String getAllFields(Object obj) {
        if(obj == null) {
            return "NULL";
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        String data = "INSTANCE OF " + obj.getClass().getName() + "\n";
        for(Field f : fields) {
            if((f.getModifiers() & Modifier.STATIC) > 0) continue;
            try {
                data += ": " + f.getName() + " = " + f.get(obj).toString();
            } catch (Exception e) {
                data += ": " + f.getName() + " = ERROR";
            }
            data += "\n";
        }
        return data;
    }

}
