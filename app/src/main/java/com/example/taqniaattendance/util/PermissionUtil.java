package com.example.taqniaattendance.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.taqniaattendance.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eng. Abdulmajid Alyafey on 3/7/2018.
 */

public class PermissionUtil {

    public static void checkSMSPermission(Activity activity, int SMS_REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS
                }, SMS_REQUEST_CODE);
            }
        }
    }

    public static boolean isGalleryPermissionGranted(Activity activity) {
        return  ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCameraPermissionGranted(Activity activity) {
        return  ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCameraPermission(Activity activity , int Camera_REQUEST_PERMISSION_CODE) {
        ActivityCompat.requestPermissions(activity,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
                        },Camera_REQUEST_PERMISSION_CODE);
    }


    public static void requestGalleryPermission(Activity activity, int GALLERY_REQUEST_PERMISSION_CODE) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                },GALLERY_REQUEST_PERMISSION_CODE);
    }

    public static boolean isLocationPermissionGranted(Activity activity) {
        return  ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermission(Activity activity, int LOCATION_REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
        },LOCATION_REQUEST_CODE);
    }

    //------------------------ Phone Call Permission ------------------------
    public static boolean isPhonePermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return  ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPhonePermission(Activity activity , int PHONE_REQUEST_PERMISSION_CODE) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.CALL_PHONE
        },PHONE_REQUEST_PERMISSION_CODE);
    }

    public static void requestPhonePermissionFromFragment(Fragment fragment , int PHONE_REQUEST_PERMISSION_CODE) {
        fragment.requestPermissions(new String[]{
                Manifest.permission.CALL_PHONE
        },PHONE_REQUEST_PERMISSION_CODE);
    }
    //---------------------------------------------------------------------

    //---------------------Get List Of Granted Permission------------------------

    public static List<String> getGrantedPermissions() {
        List<String> granted = new ArrayList<String>();
        try {
            PackageInfo pi = App.Companion.getInstance().getPackageManager().getPackageInfo("com.kacst", PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
                }
            }
        } catch (Exception e) {
        }
        return granted;
    }
}
