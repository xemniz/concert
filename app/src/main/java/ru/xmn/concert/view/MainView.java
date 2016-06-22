package ru.xmn.concert.view;

import android.support.v4.app.Fragment;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.xmn.concert.model.data.Band;

/**
 * Created by xmn on 11.06.2016.
 */
@StateStrategyType(AddToEndStrategy.class)
public interface MainView extends MvpView{
    void showData(List<Band> list);

    void showError(String message);

    void hideError();

    @StateStrategyType(SingleStateStrategy.class)
    void setFragment();
}
