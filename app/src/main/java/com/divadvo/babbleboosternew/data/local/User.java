package com.divadvo.babbleboosternew.data.local;

import java.util.ArrayList;
import java.util.Date;

public class User {

    public Date a_start_date;
    public Date b_start_date;
    public ArrayList<String> all_phonemes;
    public ArrayList<String> mastered_phonemes;
    public ArrayList<Date> test_dates;
    public String username;

    public ArrayList<String> getCurrentPhonemes() {
        ArrayList<String> currentPhonemes = new ArrayList<>(all_phonemes);
        currentPhonemes.removeAll(mastered_phonemes);
        return currentPhonemes;
    }
}
