package com.divadvo.babbleboosternew.features.customizeReinforcement;

import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import java.io.File;

import javax.inject.Inject;

@ConfigPersistent
public class CustomizeReinforcementPresenter extends BasePresenter<CustomizeReinforcementMvpView> {

    private StorageHelper storageHelper;

    @Inject
    public CustomizeReinforcementPresenter(StorageHelper storageHelper) {
        this.storageHelper = storageHelper;
    }

    public String getReinforcementVideo(String type) {
        return storageHelper.getReinforcementVideo(type);
    }

    public String getReinforcementFolder() {
        return storageHelper.getReinforcementFolder();
    }

    public String getFileExtension(File file) {
        return storageHelper.getFileExtension(file);
    }

    public void copyFile(File from, File to) {
        storageHelper.copyAndOverwriteFile(from, to);
    }
}
