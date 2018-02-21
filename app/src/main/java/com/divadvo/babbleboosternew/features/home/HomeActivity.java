package com.divadvo.babbleboosternew.features.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.data.local.LocalUser;
import com.divadvo.babbleboosternew.data.local.User;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.features.choosePhonemes.ChoosePhonemesActivity;
import com.divadvo.babbleboosternew.features.progress.ProgressActivity;
import com.divadvo.babbleboosternew.features.settingsChoose.SettingsChooseActivity;
import com.divadvo.babbleboosternew.features.testChoose.TestChooseActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

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


        enableSomeButtons();

        buttonPlay.setOnClickListener(v -> startActivity(ChoosePhonemesActivity.getStartIntent(this)));

        buttonTest.setOnClickListener(v -> startActivity(TestChooseActivity.getStartIntent(this)));

        buttonProgress.setOnClickListener(v -> startActivity(ProgressActivity.getStartIntent(this)));
    }

    private void enableSomeButtons() {
        homePresenter.loadUser();
        User user = LocalUser.getInstance();
        Date today = new Date();

        if(today.after(user.a_start_date) && isTodayTestDay(today, user.test_dates)) {
            buttonTest.setEnabled(true);
        }

        if(today.after(user.b_start_date)) {
            buttonPlay.setEnabled(true);
        }
    }

    private boolean isTodayTestDay(Date today, ArrayList<Date> testDates) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        for(Date testDate : testDates) {
            // is same day?
            if(fmt.format(testDate).equals(fmt.format(today)))
                return true;
        }
        return false;
    }

    @OnClick(R.id.button_settings)
    void startSettingsDialog() {
        new MaterialDialog.Builder(this)
                .title("Enter your password")
                .positiveText("OK")
                .negativeText("Cancel")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("Password", "", (dialog, input) -> {
                    String inputString = input.toString();
                    if(inputString.equals(LocalUser.getInstance().username))
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
