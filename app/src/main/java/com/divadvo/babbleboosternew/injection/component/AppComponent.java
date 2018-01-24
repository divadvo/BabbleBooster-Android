package com.divadvo.babbleboosternew.injection.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.data.firebase.FirebaseSyncHelper;
import com.divadvo.babbleboosternew.data.local.DbManager;
import com.divadvo.babbleboosternew.data.local.PreferencesHelper;
import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.divadvo.babbleboosternew.injection.ApplicationContext;
import com.divadvo.babbleboosternew.injection.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    DataManager apiManager();

    PreferencesHelper preferencesHelper();

    StorageHelper storageHelper();

    FirebaseSyncHelper firebaseSyncHelper();

    DbManager dbManager();
}
