package com.mia.phonetotablet.config;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigSharedPrefs {
    public static final String PREFS_CONFIG = "prefs_config";

    // App 초기화
    public static final String PREFS_KEY_YOUR_UUID = "your_uuid";

    private static SharedPreferences mSharedPrefs;
    private static ConfigSharedPrefs sConfig;

    private ConfigSharedPrefs(Context context) {
        mSharedPrefs = context.getSharedPreferences(
                ConfigSharedPrefs.PREFS_CONFIG, Context.MODE_PRIVATE);
    }

    public static ConfigSharedPrefs getConfig(Context context) {
        if (sConfig == null) {
            return new ConfigSharedPrefs(context);
        }
        return sConfig;
    }

    public boolean get(String key_name, boolean default_value) {
        return mSharedPrefs.getBoolean(key_name, default_value);
    }

    public int get(String key_name, int default_value) {
        return mSharedPrefs.getInt(key_name, default_value);
    }

    public long get(String key_name, long default_value) {
        return mSharedPrefs.getLong(key_name, default_value);
    }

    public String get(String key_name, String default_value) {
        return mSharedPrefs.getString(key_name, default_value);
    }

    public void set(String key_name, boolean value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(key_name, value);
        editor.commit();
    }

    public void set(String key_name, int value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putInt(key_name, value);
        editor.commit();
    }

    public void set(String key_name, long value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putLong(key_name, value);
        editor.commit();
    }

    public void set(String key_name, String value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(key_name, value);
        editor.commit();
    }
}
