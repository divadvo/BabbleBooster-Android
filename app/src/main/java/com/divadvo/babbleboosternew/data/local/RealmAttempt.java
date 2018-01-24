package com.divadvo.babbleboosternew.data.local;


import io.realm.RealmObject;

public class RealmAttempt extends RealmObject {
    private String username;
    private boolean isTest;
    private String phoneme;
    private int attemptNumber;
    private String newVideoFilename;
    private String response;
    private long timestamp;

    public RealmAttempt(String username, boolean isTest, String phoneme, int attemptNumber, String newVideoFilename, String response, long timestamp) {
        this.username = username;
        this.isTest = isTest;
        this.phoneme = phoneme;
        this.attemptNumber = attemptNumber;
        this.newVideoFilename = newVideoFilename;
        this.response = response;
        this.timestamp = timestamp;
    }

    public RealmAttempt() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public String getPhoneme() {
        return phoneme;
    }

    public void setPhoneme(String phoneme) {
        this.phoneme = phoneme;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public String getNewVideoFilename() {
        return newVideoFilename;
    }

    public void setNewVideoFilename(String newVideoFilename) {
        this.newVideoFilename = newVideoFilename;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Attempt generateAttempt() {
        return new Attempt(username, isTest, phoneme, attemptNumber, newVideoFilename, response, timestamp);
    }
}
