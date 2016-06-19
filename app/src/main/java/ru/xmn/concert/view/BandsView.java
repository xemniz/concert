package ru.xmn.concert.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.model.data.EventRockGig;


@StateStrategyType(SingleStateStrategy.class)
public interface BandsView extends MvpView {
    public void showData(List<Band> list);

    void showError(String message);

    void hideError();

    void onStartLoading();

    void onFinishLoading();

    void showRefreshing();

    void hideRefreshing();

    void showListProgress();

    void hideListProgress();

    void setBands(List<Band> bands);

    @StateStrategyType(AddToEndStrategy.class)
    void addBands(List<Band> bands);

    void setGigs(List<EventRockGig> bands);

    @StateStrategyType(AddToEndStrategy.class)
    void addGigs(List<EventRockGig> bands);

    void setGigsRealm(List<EventRealm> bands);

    @StateStrategyType(AddToEndStrategy.class)
    void addGigsRealm(List<EventRealm> bands);
}
