package com.divadvo.babbleboosternew.common.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.data.remote.PokemonService;
import com.divadvo.babbleboosternew.injection.ApplicationContext;

import static org.mockito.Mockito.mock;

/**
 * Provides application-level dependencies for an app running on a testing environment This allows
 * injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {
    private final Application application;

    public ApplicationTestModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    /**
     * ********** MOCKS ***********
     */
    @Provides
    @Singleton
    DataManager providesDataManager() {
        return mock(DataManager.class);
    }

    @Provides
    @Singleton
    PokemonService provideMvpBoilerplateService() {
        return mock(PokemonService.class);
    }
}
