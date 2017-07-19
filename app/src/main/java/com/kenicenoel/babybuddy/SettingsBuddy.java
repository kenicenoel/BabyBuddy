package com.kenicenoel.babybuddy;

import android.content.Context;
import android.content.SharedPreferences;


public class SettingsBuddy
{
    private static SettingsBuddy settingsBuddy;
    private SharedPreferences settings;

    private String DEFAULT = "N/A";
//    private String NO_WEATHER_MAIN = "Weather not updated yet";
//    private String NO_WEATHER_DESCRIPTION = "The weather will be updated every hour for your current location";
//    private final String NO_WEATHER_TEMPERATURE = "0" ;


    private final String TAG = SettingsBuddy.class.getSimpleName();

    public static SettingsBuddy getInstance(Context context)
    {
        if (settingsBuddy == null)
        {
            return new SettingsBuddy(context);
        }

        return settingsBuddy;
    }


    private SettingsBuddy(Context context)
    {

        settings = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

    }


    public void saveData(String key, String value)
    {
        SharedPreferences.Editor prefsEditor = settings.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key)
    {
        if (settings!= null)
        {
            return settings.getString(key, DEFAULT);
        }
        return DEFAULT;
    }





}
