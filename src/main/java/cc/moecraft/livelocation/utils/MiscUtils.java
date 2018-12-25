package cc.moecraft.livelocation.utils;

import org.eclipse.jetty.util.log.Logger;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class MiscUtils
{
    public static void disableLogging()
    {
        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");
        System.setProperty("org.eclipse.jetty.LEVEL", "OFF");

        org.eclipse.jetty.util.log.Log.setLog(new NoLogging());
    }

    public static class NoLogging implements Logger
    {
        @Override public String getName() { return "false"; }
        @Override public void warn(String msg, Object... args) { }
        @Override public void warn(Throwable thrown) { thrown.printStackTrace(); }
        @Override public void warn(String msg, Throwable thrown) { System.err.println(msg); thrown.printStackTrace(); }
        @Override public void info(String msg, Object... args) { }
        @Override public void info(Throwable thrown) { }
        @Override public void info(String msg, Throwable thrown) { }
        @Override public boolean isDebugEnabled() { return false; }
        @Override public void setDebugEnabled(boolean enabled) { }
        @Override public void debug(String msg, Object... args) { }
        @Override public void debug(String msg, long value) { }
        @Override public void debug(Throwable thrown) { }
        @Override public void debug(String msg, Throwable thrown) { }
        @Override public Logger getLogger(String name) { return this; }
        @Override public void ignore(Throwable ignored) { }
    }

}
