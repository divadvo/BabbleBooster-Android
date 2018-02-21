package com.divadvo.babbleboosternew.features.home;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.data.local.PreferencesHelper;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeMvpView> {

    private PreferencesHelper preferencesHelper;

    @Inject
    public HomePresenter(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public void loadUser() {
        preferencesHelper.loadUser();
    }

}
