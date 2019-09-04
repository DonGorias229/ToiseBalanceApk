package com.example.judeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {
    private static final String PREF_USER_LOGGED_IN = "prefUserLoggedIn";
    private static final String USER_NUMERO = "userNumero";
    private static final String IS_FIRST_TIME_LAUNCHED = "";
    //  public static final String CODE_VERIFIER = "";
    private static AppPreferences appPreferences;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private Context context;

    public AppPreferences(Context context) {
        this.context = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
    }

    static AppPreferences getInstance(Context context) {
        if (appPreferences == null)
            appPreferences = new AppPreferences(context);
        return appPreferences;
    }

    public static boolean getIsFirstLaunched() {
        return mSharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCHED, true);
    }

    public void setIsFirstLaunched(boolean isFirstTimeLaunched) {
        mEditor.putBoolean(IS_FIRST_TIME_LAUNCHED, isFirstTimeLaunched);
        mEditor.apply();
    }


    public static boolean isUserLoggedIn() {
        return mSharedPreferences.getBoolean(PREF_USER_LOGGED_IN, false);
    }

    public static void setUserLoggedIn(Boolean login) {
        mEditor.putBoolean(PREF_USER_LOGGED_IN, login);
        mEditor.commit();
    }


    public static String getUserNumero() {
        return mSharedPreferences.getString(USER_NUMERO, "");
    }

    public static void setUserNumero(String numero) {
        mEditor.putString(USER_NUMERO, numero);
        mEditor.commit();
    }


}