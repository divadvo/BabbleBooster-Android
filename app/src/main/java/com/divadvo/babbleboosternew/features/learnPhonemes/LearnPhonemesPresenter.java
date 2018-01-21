package com.divadvo.babbleboosternew.features.learnPhonemes;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class LearnPhonemesPresenter extends BasePresenter<LearnPhonemesMvpView> {
    private final DataManager dataManager;

    @Inject
    public LearnPhonemesPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }
}
