package com.divadvo.babbleboosternew.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.injection.ApplicationContext;
import com.divadvo.babbleboosternew.injection.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    DataManager apiManager();
}
