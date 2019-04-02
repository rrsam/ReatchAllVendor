package com.reatchall.charan.reatchallVendor.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by NaNi on 07/09/17.
 */

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREFERNCE_NAME = "REATCHALLVENDOR";

    private static final String VENDOR_ID = "VENDOR_ID";
    private static final String MANAGER_ID = "MANAGER_ID";
    private static final String MANAGER_PHN_NUM = "MANAGER_PHN_NUM";

    private static final String NOTIFICATION_COUNT = "NOTIFICATION_COUNT";

    private static final String FCM_TOKEN = "fcm_token";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String FORGOT_PASS = "forgotPass";

    private static final String IS_PROFILE_COMPLETE = "PROFILE";
    private static final String CUSORBUS = "CUSORBUS";
    private static final String MANAGER_LOGGED_IN = "MANAGER_LOGGED_IN";
    private static final String VENDOR_LOGGED_IN = "VENDOR_LOGGED_IN";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFERNCE_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public static void setManagerLoggedIn(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(MANAGER_LOGGED_IN, true);
        editor.commit();
    }

    public static boolean isManagerLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(MANAGER_LOGGED_IN, false);
    }


    public static void setVendorLoggedIn(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(VENDOR_LOGGED_IN, true);
        editor.commit();
    }

    public static boolean isVendorLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(VENDOR_LOGGED_IN, false);
    }

    public static void setVerified(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("verified", true);
        editor.commit();
    }
    public static void reSetVerified(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("verified", false);
        editor.commit();
    }

    public static boolean isVerified(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean("verified", false);
    }

    public static void setNotification(Context context, int count){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(NOTIFICATION_COUNT, count);
        editor.commit();
    }

    public static int getNotificationCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(NOTIFICATION_COUNT, 0);
    }

    public static void setFcmToken(Context context, String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(FCM_TOKEN, token);
        editor.commit();
    }

    public static String getFcmToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(FCM_TOKEN, null);
    }

    public static void setLogout(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("profileDone", false);
        editor.commit();
    }

    public static void clearAll(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
    public static void removesharedPreferenceKey(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERNCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getValueFromSharedPreference(Context context,
                                                      String key) {

        SharedPreferences preferences = context.getSharedPreferences(
                PREFERNCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static boolean setValueInSharedPreference(Context context,
                                                     String key, String value) {

        SharedPreferences preferences = context.getSharedPreferences(
                PREFERNCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (!TextUtils.isEmpty(value))
            editor.putString(key, value);
        else
            editor.putString(key, "");
        return editor.commit();
    }

    public void setVendorId(String candidateId, Context context){
        editor.putString(VENDOR_ID,candidateId);
        editor.commit();
    }

    public  String getVendorId(Context context){
        return pref.getString(VENDOR_ID,null);
    }

    public void setManagerPhnNum(String candidateId, Context context){
        editor.putString(MANAGER_PHN_NUM,candidateId);
        editor.commit();
    }

    public  String getManagerPhnNum(Context context){
        return pref.getString(MANAGER_PHN_NUM,null);
    }

    public void setManagerId(String candidateId, Context context){
        editor.putString(MANAGER_ID,candidateId);
        editor.commit();
    }

    public  String getManagerId(Context context){
        return pref.getString(MANAGER_ID,null);
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {

        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setForgotPass(boolean isFirstTime) {
        editor.putBoolean(FORGOT_PASS, isFirstTime);
        editor.commit();
    }

    public boolean isForgotPass() {
        return pref.getBoolean(FORGOT_PASS, false);
    }

    public void setCusorbus(boolean isFirstTime) {
        editor.putBoolean(CUSORBUS, isFirstTime);
        editor.commit();
    }

    public boolean isCusorbus() {
        return pref.getBoolean(CUSORBUS, false);
    }


    public void setIsProfileComplete(boolean isFirstTime) {
        editor.putBoolean(IS_PROFILE_COMPLETE, isFirstTime);
        editor.commit();
    }

    public boolean  getIsProfileComplete() {
        return pref.getBoolean(IS_PROFILE_COMPLETE,false);
    }


    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }



}


