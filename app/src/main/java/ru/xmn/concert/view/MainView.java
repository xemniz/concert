package ru.xmn.concert.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.xmn.concert.model.data.BandRockGig;

/**
 * Created by xmn on 11.06.2016.
 */
@StateStrategyType(SingleStateStrategy.class)
public interface MainView extends MvpView{
    void showData(List<BandRockGig> list);

    void showError(String message);

    void hideError();

    void setFragment();
}
