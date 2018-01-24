package com.divadvo.babbleboosternew.features.lock;

import com.divadvo.babbleboosternew.data.local.LocalUser;
import com.divadvo.babbleboosternew.data.local.PreferencesHelper;
import com.divadvo.babbleboosternew.data.local.User;
import com.divadvo.babbleboosternew.features.base.BasePresenter;
import com.divadvo.babbleboosternew.injection.ConfigPersistent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import timber.log.Timber;

@ConfigPersistent
public class LockPresenter extends BasePresenter<LockMvpView> {

    private PreferencesHelper preferencesHelper;

    @Inject
    public LockPresenter(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public void loginOffline(String password) {
        checkViewAttached();
        if (isCorrectPassword(password)) {
            getView().loginSuccessfulOffline(password);
        } else {
            getView().wrongPassword();
        }
    }

    private boolean isCorrectPassword(String password) {
        String usernameSaved = preferencesHelper.getString("username");

        if(doesLocalUserExist()) {
            return password.equals(usernameSaved);
        }
        else {
            // Nothing saved
            return false;
        }
    }

    public boolean doesLocalUserExist() {
        String usernameSaved = preferencesHelper.getString("username");
//        String usernameSaved = LocalUser.getInstance().username;
        return !usernameSaved.equals("");
    }

    private void signInAnonymously() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Timber.i("Success");
            } else {
                Timber.i("Failed");
            }
        });
    }

    public void loginOnline(String password) {
        checkViewAttached();

        signInAnonymously();

        // Get the document from firestore under "users" collection
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(password);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                // If this doc exists, means that the user exists
                // ie the password was correct
                if (document != null && document.exists()) {
                    Timber.d("DocumentSnapshot data: " + task.getResult().getData());
                    User user = document.toObject(User.class);
                    // Save user in local preferences
                    preferencesHelper.saveUser(user);
//                    LocalUser.setInstance(user);

                    getView().savedUserInLocal(password);
                } else {
                    Timber.d( "No such document");
                    getView().wrongPassword();
                }
            } else {
                Timber.d( "get failed with ", task.getException());
            }
        });
    }

    public void loginOnlineEmail(String password) {
        checkViewAttached();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = password + "@babblebooster.com";

        mAuth.signInWithEmailAndPassword(email, email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
//                FirebaseUser user = mAuth.getCurrentUser();
                getView().savedUserInLocal(password);
            } else {
                Timber.e(task.getException());
                getView().wrongPassword();
            }
        });

    }

    public void loadUser() {
        preferencesHelper.loadUser();
    }
}
