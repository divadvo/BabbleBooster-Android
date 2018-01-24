package com.divadvo.babbleboosternew.features.testChoose;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.data.local.DbManager;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class TestChoosePresenter extends BasePresenter<TestChooseMvpView> {
    private final DataManager dataManager;
    private final DbManager dbManager;

    @Inject
    public TestChoosePresenter(DataManager dataManager, DbManager dbManager) {
        this.dataManager = dataManager;
        this.dbManager = dbManager;
    }

    public int calculateNumberOfAttemptsRemaining(String phoneme) {
        return dbManager.calculateNumberOfAttemptsRemaining(phoneme);
    }
}
