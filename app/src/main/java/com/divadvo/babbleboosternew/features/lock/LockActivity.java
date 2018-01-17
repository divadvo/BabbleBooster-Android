package com.divadvo.babbleboosternew.features.lock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

public class LockActivity extends BaseActivity implements LockMvpView {

    @Inject
    LockPresenter lockPresenter;

    @BindView(R.id.editText_password)
    EditText editTextPassword;

    @BindView(R.id.button_login)
    Button buttonLogin;

    @BindView(R.id.button_login_online)
    Button buttonLoginOnline;


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
    }

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
    public void switchToHomeScreen(String password) {
//        startActivity(DetailActivity.getStartIntent(this, "Pikachu"));
        Toast.makeText(this, "Logged in as " + password, Toast.LENGTH_SHORT).show();
    }
}
