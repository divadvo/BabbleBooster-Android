package com.divadvo.babbleboosternew.features.home;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeMvpView> {

    private final DataManager dataManager;

    @Inject
    public HomePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


}
