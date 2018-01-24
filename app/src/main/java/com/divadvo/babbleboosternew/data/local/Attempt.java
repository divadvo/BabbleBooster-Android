package com.divadvo.babbleboosternew.data.local;

public class Attempt {

    private String username;
    private boolean isTest;
    private String phoneme;
    private String newVideoFilename;
    private String response;
    private int attemptNumber;
    private long timestamp;

    public Attempt(String username, boolean isTest, String phoneme, int attemptNumber, String newVideoFilename, String response, long timestamp) {
        this.username = username;
        this.isTest = isTest;
        this.phoneme = phoneme;
        this.attemptNumber = attemptNumber;
        this.newVideoFilename = newVideoFilename;
        this.response = response;
        this.timestamp = timestamp;
    }

    public Attempt() {

    }

    public RealmAttempt generateRealmAttempt() {
        return new RealmAttempt(username, isTest, phoneme, attemptNumber, newVideoFilename, response, timestamp);
    }

    public String getUsername() {
        return username;
    }

    public boolean isTest() {
        return isTest;
    }

    public String getPhoneme() {
        return phoneme;
    }

    public String getNewVideoFilename() {
        return newVideoFilename;
    }

    public String getResponse() {
        return response;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public void setPhoneme(String phoneme) {
        this.phoneme = phoneme;
    }

    public void setNewVideoFilename(String newVideoFilename) {
        this.newVideoFilename = newVideoFilename;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        Attempt other = (Attempt) obj;
        return this.username.equals(other.username) &&
                this.isTest == other.isTest &&
                this.phoneme.equals(other.phoneme) &&
                this.newVideoFilename.equals(other.newVideoFilename) &&
                this.response.equals(other.response) &&
                this.attemptNumber == other.attemptNumber &&
                this.timestamp == other.timestamp;
    }
}
