package com.divadvo.babbleboosternew.features.progress;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.data.local.DbManager;
import com.divadvo.babbleboosternew.data.local.PreferencesHelper;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class ProgressPresenter extends BasePresenter<ProgressMvpView> {
    private PreferencesHelper preferencesHelper;
    private final DbManager dbManager;

    @Inject
    public ProgressPresenter(PreferencesHelper preferencesHelper, DbManager dbManager) {
        this.preferencesHelper = preferencesHelper;
        this.dbManager = dbManager;
    }

    public double getBestDayAverage(String phoneme) {
        return dbManager.getBestDayAverage(phoneme);
    }


    public void loadUser() {
        preferencesHelper.loadUser();
    }
}
