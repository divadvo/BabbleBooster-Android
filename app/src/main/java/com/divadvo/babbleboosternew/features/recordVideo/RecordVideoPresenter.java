package com.divadvo.babbleboosternew.features.recordVideo;

import com.divadvo.babbleboosternew.data.DataManager;
import com.divadvo.babbleboosternew.data.local.Attempt;
import com.divadvo.babbleboosternew.data.local.DbManager;
import com.divadvo.babbleboosternew.data.local.LocalUser;
import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;

import java.io.File;

import javax.inject.Inject;

@ConfigPersistent
public class RecordVideoPresenter extends BasePresenter<RecordVideoMvpView> {

    private StorageHelper storageHelper;
    private DbManager dbManager;

    @Inject
    public RecordVideoPresenter(StorageHelper storageHelper, DbManager dbManager) {
        this.storageHelper = storageHelper;
        this.dbManager = dbManager;
    }

    public String getVideoFolder(boolean isTest) {
        if (isTest)
            return storageHelper.getTestFolder();
        else
            return storageHelper.getAttemptFolder();
    }

    public String getReinforcementVideo(String name) {
        return storageHelper.getReinforcementVideo(name);
    }

    public String newAttemptFilePath(String videoPath, String attemptNumberFull, boolean isTest) {
        return storageHelper.newAttemptFilePath(videoPath, attemptNumberFull, isTest);
    }

    public int getNextAttemptNumber(String phoneme, boolean isTest) {
        // Query DB
        int attemptNumber = dbManager.getNextAttemptNumber(phoneme, isTest);
        return attemptNumber;
    }

    public void saveAttemptInDatabase(String phoneme, boolean isTest, String originalFilePath, String response) {
        int attemptNumber = getNextAttemptNumber(phoneme, isTest);
        String attemptNumberFull = getFullAttemptString(phoneme, attemptNumber, isTest);
        String newPath = newAttemptFilePath(originalFilePath, attemptNumberFull, isTest);
        String newVideoFilename = new File(newPath).getName();

        Attempt attempt = new Attempt(LocalUser.getInstance().username, isTest, phoneme, attemptNumber, newVideoFilename, response, System.currentTimeMillis());
        // Save attempt in DB
        dbManager.saveAttempt(attempt);
    }



    private String getFullAttemptString(String phoneme, int attemptNumber, boolean isTest) {
        // Rename the recorded video to
        // [attemptnumber].[same_file_extension]

        // For example: tt2013_b_12
        String prefix = isTest ? "test" : "attempt";
        String fullAttempt = String.format("%s_%s_%s_%s", prefix, LocalUser.getInstance().username, phoneme, attemptNumber);
        return fullAttempt;
    }
}
