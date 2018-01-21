package com.divadvo.babbleboosternew.features.choosePhonemes;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class ChoosePhonemesPresenter extends BasePresenter<ChoosePhonemesMvpView> {
    private final DataManager dataManager;

    @Inject
    public ChoosePhonemesPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }
}
