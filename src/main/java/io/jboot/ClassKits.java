package io.jboot;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.jfinal.log.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类实例创建者创建者 * Created by michael on 17/3/21. * * @author michael
 */
@SuppressWarnings("ALL")
public class ClassKits
{
    private static final Map<Class, Object> singletons = new ConcurrentHashMap<>();
    public static Log log = Log.getLog(ClassKits.class);
    private static Multimap<Class<?>, Method> classMethodsCache = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

    public static <T> T singleton(Class<T> clazz)
    {
        Object object = singletons.get(clazz);
        if (object == null)
        {
            synchronized (clazz)
            {
                object = singletons.get(clazz);
                if (object == null)
                {
                    object = newInstance(clazz);
                    if (object != null) singletons.put(clazz, object);
                    else throw new RuntimeException("Failed to create instance: " + clazz);
                }
            }
        }
        return (T) object;
    }

    public static <T> T newInstance(Class<T> clazz)
    {
        return newInstance(clazz, true);
    }

    public static <T> T newInstance(String className)
    {
        try
        {
            Class<T> clazz = (Class<T>) Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            return newInstance(clazz);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to create instance: " + className + "\n" + e.toString(), e);
        }
    }

    public static <T> T newInstance(Class<T> clazz, boolean createByGuice)
    {
        try
        {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to create instance: " + clazz + "\n" + e.toString(), e);
        }
    }

    public static Class<?> getUsefulClass(Class<?> clazz)
    {
        return clazz.getName().indexOf("$$EnhancerBy") == -1 ? clazz : clazz.getSuperclass();
    }

    public static Collection<Method> getClassSetMethods(Class clazz)
    {
        Collection<Method> setMethods = classMethodsCache.get(clazz);
        if (ArrayUtils.isNullOrEmpty(setMethods))
        {
            initSetMethodsCache(clazz);
            setMethods = classMethodsCache.get(clazz);
        }
        return setMethods != null ? new ArrayList<>(setMethods) : null;
    }

    private static void initSetMethodsCache(Class clazz)
    {
        synchronized (clazz)
        {
            Method[] methods = clazz.getMethods();
            for (Method method : methods)
                if (method.getName().startsWith("set") && method.getName().length() > 3 && method.getParameterCount() == 1)
                    classMethodsCache.put(clazz, method);
        }
    }
}