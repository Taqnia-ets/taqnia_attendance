package com.example.taqniaattendance.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.taqniaattendance.App;

public class NetworkUtil {

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            return (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

}
