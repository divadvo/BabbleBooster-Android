package com.divadvo.babbleboosternew.features.lock;

import com.divadvo.babbleboosternew.features.base.MvpView;

public interface LockMvpView extends MvpView {

    void switchToHomeScreen(String password);

    void wrongPassword();
}
