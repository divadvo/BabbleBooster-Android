package com.divadvo.babbleboosternew.features.lock;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.data.firebase.FirebaseSyncHelper;
import com.divadvo.babbleboosternew.data.local.PermissionsUtils;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.features.home.HomeActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class LockActivity extends BaseActivity implements LockMvpView {

    @Inject
    LockPresenter lockPresenter;

    @Inject
    FirebaseSyncHelper firebaseSyncHelper;

    @BindView(R.id.editText_password)
    EditText editTextPassword;

    @BindView(R.id.button_login)
    Button buttonLogin;

    @BindView(R.id.button_login_online)
    Button buttonLoginOnline;

//    @BindView(R.id.button_clear_data)
//    Button buttonClearData;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LockActivity.class);
//        intent.putExtra(EXTRA_POKEMON_NAME, pokemonName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buttonLogin.setOnClickListener(v -> {
            String enteredPassword = editTextPassword.getText().toString();
            lockPresenter.loginOffline(enteredPassword);
        });

        buttonLoginOnline.setOnClickListener(v -> {
            String enteredPassword = editTextPassword.getText().toString();
            lockPresenter.loginOnline(enteredPassword);
        });


        // Login disabled if not logged in online yet
        buttonLogin.setEnabled(lockPresenter.doesLocalUserExist());

        PermissionsUtils.requestAllPermissions(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Need all permissions", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

//    @OnClick(R.id.button_clear_data)
//    void clearData() {
//        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//            ((ActivityManager)getSystemService(ACTIVITY_SERVICE))
//                    .clearApplicationUserData(); // note: it has a return value!
//        } else {
//            // use old hacky way, which can be removed
//            // once minSdkVersion goes above 19 in a few years.
//        }
//    }

    @Override
    public int getLayout() {
        return R.layout.activity_lock;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        lockPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        lockPresenter.detachView();
    }

    @Override
    public void wrongPassword() {
        Timber.e("Wrong Password");
        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccessfulOffline(String password) {
        startActivity(HomeActivity.getStartIntent(this));
//        Toast.makeText(this, "Logged in as " + password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccessfulOnline(String password) {
        // Sync with firebase
        // Download, then upload
//        FirebaseSyncHelper firebaseSyncHelper = new FirebaseSyncHelper(this);
        firebaseSyncHelper.downloadFromFirebase();
        firebaseSyncHelper.uploadEverything();
    }
}
