/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.common.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vincent
 */
public class ReflectedClass
{
    private final Class<?> theClass;
    private Object theContext;

    public ReflectedClass(String className) throws ClassNotFoundException
    {
        this.theClass = Class.forName(className);
    }

    public ReflectedClass(Class theClass)
    {
        this.theClass = theClass;
    }

    public ReflectedClass(Object theContext)
    {
        this.theClass = theContext.getClass();
        this.theContext = theContext;
    }

    public Class<?> getReflectedClass()
    {
        return this.theClass;
    }

    public <K> K cast(Object theObject)
    {
        return (K)this.theClass.cast(theObject);
    }

    public ReflectedClass setContext(Object theContext)
    {
        this.theContext = theContext;
        return this;
    }

    public Object getContext()
    {
        return this.theContext;
    }

    public void newInstance()
    {
        try {
            this.theContext = this.theClass.newInstance();
        } catch (
                SecurityException |
                InstantiationException |
                IllegalAccessException |
                IllegalArgumentException ex) {
            Logger.getLogger(ReflectedClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newInstance(Class<?>[] theClasses, Object... theObject)
    {
        try {
            this.theContext = this.theClass.getConstructor(theClasses).newInstance(theObject);
        } catch (NoSuchMethodException |
                SecurityException |
                InstantiationException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException ex) {
            Logger.getLogger(ReflectedClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean fieldExist(String theFieldName)
    {
        try
        {
            this.theContext.getClass().getDeclaredField(theFieldName);
            return true;
        }
        catch (NoSuchFieldException | SecurityException ex)
        {
            Logger.getLogger(ReflectedClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public <K> K getField(String theFieldName)
    {
        return (K)getField(theFieldName, theContext, theClass);
    }

    public void setField(String theFieldName, Object theValue)
    {
        setField(theFieldName, this.theContext, theValue, this.theClass);
    }

    private <K> K getField(String theName, Object theContext, Class theClazz)
    {
        Class theClass;
        if (theClazz == null)
            theClass = theContext.getClass();
        else
            theClass = theClazz;

        try
        {
            Field theField;
            theField = theClass.getDeclaredField(theName);
            theField.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(theField, theField.getModifiers() & ~Modifier.FINAL);
            return (K)theField.get(theContext);
        }
        catch (NoSuchFieldException Ex)
        {
            if (theClass.getSuperclass() != Object.class)
                return getField(theName, theContext, theClass.getSuperclass());
            else
                Ex.printStackTrace();
        }
        catch (IllegalAccessException Ex)
        {
            Ex.printStackTrace();
            return null;
        }
        return null;
    }

    private void setField(String theName, Object theContext, Object theValue, Class theClazz)
    {
        Class theClass;
        if (theClazz == null)
            theClass = theContext.getClass();
        else
            theClass = theClazz;

        try
        {
            Field theField;
            theField = theClass.getDeclaredField(theName);
            theField.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(theField, theField.getModifiers() & ~Modifier.FINAL);
            theField.set(theContext, theValue);
        }
        catch (NoSuchFieldException Ex)
        {
            if (theClass.getSuperclass() != Object.class)
                this.setField(theName, theContext, theValue, theClass.getSuperclass());
            else
                Ex.printStackTrace();
        }
        catch (IllegalAccessException Ex)
        {
            Ex.printStackTrace();
        }
    }

    public boolean methodExist(String theName, Object... theParams)
    {
        try {
            Class[] theClassTab = new Class[theParams.length];
            for (int i = 0; i < theParams.length; i++)
                theClassTab[i] = theParams[i].getClass();

            Method theMethod = this.theClass.getDeclaredMethod(theName, theClassTab);

            return theMethod != null;
        } catch (NoSuchMethodException |
                SecurityException |
                IllegalArgumentException ex) {
            Logger.getLogger(ReflectedClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public <K> K invokeMethod(String theName, Object... theParams)
    {
        try {
            Class[] theClassTab = new Class[theParams.length];
            for (int i = 0; i < theParams.length; i++)
                theClassTab[i] = theParams[i].getClass();

            Method theMethod = theClass.getDeclaredMethod(theName, theClassTab);
            theMethod.setAccessible(true);

            return (K) theMethod.invoke(this.theContext, theParams);
        } catch (NoSuchMethodException |
                SecurityException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException ex) {
            Logger.getLogger(ReflectedClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void listFields()
    {
        listFields(this.theClass);
    }

    private void listFields(Object theObject){
        try
        {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Fields : \n");
            for (Field theField : theObject.getClass().getFields())
            {
                theField.setAccessible(true);
                stringBuffer.append("- ").append(theField.getName())
                        .append(" (").append(theField.getType()).append(") = ")
                        .append(theField.get(theObject)).append("\n");
            }
            stringBuffer.append("Declared Fields : \n");
            for (Field theField : theObject.getClass().getDeclaredFields())
            {
                theField.setAccessible(true);
                stringBuffer.append("- ").append(theField.getName())
                        .append(" (").append(theField.getType()).append(") = ")
                        .append(theField.get(theObject)).append("\n");
            }
            System.out.println(stringBuffer);
        }
        catch (IllegalAccessException Ex)
        {
            Ex.printStackTrace();
        }
    }

    public void listMethods()
    {
        listMethods(theClass);
    }

    private void listMethods(Class theClass){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Methods : \n");
        for (Method theMethod : theClass.getMethods())
        {
            theMethod.setAccessible(true);
            stringBuffer.append("- ").append(theMethod.getName()).append(" (");
            for (Parameter theParameter : theMethod.getParameters())
            {
                stringBuffer.append(theParameter.getType().getSimpleName()).append(" ").append(theParameter.getName()).append(", ");
            }
            stringBuffer.append(") return ").append(theMethod.getReturnType().getSimpleName()).append("\n");
        }
        stringBuffer.append("Declared Methods : \n");
        for (Method theMethod : theClass.getDeclaredMethods())
        {
            theMethod.setAccessible(true);
            stringBuffer.append("- ").append(theMethod.getName()).append(" (");
            for (Parameter theParameter : theMethod.getParameters())
            {
                stringBuffer.append(theParameter.getType().getSimpleName()).append(" ").append(theParameter.getName()).append(", ");
            }
            stringBuffer.append(") return ").append(theMethod.getReturnType().getSimpleName()).append("\n");
        }
        System.out.println(stringBuffer);
    }
}
