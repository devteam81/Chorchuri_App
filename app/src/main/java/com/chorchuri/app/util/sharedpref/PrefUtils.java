package com.chorchuri.app.util.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static PrefUtils mPrefUtils;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    private PrefUtils() {
    }

    private PrefUtils(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized PrefUtils getInstance(Context context) {

        if (mPrefUtils == null) {
            mPrefUtils = new PrefUtils(context.getApplicationContext());
        }
        return mPrefUtils;
    }


    public void setValue(String key, String value) {
        synchronized (this) {
            mSharedPreferencesEditor.putString(key, value);
            mSharedPreferencesEditor.commit();
        }
    }


    public void setValue(String key, int value) {
        synchronized (this) {
            mSharedPreferencesEditor.putInt(key, value);
            mSharedPreferencesEditor.commit();
        }
    }

    public void setValue(String key, double value) {
        synchronized (this) {
            setValue(key, Double.toString(value));
        }
    }

    public void setValue(String key, long value) {
        synchronized (this) {
            mSharedPreferencesEditor.putLong(key, value);
            mSharedPreferencesEditor.commit();
        }
    }

    public void setValue(String key, boolean value) {
        synchronized (this) {
            mSharedPreferencesEditor.putBoolean(key, value);
            mSharedPreferencesEditor.commit();
        }
    }

    public String getStringValue(String key, String defaultValue) {
        synchronized (this) {
            return mSharedPreferences.getString(key, defaultValue);
        }
    }

    public int getIntValue(String key, int defaultValue) {
        synchronized (this) {
            return mSharedPreferences.getInt(key, defaultValue);
        }
    }

    public long getLongValue(String key, long defaultValue) {
        synchronized (this) {
            return mSharedPreferences.getLong(key, defaultValue);
        }
    }

    public boolean getBoolanValue(String keyFlag, boolean defaultValue) {
        synchronized (this) {
            return mSharedPreferences.getBoolean(keyFlag, defaultValue);
        }
    }

    public void removeKey(String key) {
        synchronized (this) {
            if (mSharedPreferencesEditor != null) {
                mSharedPreferencesEditor.remove(key);
                mSharedPreferencesEditor.commit();
            }
        }
    }

    public void clear() {
        synchronized (this) {
            mSharedPreferencesEditor.clear().commit();
        }
    }
}