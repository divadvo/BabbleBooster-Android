package com.divadvo.babbleboosternew.data.local;

import android.content.Context;

import com.divadvo.babbleboosternew.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


// To be implemented with Realm

@Singleton
public class DbManager {

    Realm realm;

    @Inject
    public DbManager(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public int getNextAttemptNumber(String phoneme, boolean isTest) {
        RealmResults<RealmAttempt> attemptsFound = realm.where(RealmAttempt.class)
                .equalTo("phoneme", phoneme)
                .equalTo("isTest", isTest)
                .sort("attemptNumber", Sort.DESCENDING)
                .findAll();

        RealmAttempt attemptWithMaxAttemptNumber = null;

        if (attemptsFound.size() > 0) {
            attemptWithMaxAttemptNumber = attemptsFound.get(0);
        }

        if (attemptWithMaxAttemptNumber != null) {
            return attemptWithMaxAttemptNumber.getAttemptNumber() + 1;
        } else {
            return 1;
        }
    }

    public void saveAttempt(Attempt attempt) {
        realm.beginTransaction();

//        if(attempt.isTest()) {
//            TestAttemptRealm testAttemptRealm = attempt.getTestAttemptRealm();
//            realm.copyToRealm(testAttemptRealm);
//        } else {
//            AttemptRealm attemptRealm = attempt.getAttemptRealm();
//            realm.copyToRealm(attemptRealm);
//        }

        RealmAttempt realmAttempt = attempt.generateRealmAttempt();
        realm.copyToRealm(realmAttempt);

        realm.commitTransaction();

    }

    public RealmResults<RealmAttempt> getAllRealmAttempts() {
        return realm.where(RealmAttempt.class).findAll();
    }

    private RealmResults<RealmAttempt> getAllRealmAttemptsForPhoneme(String phoneme) {
        return realm.where(RealmAttempt.class)
                .equalTo("phoneme", phoneme)
                .equalTo("isTest", false) // Important. Not test attempts
                .findAll();
    }

    private List<Attempt> convertRealmAttemptsToAttempts(RealmResults<RealmAttempt> realmAttempts) {
        List<Attempt> list = new ArrayList<>();
        for (RealmAttempt realmAttempt : realmAttempts) {
            list.add(realmAttempt.generateAttempt());
        }
        return list;
    }


    public List<Attempt> getAllAttempts() {
        return convertRealmAttemptsToAttempts(getAllRealmAttempts());
    }

    private List<Attempt> getAllAttemptsForPhoneme(String phoneme) {
        return convertRealmAttemptsToAttempts(getAllRealmAttemptsForPhoneme(phoneme));
    }

    public ArrayList<String> recalculateMasteredPhonemes() {
        List<String> allPhonemes = LocalUser.getInstance().all_phonemes;
        ArrayList<String> newMastered = new ArrayList<>();
        for (String phoneme : allPhonemes) {
            if (isMastered(phoneme) || LocalUser.getInstance().mastered_phonemes.contains(phoneme)) {
                newMastered.add(phoneme);
            }
        }
        return newMastered;
    }

    private boolean isMastered(String phoneme) {
        List<Attempt> attemptsForPhoneme = getAllAttemptsForPhoneme(phoneme);
        Map<Date, List<Attempt>> attemptsGroupedByDay = groupAttemptsByDay(attemptsForPhoneme);
        Map<Date, Double> daysWithAverages = calculateAverages(attemptsGroupedByDay);
        int maxDaysInRow = findLongestStreak(daysWithAverages);

        return (maxDaysInRow >= Constants.DAYS_IN_ROW_FOR_MASTERED); // DAYS_IN_ROW
    }

    private int findLongestStreak(Map<Date, Double> daysWithAverages) {
        List<Double> averageList = new ArrayList<>(daysWithAverages.values());
        int currentStreak = 0;
        int maximumStreak = 0;

        for (Double average : averageList) {
            if (average >= Constants.MIN_GOOD_AVERAGE_PER_DAY) { // GOOD_AVERAGE
                currentStreak++;
                if(currentStreak > maximumStreak) {
                    maximumStreak = currentStreak;
                }
            } else {
                currentStreak = 0;
            }
        }

        return maximumStreak;
    }

    private Map<Date, List<Attempt>> groupAttemptsByDay(List<Attempt> attemptsForPhoneme) {
        Map<Date, List<Attempt>> map = new HashMap<>();
        for (Attempt attempt : attemptsForPhoneme) {
            Date attemptDate = getDateWithOutTime(new Date(attempt.getTimestamp()));
            List<Attempt> group = map.get(attemptDate);
            if (group == null) {
                group = new ArrayList<Attempt>();
                map.put(attemptDate, group);
            }
            group.add(attempt);
        }
        return map;
    }

    private Map<Date, Double> calculateAverages(Map<Date, List<Attempt>> attemptsGroupedByDay) {
        Map<Date, Double> averageValid = new HashMap<>();

        for (Map.Entry<Date, List<Attempt>> dayEntry : attemptsGroupedByDay.entrySet()) {
            Date date = dayEntry.getKey();
            List<Attempt> attemptsOnDay = dayEntry.getValue();

            int totalNumberOfAttempts = attemptsOnDay.size();
            int numberYesAttempts = 0;
            for (Attempt attemptLocalYes : attemptsOnDay) {
                if (attemptLocalYes.getResponse().equals("YES"))
                    numberYesAttempts++;
            }


            // If enough attempts
            double average = numberYesAttempts * 100.0 / totalNumberOfAttempts;

            // if not enough attempts -> don't count that day = streak ends
            if (totalNumberOfAttempts < Constants.MIN_NUMBER_ATTEMPTS_ENOUGH_PER_DAY) //MIN_NUMBER_ATTEMPTS_ENOUGH_PER_DAY
                average = 0;

            averageValid.put(date, average);
        }

        return averageValid;
    }

    public double getBestDayAverage(String phoneme) {
        List<Attempt> attemptsForPhoneme = getAllAttemptsForPhoneme(phoneme);
        Map<Date, List<Attempt>> attemptsGroupedByDay = groupAttemptsByDay(attemptsForPhoneme);
        Map<Date, Double> daysWithAverages = calculateAverages(attemptsGroupedByDay);

        double max = 0;
        for (Map.Entry<Date, Double> day : daysWithAverages.entrySet()) {
            Double attemptDay = day.getValue();
            if (attemptDay > max)
                max = attemptDay;
        }
        return max;
    }

    private Date getDateWithOutTime(Date targetDate) {
        Calendar newDate = Calendar.getInstance();
        newDate.setLenient(false);
        newDate.setTime(targetDate);
        newDate.set(Calendar.HOUR_OF_DAY, 0);
        newDate.set(Calendar.MINUTE, 0);
        newDate.set(Calendar.SECOND, 0);
        newDate.set(Calendar.MILLISECOND, 0);

        return newDate.getTime();

    }

    private Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public int calculateNumberOfAttemptsRemaining(String phoneme) {
        return Constants.NUMBER_OF_TEST_ATTEMPTS_PER_PHONEME - numberOfTestAttemptsForPhonemeToday(phoneme);
    }

    private int numberOfTestAttemptsForPhonemeToday(String phoneme) {
        return getAllTestAttemptsForPhonemeToday(phoneme).size();
    }

    private RealmResults<RealmAttempt> getAllRealmTestAttemptsForPhonemeToday(String phoneme) {
        Date now = new Date();

        long beginningOfDayTimestamp = getDateWithOutTime(now).getTime();
        long endOfDayTimestamp = getEndOfDay(now).getTime();

        RealmResults<RealmAttempt> results2 = realm.where(RealmAttempt.class)
                .equalTo("phoneme", phoneme)
                .equalTo("isTest", true)
                .findAll();

        RealmResults<RealmAttempt> results = realm.where(RealmAttempt.class)
                .equalTo("phoneme", phoneme)
                .equalTo("isTest", true)
                .between("timestamp", beginningOfDayTimestamp, endOfDayTimestamp)
                .findAll();

        return results;
    }

    private List<Attempt> getAllTestAttemptsForPhonemeToday(String phoneme) {
        return convertRealmAttemptsToAttempts(getAllRealmTestAttemptsForPhonemeToday(phoneme));
    }
}
