package com.divadvo.babbleboosternew.features.choosePhonemes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.features.home.HomeActivity;
import com.divadvo.babbleboosternew.features.home.HomePresenter;
import com.divadvo.babbleboosternew.features.learnPhonemes.LearnPhonemesActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChoosePhonemesActivity extends BaseActivity implements ChoosePhonemesMvpView {
    @Inject
    ChoosePhonemesPresenter choosePhonemesPresenter;

    @BindView(R.id.layout_phoneme_buttons)
    LinearLayout linearLayout;


    private List<String> phonemes;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChoosePhonemesActivity.class);
//        intent.putExtra(EXTRA_POKEMON_NAME, pokemonName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phonemes = Arrays.asList("p", "b", "k");
        // get from user
        generateButtons();
    }

    private void generateButtons() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        layoutParams.setMargins(24, 0, 24, 0);
        ButtonClickListener buttonClickListener = new ButtonClickListener();

        int i = 0;
        for (String phoneme : phonemes) {
            if(i == 3)
                break;

            Button button = new Button(this);
            button.setText(phoneme);
            button.setTextSize(36);
            button.setOnClickListener(buttonClickListener);

            linearLayout.addView(button, layoutParams);
            i++;
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_choose_phonemes;
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

    public class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Start learning the chosen phoneme
            Button btn = (Button) v;
            String phoneme = btn.getText().toString();

            startActivity(LearnPhonemesActivity.getStartIntent(getApplicationContext(), phoneme, false));
            finish();
        }

    }
}
