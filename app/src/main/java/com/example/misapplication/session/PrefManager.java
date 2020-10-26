package com.example.misapplication.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Context context;
    //shared pref mode
    private int PRIVATE_MODE=0;
    //shared preferences file name
    private static final String PREF_NAME="attendence";

    private static final String IS_FIRST_TIME_LAUNCH="IsFirstTimeLaunch";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context){
        this.context=context;
        preferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=preferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){

        editor.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstTime);
        editor.commit();

    }

    public boolean isFirstTimeLaunch(){

        return preferences.getBoolean(IS_FIRST_TIME_LAUNCH,true);

    }

    public void clearSession() {

        editor.clear();
        editor.commit();

    }
}
