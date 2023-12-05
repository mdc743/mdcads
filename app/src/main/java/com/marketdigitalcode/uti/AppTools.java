package com.marketdigitalcode.uti;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class AppTools {


    public static void setLog(String str) {
        if (!TextUtils.isEmpty(str)) {
            Log.d(AppConstant.TAG_LOG, str);
        }
    }
    public static boolean checkPermissions(Activity activity,String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
