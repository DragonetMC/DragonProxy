package org.dragonet.proxy.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2017/12/3.
 */
public class DebugTools {
    
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

    public static String getAllFields(Object obj) {
        if(obj == null) {
            return "NULL";
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        String data = "INSTANCE OF " + obj.getClass().getName() + "\n";
        for(Field f : fields) {
            if((f.getModifiers() & Modifier.STATIC) > 0) continue;
            try {
                if (isWrapperType(f.get(obj).getClass()))
                    data += ": " + f.getName() + " = " + f.get(obj).toString();
                else
                    data += ": " + getAllFields(f.get(obj));
            } catch (Exception e) {
                data += ": " + f.getName() + " = ERROR";
            }
            data += "\n";
        }
        return data;
    }

}
