package com.divadvo.babbleboosternew.data.firebase;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.divadvo.babbleboosternew.Constants;
import com.divadvo.babbleboosternew.data.local.Attempt;
import com.divadvo.babbleboosternew.data.local.DbManager;
import com.divadvo.babbleboosternew.data.local.LocalUser;
import com.divadvo.babbleboosternew.data.local.RealmAttempt;
import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.divadvo.babbleboosternew.features.lock.LockActivity;
import com.divadvo.babbleboosternew.features.lock.LockMvpView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.snatik.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmResults;
import timber.log.Timber;

@Singleton
public class FirebaseSyncHelper {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private Storage storage;

    StorageHelper storageHelper;
    DbManager dbManager;
    private LockMvpView progressView;

    public volatile int tasksToFinish = 0;
    public ArrayList<String> tasks = new ArrayList<>();
    public AtomicInteger tasksToF = new AtomicInteger(0);

    @Inject
    public FirebaseSyncHelper(Context context, StorageHelper storageHelper, DbManager dbManager) {
        storage = new Storage(context);
        this.storageHelper = storageHelper;
        this.dbManager = dbManager;
    }

    public void downloadFromFirebase() {
        downloadPhonemes();
        downloadReinforcement();
    }

    private void downloadPhonemes() {
        db.collection("phonemeData")
        .get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressView.tryStartingHomeButWaitUntilFinished();

                for (DocumentSnapshot document : task.getResult()) {
                    Timber.d(document.getId() + " => " + document.getData());
                    downloadPhoneme(document);
                }
            } else {
                Timber.d("Error getting documents.", task.getException());
            }
        });
    }

    private void downloadPhoneme(DocumentSnapshot phoneme) {
        Map<String, Object> data = phoneme.getData();
        String phonemeString = data.get("phoneme").toString();
        String finalVideo = data.get("finalVideo").toString();
        String sound = data.get("sound").toString();

        ArrayList<String> images = (ArrayList<String>) data.get("images");
        Timber.i(images.toString());

        String folderPhoneme = storageHelper.getPhonemeFolder(phonemeString);
        String folderFinal = storageHelper.getPhonemeFolderFinal(phonemeString);
        downloadFile(folderFinal, finalVideo);
        downloadFile(folderFinal, sound);

        List<String> filesToIgnore = storageHelper.getListOfFilesToIgnore(phonemeString);

        for(String image : images) {
            downloadFile(folderPhoneme, image, filesToIgnore);
        }

    }

    private void downloadFile(String folder, String gsFileLocation, List<String> filesToIgnore) {
        String fileName = gsFileLocation.substring(gsFileLocation.lastIndexOf('/') + 1, gsFileLocation.length());

        // Only download if not previously deleted by the user
        if(!filesToIgnore.contains(fileName)) {
            downloadFile(folder, gsFileLocation);
        }

    }

    private void downloadFile(String folder, String gsFileLocation) {
        File folderF = new File(folder);
        String fileName = gsFileLocation.substring(gsFileLocation.lastIndexOf('/') + 1, gsFileLocation.length());

        File fileLocation = new File(folderF, fileName);



        StorageReference objectReference = firebaseStorage.getReferenceFromUrl(gsFileLocation);

        // TODO: check if doesn't exist
        if(!fileLocation.exists()) {
//            progressView.displayStatus("Downloading file: " + fileName);

            tasksToFinish++;
            tasksToF.incrementAndGet();
            progressView.displayStatus(tasksToF.get());

//            tasks.add(gsFileLocation);
            // Handle any errors
            objectReference.getFile(fileLocation).addOnSuccessListener(taskSnapshot -> {
                tasksToFinish--;
                tasksToF.decrementAndGet();
                progressView.displayStatus(tasksToF.get());
//                tasks.remove(gsFileLocation);
                // Local temp file has been created
            }).addOnFailureListener(Timber::e);
        }

    }

    private void downloadReinforcement() {
        db.collection("users").document("default")
        .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                downloadReinforcementFiles(document);
            } else {
                Timber.d( "Error getting documents.", task.getException());
            }
        });
    }

    private void downloadReinforcementFiles(DocumentSnapshot document) {
        Map<String, Object> data = document.getData();
        String videoYes = data.get("video_yes").toString();
        String videoGoodTry = data.get("video_good_try").toString();

        String folderReinforcement = storageHelper.getReinforcementFolder();
        downloadFile(folderReinforcement, videoYes);
        downloadFile(folderReinforcement, videoGoodTry);
    }

    public void uploadEverything() {
        uploadAttempts();
        uploadTests();
        uploadDatabase();
        uploadMastered();
    }

    private void uploadAttempts() {
        List<File> attemptVideos = storageHelper.getAllAttemptVideos();

        StorageReference storageRef = firebaseStorage.getReference();

        String username = LocalUser.getInstance().username;
        String folderFirebase = "attempts/" + username;

        for(File file : attemptVideos) {
            String fileFirebase = folderFirebase + "/" + file.getName();
            StorageReference fileReference = storageRef.child(fileFirebase);

            uploadFile(fileReference, file, fileFirebase);
        }
    }


    private void uploadTests() {
        List<File> testVideos = storageHelper.getAllTestVideos();

        StorageReference storageRef = firebaseStorage.getReference();

        String username = LocalUser.getInstance().username;
        String folderFirebase = "tests/" + username;

        for(File file : testVideos) {
            String fileFirebase = folderFirebase + "/" + file.getName();
            StorageReference fileReference = storageRef.child(fileFirebase);

            uploadFile(fileReference, file, fileFirebase);
        }
    }

    private void uploadDatabase() {
//        progressView.displayStatus("Uploading results");

        List<Attempt> allAttempts = dbManager.getAllAttempts();

//        CollectionReference collectionAttempts = db.collection("results").document(LocalUser.getInstance().username).collection("attempts");
        CollectionReference collectionAttempts = db.collection("results");

        List<Attempt> attemptsInDatabase = new ArrayList<>();

        collectionAttempts
        .get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Attempt attempt = document.toObject(Attempt.class);
                    attemptsInDatabase.add(attempt);
                }

                for(Attempt attempt : allAttempts) {
                    // Skip if already uploaded
                    if(attemptsInDatabase.contains(attempt))
                        continue;

                    collectionAttempts.add(attempt);
                }
            } else {
                Timber.d("Error getting documents: ", task.getException());
            }
        });
    }

    private void uploadMastered() {
//        progressView.displayStatus("Uploading Mastered");

        Map<String, Object> data = new HashMap<>();
        data.put("mastered_phonemes", LocalUser.getInstance().mastered_phonemes);
//        data.put("mastered_phonemes", Arrays.asList("b", "d", "t"));

        db.collection("users").document(LocalUser.getInstance().username)
                .set(data, SetOptions.merge());
    }

    private void uploadFile(StorageReference fileReference, File localFile, String fileFirebase) {
//        tasksToFinish++;
//        tasksToF.incrementAndGet();
//        tasks.add(fileFirebase);

        StorageReference storageRef = firebaseStorage.getReference();
//        fileReference
        storageRef.child(fileFirebase).getDownloadUrl().addOnSuccessListener(uri -> {
            // Got the download URL for 'users/me/profile.png'
            Timber.i("File exists" + localFile);
//            tasksToFinish--;
//            tasksToF.decrementAndGet();
//            tasks.remove(fileFirebase);
        }).addOnFailureListener(exception -> {
            Timber.e(exception);
            doesntExistSoUpload(fileReference, localFile, fileFirebase);
        });
    }

    private void doesntExistSoUpload(StorageReference fileReference, File localFile, String fileFirebase) {
        // File doesn't exist
        Timber.i("File doesn't exist: " + localFile);
        Uri file = Uri.fromFile(localFile);
//        UploadTask uploadTask = fileReference.putFile(file);

//        tasksToFinish++;
//        tasks.add(fileFirebase);
        tasksToF.incrementAndGet();
        Timber.i("tasksToF: " + tasksToF.get());
        progressView.displayStatus(tasksToF.get());

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference firebaseFile = storageRef.child(fileFirebase);
        UploadTask uploadTask = firebaseFile.putFile(file);

//        progressView.displayStatus("Uploading file: " + localFile.getName());

        uploadTask.addOnFailureListener(exception2 -> {
            // Handle unsuccessful uploads
            Timber.e("Upload failed", exception2);
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            tasksToFinish--;
            tasksToF.decrementAndGet();
            progressView.displayStatus(tasksToF.get());
//            tasks.remove(fileFirebase);
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            Timber.i("Upload successful", downloadUrl);
            if(Constants.DELETE_UPLOADED_VIDEOS) {
                storageHelper.deleteFile(localFile.getAbsolutePath());
            }
        });
    }

    public void setProgressView(LockMvpView progressView) {
        this.progressView = progressView;
    }
}
