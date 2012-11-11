package de.engineapp;

import java.lang.reflect.*;

public class PropertyConnector<T>
{
    private enum PropertyType { FIELD, METHOD }
    
    
    private PropertyType pType;
    private Object connectedObject;
    private Class<T> clazz;
    private Field connectedField;
    private Method connectedMethod;
    
    
    @SuppressWarnings("unchecked")
    public PropertyConnector(Object object, String propertyName)
    {
        connectedObject = object;
        
        connectedField = getField(object, propertyName);
        
        if (connectedField != null)
        {
            pType = PropertyType.FIELD;
            connectedField.setAccessible(true);
            clazz = (Class<T>) connectedField.getType();
        }
        else
        {
            connectedMethod = getMethod(object, propertyName);
            
            if (connectedMethod != null)
            {
                pType = PropertyType.METHOD;
                connectedMethod.setAccessible(true);
                clazz = (Class<T>) connectedMethod.getReturnType();
            }
            else
            {
                throw new RuntimeException("'" + propertyName + "' could not found in " + object);
            }
        }
    }
    
    
    public T get()
    {
        try
        {
            switch (pType)
            {
                case FIELD:
                    Object fieldValue = connectedField.get(connectedObject);
                    return clazz.cast(fieldValue);
                    
                case METHOD:
                    Object methodValue = connectedField.get(connectedObject);
                    return clazz.cast(methodValue);
            }
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    public Object getObject()
    {
        try
        {
            switch (pType)
            {
                case FIELD:
                    return connectedField.get(connectedObject);
                    
                case METHOD:
                    return connectedField.get(connectedObject);
            }
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    public static Field getField(Object object, String fieldName)
    {
        return getField(object.getClass(), fieldName);
    }
    
    public static Field getField(Class<?> clazz, String fieldName)
    {
        if (clazz == null || fieldName == null)
        {
            return null;
        }
        for (Field field : clazz.getDeclaredFields())
        {
            if (field.getName().equals(fieldName))
            {
                return field;
            }
        }
        
        return getField(clazz.getSuperclass(), fieldName);
    }
    
    
    public static Method getMethod(Object object, String methodName)
    {
        return getMethod(object.getClass(), methodName);
    }
    
    public static Method getMethod(Class<?> clazz, String methodName)
    {
        if (clazz == null || methodName == null)
        {
            return null;
        }
        for (Method method : clazz.getDeclaredMethods())
        {
            if (method.getName().equals(methodName))
            {
                return method;
            }
        }
        
        return getMethod(clazz.getSuperclass(), methodName);
    }
}