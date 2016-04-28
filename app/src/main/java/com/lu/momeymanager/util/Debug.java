package com.lu.momeymanager.util;
import android.util.Log;

/**
 * @author laichunling
 * @Package com.goopai.base
 * @Description: ${TODO}(日志输入打印类)
 * @date 2015/12/3 13:59
 */
public class Debug {

    /**
     * 日志打印开关
     */
    public static boolean ENABLE_DEBUG = true;

    static final boolean DEBUG = true;

    public static void w(String tag, Object msg) {
        log(tag, msg.toString(), 'w');
    }

    public static void e(String tag, Object msg) {
        log(tag, msg.toString(), 'e');
    }

    public static void d(String tag, Object msg) {
        log(tag, msg.toString(), 'd');
    }

    public static void i(String tag, Object msg) {
        log(tag, msg.toString(), 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 日志打印实现
     *
     * @param tag
     * @param msg
     * @param level
     */
    private static void log(String tag, String msg, char level) {
        // log print switcher
        if (ENABLE_DEBUG) {
            if ('e' == level) {
                Log.e(tag, msg);
            } else if ('w' == level) {
                Log.w(tag, msg);
            } else if ('d' == level) {
                Log.d(tag, msg);
            } else if ('i' == level) {
                Log.i(tag, msg);
            } else {
                Log.v(tag, msg);
            }
        }
    }


    public static void d(Class<?> class1, String msg) {
        if (DEBUG) {
            Log.d(class1.getSimpleName(), msg);
        }

    }
    public static void d(Object o, String msg) {
        if (DEBUG) {
            Log.d(o.getClass().getSimpleName(), msg);
        }

    }
    public static void d(Object o, String msg,Object... objects) {
        if (DEBUG) {
            Log.d(o.getClass().getSimpleName(), String.format(msg,objects));
        }

    }

    public static void e(Class<?> class1, String msg) {
        if (DEBUG) {
            Log.e(class1.getSimpleName(), msg);
        }
    }

    public static void i(Class<?> class1, String msg) {
        if (DEBUG) {
            Log.i(class1.getSimpleName(), msg);
        }
    }

    public static void v(Class<?> class1, String msg) {
        if (DEBUG) {
            Log.v(class1.getSimpleName(), msg);
        }
    }

    public static void w(Class<?> class1, String msg) {
        if (DEBUG) {
            Log.w(class1.getSimpleName(), msg);
        }
    }
}
