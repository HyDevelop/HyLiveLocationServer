package io.jboot;

/**
 * 断言
 */
public class JbootAssert
{
    public static void assertNull(Object object, String message)
    {
        if (object != null)
        {
            throw new RuntimeException(message);
        }
    }

    public static void assertNotNull(Object object, String message)
    {
        if (object == null)
        {
            throw new RuntimeException(message);
        }
    }

    public static void assertFalse(boolean condition, String message)
    {
        if (condition)
        {
            throw new RuntimeException(message);
        }
    }

    public static void assertTrue(boolean condition, String message)
    {
        if (!condition)
        {
            throw new RuntimeException(message);
        }
    }
}