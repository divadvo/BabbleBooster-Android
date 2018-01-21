package com.divadvo.babbleboosternew.data.firebase;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.divadvo.babbleboosternew.data.local.StorageHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class FirebaseSyncHelper {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private Storage storage;

    StorageHelper storageHelper;

    @Inject
    public FirebaseSyncHelper(Context context, StorageHelper storageHelper) {
        storage = new Storage(context);
        this.storageHelper = storageHelper;
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
            // Handle any errors
            objectReference.getFile(fileLocation).addOnSuccessListener(taskSnapshot -> {
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
    }

    private void uploadAttempts() {
        List<File> attemptVideos = storageHelper.getAllAttemptVideos();

        StorageReference storageRef = firebaseStorage.getReference();

        String username = "ds2018";
        String folderFirebase = "attempts/" + username;

        for(File file : attemptVideos) {
            String fileFirebase = folderFirebase + "/" + file.getName();
            StorageReference fileReference = storageRef.child(fileFirebase);

            uploadFile(fileReference, file);
        }
    }

    private void uploadTests() {
        List<File> testVideos = storageHelper.getAllTestVideos();

        StorageReference storageRef = firebaseStorage.getReference();

        String username = "ds2018"; // TODO
        String folderFirebase = "tests/" + username;

        for(File file : testVideos) {
            String fileFirebase = folderFirebase + "/" + file.getName();
            StorageReference fileReference = storageRef.child(fileFirebase);

            uploadFile(fileReference, file);
        }
    }

    private void uploadDatabase() {

    }

    private void uploadFile(StorageReference fileReference, File localFile) {

        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Timber.i("File exists");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // File doesn't exist
                Timber.i("File doesn't exist");
                Uri file = Uri.fromFile(localFile);
                UploadTask uploadTask = fileReference.putFile(file);

                uploadTask.addOnFailureListener(exception2 -> {
                    // Handle unsuccessful uploads
                }).addOnSuccessListener(taskSnapshot -> {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                });
            }
        });
    }
}
