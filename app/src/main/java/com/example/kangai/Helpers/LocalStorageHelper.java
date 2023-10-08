package com.example.kangai.Helpers;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kangai.Application.Kangai;

public class LocalStorageHelper {

    private static final String PREFS_NAME = "MyAppPrefs";

    public static void setAccountCreatedFlag(Context context, boolean flag, String id) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("account_created", flag);
        editor.putString("account", id);
        editor.apply();
        Kangai.getInstance().setUserID(getAccount(context));
    }

    public static boolean isAccountCreated(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean("account_created", false);
    }
    public static String getAccount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("account", "");
    }

    /*
    * GENERAL HELPER FUNCTIONS
    * */

    public static void setPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void setPref(Context context, String key, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void setPref(Context context, String key, Integer value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getPref(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }
    public static Boolean getPref(Context context, String key, Boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultValue);
    }
    public static Integer getPref(Context context, String key, Integer defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, defaultValue);
    }

    public interface PrefNames{
        String NOTIFICATION_SMS = "NOTIFICATION_SMS";
        String NOTIFICATION_PUSHNOTIF = "NOTIFICATION_PUSHNOTIF";
        String NOTIFICATION_CATEG_STATES = "NOTIFICATION_CATEG_STATES";
        String NOTIFICATION_CATEG_WATERING = "NOTIFICATION_CATEG_WATERING";
        String NOTIFICATION_CATEG_PLANTUPDATES = "NOTIFICATION_CATEG_PLANTUPDATES";
    }
}
