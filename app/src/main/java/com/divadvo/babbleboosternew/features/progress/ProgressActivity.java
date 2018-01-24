package com.divadvo.babbleboosternew.features.progress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.data.local.LocalUser;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ProgressActivity extends BaseActivity implements ProgressMvpView {
    @Inject
    ProgressPresenter choosePhonemesPresenter;

    @BindView(R.id.text_view_current_scores)
    TextView textViewCurrentScores;

    @BindView(R.id.text_view_mastered_sounds)
    TextView textViewMasteredSounds;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ProgressActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generateCurrentText();
        generateMasteredText();
    }

    private void generateMasteredText() {
        List<String> masteredPhonemes = LocalUser.getInstance().mastered_phonemes;
        String masteredText = "";
        for(String phoneme : masteredPhonemes) {
            masteredText += phoneme + "\n";
        }
        textViewMasteredSounds.setText(masteredText);
    }

    private void generateCurrentText() {
        List<String> currentPhonemes = LocalUser.getInstance().getCurrentPhonemes();

        int i = 0;
        String currentPhonemesText = "";
        for (String phoneme : currentPhonemes) {
            if(i > 3)
                break;

            double average = choosePhonemesPresenter.getBestDayAverage(phoneme);
            String text = phoneme;

            // only display the average when it's not 0
            String averageText = String.format(" (%d%% correct)", (long)average);
            if(average > 0)
                text += averageText;

            currentPhonemesText += text + "\n";

            i++;
        }
        textViewCurrentScores.setText(currentPhonemesText);
    }



    @Override
    public int getLayout() {
        return R.layout.activity_progress;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        choosePhonemesPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        choosePhonemesPresenter.detachView();
    }
}
