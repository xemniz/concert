package ru.xmn.concert.view;

import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.model.data.EventRockGig;


@StateStrategyType(AddToEndStrategy.class)
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

    void setAdapter(RecyclerView.Adapter adapter);

    void setGigsRealm(List<String> bands);

    @StateStrategyType(AddToEndStrategy.class)
    void addGigsRealm(List<String> bands);
}
