package com.reatchall.charan.reatchallVendor.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Constants {
    //192.168.1.32
    //public static String BASE_URL ="http://192.168.1.23:2300/";
    //public static String SOCKET_URL ="http://192.168.1.7:1200";
    public static String BASE_URL = "https://webapi.reatchall.com/";
    public static String SOCKET_URL ="http://13.233.13.171:1200";


    public static String S3_ACCESS_KEY_ID = "AKIAI2DO5JE5JTKS7SHQ";
    public static String S3_SECRET_ACCESS_KEY = "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";
    public static String S3_BUCKET_CATEGORY ="reatchallimages";

    public static int getCurrentNotificationId(Context iContext){

        int NOTIFICATION_ID_UPPER_LIMIT = 30000; // Arbitrary number.
        int NOTIFICATION_ID_LOWER_LIMIT = 0;
        SharedPreferences sharedPreferences       = PreferenceManager.getDefaultSharedPreferences(iContext);
        int previousTokenId                       = sharedPreferences.getInt("currentNotificationTokenId", 0);
        int currentTokenId                        = previousTokenId+1;
        SharedPreferences.Editor editor           = sharedPreferences.edit();
        if(currentTokenId<NOTIFICATION_ID_UPPER_LIMIT) {
            editor.putInt("currentNotificationTokenId", currentTokenId); // }
        }else{
            //If reaches the limit reset to lower limit..
            editor.putInt("currentNotificationTokenId", NOTIFICATION_ID_LOWER_LIMIT);
        }
        editor.apply();
        return currentTokenId;
    }
}
