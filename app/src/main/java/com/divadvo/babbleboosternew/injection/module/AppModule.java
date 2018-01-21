package com.divadvo.babbleboosternew.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

import com.divadvo.babbleboosternew.data.firebase.FirebaseSyncHelper;
import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.divadvo.babbleboosternew.injection.ApplicationContext;
import com.snatik.storage.Storage;

import javax.inject.Singleton;

import static com.divadvo.babbleboosternew.Constants.PREF_FILE_NAME;

@Module(includes = {ApiModule.class})
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreference(@ApplicationContext Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    StorageHelper provideStorageHelper(@ApplicationContext Context context) {
        return new StorageHelper(context);
    }

    @Provides
    @Singleton
    FirebaseSyncHelper provideFirebaseSyncHelper(@ApplicationContext Context context) {
        return new FirebaseSyncHelper(context, provideStorageHelper(context));
    }
}
