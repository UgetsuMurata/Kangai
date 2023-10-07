package com.example.kangai.localStorage;
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

}
