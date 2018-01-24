package com.divadvo.babbleboosternew.features.lock;

import com.divadvo.babbleboosternew.features.base.MvpView;

public interface LockMvpView extends MvpView {

    void loginSuccessfulOffline(String password);

    void savedUserInLocal(String password);

    void wrongPassword();

    void displayStatus(int numberRemaining);

    void tryStartingHomeButWaitUntilFinished();

}
