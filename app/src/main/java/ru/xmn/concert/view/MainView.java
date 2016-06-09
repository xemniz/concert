package ru.xmn.concert.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;
import java.util.Set;

import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventGig;


@StateStrategyType(SingleStateStrategy.class)
public interface MainView extends MvpView {
    public void showData(List<Band> list);

    void showError(String message);

    void hideError();

    void onStartLoading();

    void onFinishLoading();

    void showRefreshing();

    void hideRefreshing();

    void showListProgress();

    void hideListProgress();

    void setBands(List<Band> bands, boolean maybeMore);

    @StateStrategyType(AddToEndStrategy.class)
    void addBands(List<Band> bands, boolean maybeMore);
}
