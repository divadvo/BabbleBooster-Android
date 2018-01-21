package com.divadvo.babbleboosternew.features.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.features.choosePhonemes.ChoosePhonemesActivity;
import com.divadvo.babbleboosternew.features.settingsChoose.SettingsChooseActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements HomeMvpView {

    @Inject
    HomePresenter homePresenter;

    @BindView(R.id.button_play)
    Button buttonPlay;

    @BindView(R.id.button_test)
    Button buttonTest;

    @BindView(R.id.button_settings)
    Button buttonSettings;

    @BindView(R.id.button_progress)
    Button buttonProgress;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
//        intent.putExtra(EXTRA_POKEMON_NAME, pokemonName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonPlay.setOnClickListener(v -> startActivity(ChoosePhonemesActivity.getStartIntent(this)));
    }

    @OnClick(R.id.button_settings)
    void startSettingsDialog() {
        new MaterialDialog.Builder(this)
                .title("Enter your password")
                .positiveText("OK")
                .negativeText("Cancel")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("Password", "ds2018", (dialog, input) -> {
                    String inputString = input.toString();
                    if(inputString.equals("ds2018"))
                        startSettingsActivity();
                }).show();
    }

    void startSettingsActivity() {
        startActivity(SettingsChooseActivity.getStartIntent(this));
    }

    @Override
    public int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        homePresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        homePresenter.detachView();
    }
}
