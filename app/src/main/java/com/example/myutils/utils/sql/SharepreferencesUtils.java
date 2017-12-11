package com.example.myutils.utils.sql;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.myutils.R;
import com.example.myutils.base.BaseApplication;

import java.util.Set;

public class SharepreferencesUtils {
    private static SharepreferencesUtils sharepreferencesUtils;

    public static SharepreferencesUtils getInstance() {
        if (sharepreferencesUtils == null)
            sharepreferencesUtils = new SharepreferencesUtils();
        return sharepreferencesUtils;
    }

    private SharepreferencesUtils() {
        Context context = BaseApplication.getAppContext();
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //------------------------------------------------------------------put------------------------------------------------------------------------
    public void putString(String key, String value) {
        editor.putString(key, value);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
    }

    public void putStirngSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
    }

    //------------------------------------------------------------------get------------------------------------------------------------------------
    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public Set<String> getStirngSet(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void apply() {
        editor.apply();
    }

    public void commit() {
        editor.commit();
    }
}
