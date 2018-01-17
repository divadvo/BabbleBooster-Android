package com.divadvo.babbleboosternew.injection.component;

import dagger.Subcomponent;
import com.divadvo.babbleboosternew.features.detail.DetailActivity;
import com.divadvo.babbleboosternew.features.lock.LockActivity;
import com.divadvo.babbleboosternew.features.main.MainActivity;
import com.divadvo.babbleboosternew.injection.PerActivity;
import com.divadvo.babbleboosternew.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(DetailActivity detailActivity);

    void inject(LockActivity lockActivity);
}
