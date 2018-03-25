package com.divadvo.babbleboosternew.features.testChoose;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.data.local.LocalUser;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.features.recordVideo.RecordVideoActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TestChooseActivity extends BaseActivity implements TestChooseMvpView {

    @Inject
    TestChoosePresenter testChoosePresenter;

    @BindView(R.id.layout_phoneme_test_buttons)
    LinearLayout linearLayout;

    private List<String> phonemes;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, TestChooseActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phonemes = new ArrayList<>();
        phonemes.addAll(LocalUser.getInstance().getCurrentPhonemes());
        phonemes.addAll(LocalUser.getInstance().mastered_phonemes);

//        phonemes = LocalUser.getInstance().getCurrentPhonemes();

        generateButtons();
    }

    private void generateButtons() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        layoutParams.setMargins(24, 0, 24, 0);
        ButtonClickListener buttonClickListener = new ButtonClickListener();

        int i = 0;
        for (String phoneme : phonemes) {
//            if(i == 3)
//                break;

            int numberOfAttemptsRemaining = testChoosePresenter.calculateNumberOfAttemptsRemaining(phoneme);

            String buttonText = String.format("%s %d", phoneme, numberOfAttemptsRemaining); // \n
            boolean buttonEnabled = numberOfAttemptsRemaining > 0;

            Button button = new Button(this);
            button.setText(buttonText);
            button.setEnabled(buttonEnabled);
            button.setTextSize(36);
            button.setOnClickListener(buttonClickListener);

            linearLayout.addView(button, layoutParams);
            i++;
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_test_choose;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        testChoosePresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        testChoosePresenter.detachView();
    }

    public class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Start learning the chosen phoneme
            Button btn = (Button) v;
            String buttonText = btn.getText().toString();

            String lines[] = buttonText.split("\\s+"); // "\\r?\\n"

            String phoneme = lines[0];

            startActivity(RecordVideoActivity.getStartIntent(getApplicationContext(), phoneme, true));
            finish();
        }

    }
}
