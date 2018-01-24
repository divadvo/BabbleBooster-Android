package com.divadvo.babbleboosternew.injection.component;

import dagger.Subcomponent;

import com.divadvo.babbleboosternew.features.choosePhonemes.ChoosePhonemesActivity;
import com.divadvo.babbleboosternew.features.choosePhonemes.ChoosePhonemesPresenter;
import com.divadvo.babbleboosternew.features.customizeReinforcement.CustomizeReinforcementActivity;
import com.divadvo.babbleboosternew.features.customizeStimuli.CustomizeStimuliActivity;
import com.divadvo.babbleboosternew.features.detail.DetailActivity;
import com.divadvo.babbleboosternew.features.home.HomeActivity;
import com.divadvo.babbleboosternew.features.learnPhonemes.LearnPhonemesActivity;
import com.divadvo.babbleboosternew.features.lock.LockActivity;
import com.divadvo.babbleboosternew.features.main.MainActivity;
import com.divadvo.babbleboosternew.features.recordVideo.RecordVideoActivity;
import com.divadvo.babbleboosternew.features.recordVideo.RecordVideoMvpView;
import com.divadvo.babbleboosternew.features.settingsChoose.SettingsChooseActivity;
import com.divadvo.babbleboosternew.features.testChoose.TestChooseActivity;
import com.divadvo.babbleboosternew.injection.PerActivity;
import com.divadvo.babbleboosternew.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(DetailActivity detailActivity);

    void inject(LockActivity lockActivity);

    void inject(HomeActivity homeActivity);

    void inject(ChoosePhonemesActivity choosePhonemesActivity);

    void inject(LearnPhonemesActivity learnPhonemesActivity);

    void inject(RecordVideoActivity recordVideoActivity);

    void inject(SettingsChooseActivity settingsChooseActivity);

    void inject(CustomizeReinforcementActivity customizeReinforcementActivity);

    void inject(CustomizeStimuliActivity customizeStimuliActivity);

    void inject(TestChooseActivity testChooseActivity);
}
