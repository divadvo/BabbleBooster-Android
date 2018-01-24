package com.divadvo.babbleboosternew.data.local;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "mvpstarter_pref_file";

    private final SharedPreferences preferences;
    private final DbManager dbManager;

    @Inject
    PreferencesHelper(SharedPreferences sharedPreferences, DbManager dbManager) {
        preferences = sharedPreferences;
        this.dbManager = dbManager;
    }

    public void putString(@Nonnull String key, @Nonnull String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(@Nonnull String key) {
        return preferences.getString(key, "");
    }

    public void putBoolean(@Nonnull String key, @Nonnull boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(@Nonnull String key) {
        return preferences.getBoolean(key, false);
    }

    public void putInt(@Nonnull String key, @Nonnull boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public int getInt(@Nonnull String key) {
        return preferences.getInt(key, -1);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void saveUser(User user) {
        String json = new Gson().toJson(user);
        preferences.edit().putString("User", json).apply();
    }

    public void loadUser() {
        String json = preferences.getString("User", "");
        User user = new Gson().fromJson(json, User.class);
        LocalUser.setInstance(user);

        LocalUser.getInstance().mastered_phonemes = dbManager.recalculateMasteredPhonemes();
        saveUser(LocalUser.getInstance());
    }
}
