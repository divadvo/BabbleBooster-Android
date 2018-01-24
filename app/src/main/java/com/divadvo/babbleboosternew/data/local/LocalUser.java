package com.divadvo.babbleboosternew.data.local;

public class LocalUser {

    private static User instance;

    public static User getInstance() {
        return instance;
    }
    public static void setInstance(User user) {instance = user;}
}
