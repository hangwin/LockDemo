package com.hang.study.lockdemo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hang on 16/8/4.
 */
public class SPUtil {
    public static void setString(Context context,String key,String val) {
        SharedPreferences sp=context.getSharedPreferences("lockPassword",Context.MODE_PRIVATE);
        sp.edit().putString(key,val).commit();
    }
    public static String getString(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences("lockPassword",Context.MODE_PRIVATE);
        return sp.getString(key,null);
    }
    public static void setBoolean(Context context,String key,boolean val) {
        SharedPreferences sp=context.getSharedPreferences("lockPassword",Context.MODE_PRIVATE);
        sp.edit().putBoolean("isLock",val).commit();
    }
    public static boolean getBoolean(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences("lockPassword",Context.MODE_PRIVATE);
        return  sp.getBoolean("isLock",false);
    }
}
