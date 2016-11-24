package ru.xmn.concert.mvp.view;

import java.util.List;

import ru.xmn.concert.mvp.model.data.Event;

/**
 * Created by xmn on 10.09.2016.
 */

public interface EventListView {

    void loadEventList(List<Event> events);

    void setListSize(Integer listSize);

    void refresh();
}
