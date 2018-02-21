package com.divadvo.babbleboosternew.features.customizeReinforcement;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.divadvo.babbleboosternew.R;
import com.divadvo.babbleboosternew.features.base.BaseActivity;
import com.divadvo.babbleboosternew.injection.component.ActivityComponent;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class CustomizeReinforcementActivity extends BaseActivity implements CustomizeReinforcementMvpView {

    @Inject
    CustomizeReinforcementPresenter customizeReinforcementPresenter;

    @BindView(R.id.button_change_yes)
    Button btnYes;

    @BindView(R.id.button_change_good_try)
    Button btnGoodTry;

    @BindView(R.id.video_reinforcement_yes)
    VideoView videoViewYes;

    @BindView(R.id.video_reinforcement_good_try)
    VideoView videoViewGoodTry;

    @BindView(R.id.customize_textview_output)
    TextView textView;

    private static int VIDEO_YES = 101, VIDEO_GOOD_TRY = 201;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CustomizeReinforcementActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The videos are played when clicked on
        videoViewYes.setOnTouchListener((v, event) -> {
            videoViewYes.start();
            return false;
        });
        videoViewGoodTry.setOnTouchListener((v, event) -> {
            videoViewGoodTry.start();
            return false;
        });
        btnYes.setOnClickListener(v -> changeYes());
        btnGoodTry.setOnClickListener(v -> changeGoodTry());

        showCurrentVideos();

    }

    /**
     * Gets the paths to the reinforcement videos
     * and sets them to the viewviews
     */
    private void showCurrentVideos() {
        String yesPath = customizeReinforcementPresenter.getReinforcementVideo("YES");
        if (yesPath != null)
            videoViewYes.setVideoPath(yesPath);

        String goodTryPath = customizeReinforcementPresenter.getReinforcementVideo("GOOD_TRY");
        if (goodTryPath != null)
            videoViewGoodTry.setVideoPath(goodTryPath);

        refreshVideoViews();
    }

    /**
     * Shows the first frame as a thumbnail.
     */
    private void refreshVideoViews() {
        videoViewYes.seekTo(1);
        videoViewGoodTry.seekTo(1);
    }

    // The parent can choose a video from the gallery
    private void changeYes() {
//        Intent intent = new Intent();
//        intent.setType("video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/mp4");
        startActivityForResult(Intent.createChooser(intent, "Select YES Video"), VIDEO_YES);

    }

    private void changeGoodTry() {
//        Intent intent = new Intent();
//        intent.setType("video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/mp4");
        startActivityForResult(Intent.createChooser(intent, "Select GOOD TRY Video"), VIDEO_GOOD_TRY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
//            String selectedImagePath = getRealPathFromURI(this, selectedImageUri);
            String selectedImagePath = getPath(selectedImageUri);

            // After a video was selected update the file
            if (selectedImagePath != null) {
                if (requestCode == VIDEO_YES) {
                    updateVideo(selectedImagePath, "YES");
                } else if (requestCode == VIDEO_GOOD_TRY) {
                    updateVideo(selectedImagePath, "GOOD_TRY");
                }
            }
        }
    }

    private void updateVideo(String path, String type) {
        File userReinforcementFolder = new File(customizeReinforcementPresenter.getReinforcementFolder());
        // YES.mp4 for example
        String fileName = type + "." + customizeReinforcementPresenter.getFileExtension(new File(path));
        File newReinforcementVideoFile = new File(userReinforcementFolder, fileName);

        // Copy and play the video
        customizeReinforcementPresenter.copyFile(new File(path), newReinforcementVideoFile);
//        videoViewYes.setVideoPath(newReinforcementVideoFile.getAbsolutePath());
//        refreshVideoViews();
//        videoViewYes.start();
        showCurrentVideos();
    }


    @Override
    public int getLayout() {
        return R.layout.activity_customize_reinforcement;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        customizeReinforcementPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        customizeReinforcementPresenter.detachView();
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
