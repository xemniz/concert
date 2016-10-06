package ru.xmn.concert.view;

import java.util.List;

import ru.xmn.concert.model.data.EventRealm;
import rx.Observable;

/**
 * Created by xmn on 10.09.2016.
 */

public interface EventListView {

    void loadEventList(List<EventRealm> eventRealms);

    void setListSize(Integer listSize);
}
