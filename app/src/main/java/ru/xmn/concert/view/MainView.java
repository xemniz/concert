package ru.xmn.concert.view;

import java.util.List;

import ru.xmn.concert.model.data.EventGig;

/**
 * Created by xmn on 22.05.2016.
 */

public interface MainView {
    public void showData(List<EventGig> list);
}
