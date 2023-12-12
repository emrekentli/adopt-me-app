package com.emrekentli.adoptme.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TokenManager {
    private static final String TOKEN_KEY = "token_key";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void deleteToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply();
    }
}
