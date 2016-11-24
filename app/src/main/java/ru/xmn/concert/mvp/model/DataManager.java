package ru.xmn.concert.mvp.model;

import android.util.Log;

import java.util.List;

import ru.xmn.concert.gigs.filter.GigsFilter;
import ru.xmn.concert.mvp.model.api.RealmProvider;
import ru.xmn.concert.mvp.model.api.RockGigProvider;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Observable;

/**
 * Created by xmn on 19.11.2016.
 */

public class DataManager {
    private static final String TAG = "DataManager";
    private RockGigProvider mRockGigProvider;
    private RealmProvider mRealmProvider;

    private static final DataManager INSTANCE = new DataManager();

    public static DataManager getInstance() {
        return INSTANCE;
    }

    private DataManager(){
        mRockGigProvider = new RockGigProvider();
        mRealmProvider = new RealmProvider();
    }


    //region Splash
    public Observable<List<Event>> loadGigsToRealm() {
        return mRockGigProvider.getEventsRockGig()
                .flatMap(Observable::from)
                .flatMap(eventRockGig -> Observable.just(new Event(eventRockGig)))
                .toList()
                .doOnNext(this::toRealmIfNeeded);
    }

    private void toRealmIfNeeded(List<Event> events) {
        if (mRealmProvider.getEventsCount() < 1){
            mRealmProvider.ListToRealm(events);
        }
        Log.d(TAG, "toRealmIfNeeded: already loaded");
    }

    public Observable<List<Event>> getAllEvents(GigsFilter filter) {
        return mRealmProvider.getEvents();
    }

    //endregion
}
