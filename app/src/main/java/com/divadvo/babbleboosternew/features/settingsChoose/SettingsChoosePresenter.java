package com.divadvo.babbleboosternew.features.settingsChoose;

import com.divadvo.babbleboosternew.data.local.PreferencesHelper;
import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class SettingsChoosePresenter extends BasePresenter<SettingsChooseMvpView> {

    private StorageHelper storageHelper;
    private PreferencesHelper preferencesHelper;

    @Inject
    public SettingsChoosePresenter(StorageHelper storageHelper, PreferencesHelper preferencesHelper) {
        this.storageHelper = storageHelper;
        this.preferencesHelper = preferencesHelper;
    }

    public void loadUser() {
        preferencesHelper.loadUser();
    }
}
