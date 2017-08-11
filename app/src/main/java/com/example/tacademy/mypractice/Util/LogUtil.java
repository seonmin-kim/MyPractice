package com.example.tacademy.mypractice.Util;

import android.util.Log;

/**
 * Created by Tacademy on 2017-08-10.
 */

public class LogUtil {
    private static final LogUtil ourInstance = new LogUtil();

    static LogUtil getInstance() {
        return ourInstance;
    }

    private LogUtil() {
    }
    final static String TAG = "TAG";

    public static void log(String msg){
        Log.i(TAG, msg);
    }
}
