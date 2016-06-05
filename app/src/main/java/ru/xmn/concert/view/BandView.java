package ru.xmn.concert.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.xmn.concert.model.data.BandLastfm;
import ru.xmn.concert.model.data.EventGig;
@StateStrategyType(AddToEndSingleStrategy.class)
public interface BandView extends MvpView {
    public void showData(BandLastfm bandLastfmDTO);

    public void showEvents(List<EventGig> data);
}
