package com.divadvo.babbleboosternew.features.lock;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class LockPresenter extends BasePresenter<LockMvpView> {

    private final DataManager dataManager;

    @Inject
    public LockPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void loginOffline(String password) {
        checkViewAttached();
        if (isCorrectPassword(password)) {
            getView().switchToHomeScreen(password);
        }
        else {
            getView().wrongPassword();
        }
    }

    private boolean isCorrectPassword(String password) {
        return "ds2018".equals(password);
    }

    public void loginOnline(String password) {
        checkViewAttached();
        if (isCorrectPassword(password)) {
            getView().switchToHomeScreen(password);
        }
        else {
            getView().wrongPassword();
        }
    }
}
