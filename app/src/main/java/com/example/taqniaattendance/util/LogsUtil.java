package com.example.taqniaattendance.util;

import android.util.Log;

import com.example.taqniaattendance.BuildConfig;

import timber.log.Timber;


public class LogsUtil {

    public static void printErrorLog(String tag, String value){
        if(value == null)
            value = "null";
        if(BuildConfig.DEBUG)
            Log.e(tag,value);
    }

    public static void printDebugLog(String tag, String value){
        if(value == null)
            value = "null";
        if(BuildConfig.DEBUG)
            Log.d(tag,value);
    }

//    /**
//     * Send warning to bugsnag with specific type of error code.
//     */
//    public static void notifyBugsnag(MorniError error){
//        if(error != null && error.getCode() != Constants.ErrorConstants.ERROR_CODE_NETWORK &&
//                error.getCode() != ErrorConstants.ERROR_CODE_TIME_OUT &&
//                error.getCode() != ErrorConstants.ERROR_CODE_UNAUTHORIZED) {
//            //todo Bugsnag.notify(new RuntimeException("Warning!! Error happens when calling the backend sever. Error info is: " + new Gson().toJson(error)));
//        }
//    }

}
