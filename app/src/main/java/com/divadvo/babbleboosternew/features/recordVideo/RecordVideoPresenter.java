package com.divadvo.babbleboosternew.features.recordVideo;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class RecordVideoPresenter extends BasePresenter<RecordVideoMvpView> {

    private StorageHelper storageHelper;

    @Inject
    public RecordVideoPresenter(StorageHelper storageHelper) {
        this.storageHelper = storageHelper;
    }

    public String getVideoFolder() {
        return storageHelper.getAttemptFolder();
    }

    public String getReinforcementVideo(String name) {
        return storageHelper.getReinforcementVideo(name);
    }
}
