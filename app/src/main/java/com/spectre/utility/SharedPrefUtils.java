package com.spectre.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefUtils {
    private static final String PREFERENCES_FILE = "reader_settings";

    public static String getPreference(Context context, String prefKey, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(prefKey, defValue);
    }

    public static void setPreference(Context context, String prefKey, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(prefKey, value);
        editor.commit();
    }

    public static boolean getPreference(Context context, String prefKey, boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(prefKey, defValue);
    }

    public static void setPreference(Context context, String prefKey, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(prefKey, value);
        editor.commit();
    }

    public static int getPreference(Context context, String prefKey, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(prefKey, defValue);
    }

    public static void setPreference(Context context, String prefKey, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(prefKey, value);
        editor.commit();
    }

    public static long getPreference(Context context, String prefKey, long defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(prefKey, defValue);
    }

    public static void setPreference(Context context, String prefKey, long value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(prefKey, value);
        editor.commit();
    }
    public static void clearAllPreference(Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }
}
