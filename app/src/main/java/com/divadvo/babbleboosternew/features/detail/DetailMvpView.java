package com.divadvo.babbleboosternew.features.detail;

import com.divadvo.babbleboosternew.data.model.response.Pokemon;
import com.divadvo.babbleboosternew.data.model.response.Statistic;
import com.divadvo.babbleboosternew.features.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showPokemon(Pokemon pokemon);

    void showStat(Statistic statistic);

    void showProgress(boolean show);

    void showError(Throwable error);
}
