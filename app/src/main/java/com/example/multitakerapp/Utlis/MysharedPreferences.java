package com.example.multitakerapp.Utlis;

import android.content.Context;
import android.content.SharedPreferences;

public class MysharedPreferences {

    private static MysharedPreferences mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "loginSharedPref1";
    private static final String User_Id = "user_id";
    private static final String SELECT_ROLE_Info = "SELECT_ROLE_Info";
    private static final String User_Name = "user_name";
    private static final String User_Email = "user_email";
    private static final String Fcm_Id = "fcm_id";

    private MysharedPreferences(Context context) {
        mCtx = context;
    }

    public static synchronized MysharedPreferences getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MysharedPreferences(context);
        }
        return mInstance;
    }


    public void logOut() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECT_ROLE_Info, null);
        editor.apply();

    }

    public void PutSelectRoleInfo(String key, String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }


    public String getSelectRole(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }


}
