package com.nurbk.ps.photosfromtheworldprivate.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    /*
     * SharedPreference file name
     */
    private static final String PREF_FILE_NAME = "Preference";

    /*
     * Key used to save the token value
     */
    private static final String PREF_TOKEN = "token";

    /**
     * @param context Context used to get the SharedPreferences
     * @return previously saved token value if exist, null otherwise!
     */
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_TOKEN, null);
    }

    /**
     * Helper method to handle setting token in preferences
     *
     * @param context Context used to get the SharedPreferences
     * @param token   token value to be saved
     */
    public static void setToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_TOKEN, token).apply();
    }

}
