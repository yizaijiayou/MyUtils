package com.example.myutils.utils.systemUtils;

import android.util.Log;

/**
 * Project name ElectroCar
 * Created by 哎呀呀！要努力打代码啦！··
 * on 2017/5/2 13:28.
 * 本类描述：Log 统一管理类
 */
public class L {

    private L(){
        throw  new UnsupportedOperationException("cannot be instantiated");
    }
    private final static boolean DEBUG = true;
    private final static String TGA = "L";

    public static void out(Object msg){
        if (DEBUG){
            System.out.println(msg);
        }
    }

    public static void d(String msg){
        if (DEBUG){
            Log.d(TGA,msg);
        }
    }

    public static void i(String msg){
        if (DEBUG){
            Log.i(TGA, msg);
        }
    }

    public static void v(String msg){
        if (DEBUG){
            Log.v(TGA, msg);
        }
    }

    public static void w(String msg){
        if (DEBUG){
            Log.w(TGA, msg);
        }
    }

    public static void e(String msg){
        if (DEBUG){
            Log.e(TGA, msg);
        }
    }

    /**
     * 带tag的函数
     * @param tag  tag标识
     * @param msg  显示内容
     */

    public static void d(String tag , String msg){
        if (DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag ,String msg){
        if (DEBUG){
            Log.i(tag, msg);
        }
    }

    public static void v(String tag ,String msg){
        if (DEBUG){
            Log.v(tag, msg);
        }
    }

    public static void w(String tag ,String msg){
        if (DEBUG){
            Log.w(tag, msg);
        }
    }

    public static void e(String tag ,String msg){
        if (DEBUG){
            Log.e(tag, msg);
        }
    }
}
