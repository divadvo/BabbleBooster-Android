package com.divadvo.babbleboosternew;

import com.afollestad.materialcamera.MaterialCamera;

public interface Constants {

    String PREF_FILE_NAME = "mvpstarter_pref_file";

    int CAMERA_QUALITY = MaterialCamera.QUALITY_480P; // MaterialCamera.QUALITY_HIGH;

    boolean DELETE_UPLOADED_VIDEOS = true;

    int DAYS_IN_ROW_FOR_MASTERED = 3;
    int MIN_NUMBER_ATTEMPTS_ENOUGH_PER_DAY = 4;
    double MIN_GOOD_AVERAGE_PER_DAY = 75.0;

    int NUMBER_OF_TEST_ATTEMPTS_PER_PHONEME = 3;

}
