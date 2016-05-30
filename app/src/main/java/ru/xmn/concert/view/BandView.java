package ru.xmn.concert.view;

import java.util.List;

import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventGig;

public interface BandView {
    public void showData(Band bandDTO);

    public void showEvents(List<EventGig> data);
}
