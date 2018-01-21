package com.divadvo.babbleboosternew.features.customizeStimuli;

import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

@ConfigPersistent
public class CustomizeStimuliPresenter extends BasePresenter<CustomizeStimuliMvpView> {

    private StorageHelper storageHelper;

    @Inject
    public CustomizeStimuliPresenter(StorageHelper storageHelper) {
        this.storageHelper = storageHelper;
    }

    public List<File> getAllImagesForPhoneme(String phoneme) {
        return storageHelper.getAllImagesForPhoneme(phoneme);
    }

    public List<File> getAllVideosForPhoneme(String phoneme) {
        return storageHelper.getAllVideosForPhoneme(phoneme);
    }

    public File getStimuliFileFromExternal(String phoneme, File file) {
        String directoryCurrentPhoneme = storageHelper.getPhonemeFolder(phoneme);
        File stimuliFile = new File(directoryCurrentPhoneme, file.getName());
        return stimuliFile;
    }

    public void copyFile(File from, File to) {
        storageHelper.copyAndOverwriteFile(from, to);
    }

    public void addToIgnoreFile(String phoneme, String path) {
        storageHelper.addToIgnoreFile(phoneme, path);
    }

    public void deleteFile(String path) {
        storageHelper.deleteFile(path);
    }
}
