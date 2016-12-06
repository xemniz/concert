package ru.xmn.concert.gigs;

import android.util.Log;

import ru.xmn.concert.mvp.model.DataManager;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class GigsModel {
    private static final String TAG = "GigsModel";
    private final DataManager mDataManager;
    private List<Event> mCachedEvents;
    private List<Event> mEvents;

    //paginator
    private final int ONPAGE = 20;
    private volatile int CURPAGE = 0;
    private volatile int COUNT = 0;

    //region singleton
    private GigsModel() {
        mDataManager = DataManager.getInstance();
    }

    final static GigsModel INSTANCE = new GigsModel();

    public static GigsModel getInstance() {
        return INSTANCE;
    }
    //endregion

    //region getters, setters
    private void setEvents(List<Event> events) {
        mEvents = new ArrayList<>(events);
    }

    List<Event> getEvents() {
        return mEvents;
    }
    //endregion

    //region loadEvents
    Observable<List<Event>> loadGigs() {
        COUNT = 0;
        CURPAGE = 0;

        return mDataManager.getAllEvents()
                .doOnNext(events -> COUNT = events.size())
                .doOnNext(this::setCachedEvents)
                .map(events -> events.subList(0, getNextPageSize()))
                .doOnNext(this::setEvents)
                .doOnNext(e -> CURPAGE++);
    }

    private void setCachedEvents(List<Event> events) {
        mCachedEvents = new ArrayList<>(events);
    }

    void loadNextGigs() {
        mEvents.addAll(new ArrayList<>(mCachedEvents.subList(ONPAGE * CURPAGE, ONPAGE * CURPAGE + getNextPageSize())));
        CURPAGE++;
    }

    private int getNextPageSize() {
        if (ONPAGE < COUNT - ONPAGE * CURPAGE) {
            Log.d(TAG, "getNextPageSize() returned: " + ONPAGE);
            return ONPAGE;
        } else {
            Log.d(TAG, "getNextPageSize() returned: " + (COUNT - ONPAGE * CURPAGE));
            return COUNT - ONPAGE * CURPAGE;
        }
    }

    boolean isLoadMore() {
        if (ONPAGE < mCachedEvents.size() - ONPAGE * CURPAGE) {
            return true;
        } else {
            return false;
        }
    }

    public void changeFilter(int id, boolean isApply) {
        mDataManager.changeFilter(id, isApply);
    }
    //endregion
}
