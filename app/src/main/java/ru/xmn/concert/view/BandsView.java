package ru.xmn.concert.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.xmn.concert.model.data.BandRockGig;
import ru.xmn.concert.model.data.EventRockGig;


@StateStrategyType(SingleStateStrategy.class)
public interface BandsView extends MvpView {
    public void showData(List<BandRockGig> list);

    void showError(String message);

    void hideError();

    void onStartLoading();

    void onFinishLoading();

    void showRefreshing();

    void hideRefreshing();

    void showListProgress();

    void hideListProgress();

    void setBands(List<BandRockGig> bandRockGigs);

    @StateStrategyType(AddToEndStrategy.class)
    void addBands(List<BandRockGig> bandRockGigs);

    void setGigs(List<EventRockGig> bands);

    @StateStrategyType(AddToEndStrategy.class)
    void addGigs(List<EventRockGig> bands);
}
