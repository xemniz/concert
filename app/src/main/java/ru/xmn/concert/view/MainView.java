package ru.xmn.concert.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;
import java.util.Set;

import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventGig;

/**
 * Created by xmn on 22.05.2016.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    public void showData(Set<Band> list);
}
