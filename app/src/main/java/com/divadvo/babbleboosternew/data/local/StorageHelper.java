package com.divadvo.babbleboosternew.data.local;

import android.content.Context;

import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class StorageHelper {

    public static final String VIDEO_REGEX = "([^\\s]+(\\.(?i)(mp4|3gp))$)";
    public static final String IMAGE_REGEX = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
    private Storage storage;

    private String rootAppInternalFolder;

    public StorageHelper(Context context) {
        storage = new Storage(context);
        rootAppInternalFolder = storage.getInternalFilesDirectory();
    }

    // Example: root/phonemes/p
    public String getPhonemeFolder(String phoneme) {
        String folder = rootAppInternalFolder + File.separator + "phonemes" + File.separator + phoneme;
        storage.createDirectory(folder);
        return folder;
    }

    // Example: root/phonemes/p/final
    public String getPhonemeFolderFinal(String phoneme) {
        String folder = getPhonemeFolder(phoneme) + File.separator + "final";
        storage.createDirectory(folder);
        return folder;
    }

    // root/attempts
    public String getAttemptFolder() {
        String folder = rootAppInternalFolder + File.separator + "attempts";
        storage.createDirectory(folder);
        return folder;
    }

    // root/tests
    public String getTestFolder() {
        String folder = rootAppInternalFolder + File.separator + "tests";
        storage.createDirectory(folder);
        return folder;
    }

    public String getReinforcementFolder() {
        String folder = rootAppInternalFolder + File.separator + "reinforcement";
        storage.createDirectory(folder);
        return folder;
    }

    public String getReinforcementVideo(String type) {
        File reinforcementFolder = new File(getReinforcementFolder());
        for (File videoFile : reinforcementFolder.listFiles()) {
            if (videoFile.getName().toUpperCase().contains(type)) {
                return videoFile.getAbsolutePath();
            }
        }
        return null;
    }

    public String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public void copyAndOverwriteFile(File from, File to) {
        storage.deleteFile(to.getAbsolutePath());
        storage.copy(from.getAbsolutePath(), to.getAbsolutePath());
    }

    public void deleteFile(String filePath) {
        storage.deleteFile(filePath);
    }

    public List<File> getAllAttemptVideos() {
        String regex = VIDEO_REGEX;
        String path = getAttemptFolder();
        List<File> files = storage.getFiles(path, regex);
        return files;
    }

    public List<File> getAllTestVideos() {
        String regex = VIDEO_REGEX;
        String path = getTestFolder();
        List<File> files = storage.getFiles(path, regex);
        return files;
    }

    public List<File> getAllImagesForPhoneme(String phoneme) {
        String regex = IMAGE_REGEX;
        String path = getPhonemeFolder(phoneme);
        List<File> files = storage.getFiles(path, regex);
        return files;
    }

    public List<File> getAllVideosForPhoneme(String phoneme) {
        String regex = VIDEO_REGEX;
        String path = getPhonemeFolder(phoneme);
        List<File> files = storage.getFiles(path, regex);
        return files;
    }

    private String ignoreFilePath(String phoneme) {
        String folder = getPhonemeFolder(phoneme);
        String ignoreFilePath = folder + File.separator + "ignore.txt";
        return ignoreFilePath;

    }

    public void addToIgnoreFile(String phoneme, String filePath) {
        String ignoreFilePath = ignoreFilePath(phoneme);
        String fileName = new File(filePath).getName();
        if(!storage.isFileExist(ignoreFilePath))
            storage.createFile(ignoreFilePath, "");

        storage.appendFile(ignoreFilePath, fileName);

    }

    public List<String> getListOfFilesToIgnore(String phoneme) {
        String ignoreFilePath = ignoreFilePath(phoneme);
        if(!storage.isFileExist(ignoreFilePath))
            return new ArrayList<>();
        String content = storage.readTextFile(ignoreFilePath);
        String lines[] = content.split("\\r?\\n");
        List<String> linesList = Arrays.asList(lines);
        return linesList;
    }

    public String newAttemptFilePath(String videoPath, String attemptName, boolean isTest) {
        File originalFile = new File(videoPath); // with file://
        String newFilename = attemptName + "." + getFileExtension(originalFile);
        String folder = isTest ? getTestFolder() : getAttemptFolder();

        File originalFileWithoutFile = new File(folder, originalFile.getName());
        File newFile = new File(folder, newFilename);
        originalFileWithoutFile.renameTo(newFile);
//        storage.move(originalFile.getAbsolutePath(), newFile.getAbsolutePath());
        return newFile.getAbsolutePath();
    }
}
