package com.divadvo.babbleboosternew.features.settingsChoose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.data.local.LocalUser;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.features.customizeReinforcement.CustomizeReinforcementActivity;
import com.divadvo.babbleboosternew.features.customizeStimuli.CustomizeStimuliActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SettingsChooseActivity extends BaseActivity implements SettingsChooseMvpView {

    @Inject
    SettingsChoosePresenter settingsChoosePresenter;

    @BindView(R.id.buttonChangeReinforcement)
    Button btnCustomizeReinforcement;

    @BindView(R.id.layout_phoneme_buttons_settings)
    LinearLayout linearLayout;

    private List<String> phonemes;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SettingsChooseActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getPhonemes();
        generateButtons();

        btnCustomizeReinforcement.setOnClickListener(v -> startActivity(CustomizeReinforcementActivity.getStartIntent(this)));

    }

    private void getPhonemes() {
//        phonemes = new ArrayList<>(user.getCurrentPhonemes());
//        phonemes = Arrays.asList("p", "b", "k");
        phonemes = LocalUser.getInstance().getCurrentPhonemes();
    }

    private void generateButtons() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ButtonClickListener buttonClickListener = new ButtonClickListener();

        // Creates buttons for all current phonemes
        // The stimuli can be change after the parent
        // clicks on one of the phonemes

        for (String phoneme : phonemes) {
            Button button = new Button(this);
            button.setText(phoneme);
            button.setTextSize(24);
            button.setOnClickListener(buttonClickListener);

            linearLayout.addView(button, layoutParams);
        }
    }

    public class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            String phoneme = btn.getText().toString();

            startCustomizeStimuliActivity(phoneme);
        }

    }

    private void startCustomizeStimuliActivity(String phoneme) {
        startActivity(CustomizeStimuliActivity.getStartIntent(this, phoneme));
    }

    @Override
    public int getLayout() {
        return R.layout.activity_settings_choose;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        settingsChoosePresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        settingsChoosePresenter.detachView();
    }
}
