package com.divadvo.babbleboosternew.features.progress;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.data.local.DbManager;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class ProgressPresenter extends BasePresenter<ProgressMvpView> {
    private final DataManager dataManager;
    private final DbManager dbManager;

    @Inject
    public ProgressPresenter(DataManager dataManager, DbManager dbManager) {
        this.dataManager = dataManager;
        this.dbManager = dbManager;
    }

    public double getBestDayAverage(String phoneme) {
        return dbManager.getBestDayAverage(phoneme);
    }
}
