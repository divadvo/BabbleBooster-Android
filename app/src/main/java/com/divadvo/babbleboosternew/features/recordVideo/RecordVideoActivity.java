package com.divadvo.babbleboosternew.features.recordVideo;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialcamera.MaterialCamera;
import com.divadvo.babbleboosternew.Constants;
import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.features.choosePhonemes.ChoosePhonemesActivity;
import com.divadvo.babbleboosternew.features.choosePhonemes.ChoosePhonemesPresenter;
import com.divadvo.babbleboosternew.features.learnPhonemes.LearnPhonemesActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;

public class RecordVideoActivity extends BaseActivity implements RecordVideoMvpView {
    public static final String EXTRA_PHONEME = "EXTRA_PHONEME";


    private final static int CAMERA_RQ = 6969;

    @BindView(R.id.button_yes_new)
    Button btnYes;

    @BindView(R.id.button_attempt_new)
    Button btnAttempt;

    @BindView(R.id.button_no_new)
    Button btnNo;

    @BindView(R.id.video_view_record)
    VideoView videoView;

    @Inject
    RecordVideoPresenter recordVideoPresenter;

    private String phoneme;

    public static Intent getStartIntent(Context context, String phoneme) {
        Intent intent = new Intent(context, RecordVideoActivity.class);
        intent.putExtra(EXTRA_PHONEME, phoneme);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phoneme = getIntent().getStringExtra(EXTRA_PHONEME);
        if (phoneme == null) {
            throw new IllegalArgumentException("Record Video Activity requires a phoneme");
        }

        ButtonClickListener buttonClickListener = new ButtonClickListener();
        btnYes.setOnClickListener(buttonClickListener);
        btnAttempt.setOnClickListener(buttonClickListener);
        btnNo.setOnClickListener(buttonClickListener);

        startCamera();
    }

    /**
     * Uses material camera library to start a front facing camera
     * Video recording starts automatically and ends after max 10 seconds
     */
    private void startCamera() {
        String folder = recordVideoPresenter.getVideoFolder();

        new MaterialCamera(this)
                .allowRetry(false)
                .autoSubmit(true)
                .saveDir(folder)
                .qualityProfile(Constants.CAMERA_QUALITY)
                .showPortraitWarning(false)
                .defaultToFrontFacing(true)
                .videoPreferredAspect(16f / 9f)
                .retryExits(false)
                .autoRecordWithDelayMs(100)
                .countdownSeconds(10f) // max length of video is 10 seconds
                .countdownImmediately(true) // recording starts automatically
                .start(CAMERA_RQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {

            if (resultCode == RESULT_OK) {
                saveAttemptVideo(data.getDataString());
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
            }
        }
    }

    private void saveAttemptVideo(String videoPath) {
        // Rename the recorded video to
        // [attemptnumber].[same_file_extension]

//        File from = new File(StorageUtils.directoryAttempts, new File(videoPath).getName());
//        attemptNumberFull = getFullAttemptString();
//        String newFilename = attemptNumberFull + "." + StorageUtils.getFileExtension(from);
//        File to = new File(StorageUtils.directoryAttempts, newFilename);
//        from.renameTo(to);

//        showRecordedVideo(to.getAbsolutePath());

//        File from = new File(videoPath);


        showRecordedVideo(videoPath);
    }

    private void showRecordedVideo(String videoPath) {
        videoView.setVideoPath(videoPath);
        videoView.start();
    }



    private void runSaveAttemptInDatabase(String response) {
//        AttemptLocal attemptLocal = new AttemptLocal(user.getUserid(), attemptNumberFull, CommonUtils.getCurrentTimeStamp(), phoneme, response);
//
//        LocalDatabaseHelper databaseHelper = LocalDatabaseHelper.getInstance(this);
//        databaseHelper.addAttempt(attemptLocal);

        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

        // Hide all buttons
        btnYes.setVisibility(View.GONE);
        btnAttempt.setVisibility(View.GONE);
        btnNo.setVisibility(View.GONE);

        performActionAfterResponse(response);
    }

    private void performActionAfterResponse(String response) {
        if ("YES".equals(response)) {
            playVideo("YES");
        }
        if ("GOOD TRY".equals(response)) {
            playVideo("GOOD_TRY");
        }
        if ("TRY AGAIN".equals(response)) {
            startActivity(LearnPhonemesActivity.getStartIntent(this, phoneme, true));
            finish();
        }
    }

    private void playVideo(String name) {
        String videoPath = recordVideoPresenter.getReinforcementVideo(name);
        if(videoPath != null) {
            videoView.setVideoPath(videoPath);
            videoView.start();

            // Play until the end of the video
            // Then go back to the menu where one can
            // choose the phonemes
            videoView.setOnCompletionListener(mp -> startChoosePhoneme());
        }
    }


    private void startChoosePhoneme() {
        startActivity(ChoosePhonemesActivity.getStartIntent(this));
        finish();
    }


    @Override
    public int getLayout() {
        return R.layout.activity_record_video;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        recordVideoPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        recordVideoPresenter.detachView();
    }

    public class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            String response = btn.getText().toString();
            runSaveAttemptInDatabase(response);
        }
    }

}
