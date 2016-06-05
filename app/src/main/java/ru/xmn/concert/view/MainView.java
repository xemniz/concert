package ru.xmn.concert.view;

import java.util.List;
import java.util.Set;

import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventGig;

/**
 * Created by xmn on 22.05.2016.
 */

public interface MainView {
    public void showData(Set<Band> list);
}
