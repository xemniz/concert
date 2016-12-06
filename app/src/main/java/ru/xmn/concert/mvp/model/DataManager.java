package ru.xmn.concert.mvp.model;

import android.util.Log;

import java.util.List;

import ru.xmn.concert.gigs.GigsFragment;
import ru.xmn.concert.gigs.MainActivity;
import ru.xmn.concert.gigs.filter.GigsFilter;
import ru.xmn.concert.gigs.filter.VkFilterItem;
import ru.xmn.concert.mvp.model.api.RealmProvider;
import ru.xmn.concert.mvp.model.api.RockGigProvider;
import ru.xmn.concert.mvp.model.api.VkApiBridge;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Observable;

/**
 * Created by xmn on 19.11.2016.
 */

public class DataManager {
    private static final String TAG = "DataManager";
    private final GigsFilter mFilter;
    private RockGigProvider mRockGigProvider;
    private RealmProvider mRealmProvider;
    private VkApiBridge mVkApiBridge;

    private static final DataManager INSTANCE = new DataManager();

    public static DataManager getInstance() {
        return INSTANCE;
    }

    private DataManager(){
        mRockGigProvider = new RockGigProvider();
        mRealmProvider = new RealmProvider();
        mVkApiBridge = new VkApiBridge();
        mFilter = new GigsFilter();
        mFilter.addItem(GigsFragment.FILTER_VK, new VkFilterItem(mVkApiBridge));
    }


    //region Splash
    public Observable<List<Event>> loadGigsToRealm() {
        return mRockGigProvider.getEventsRockGig()
                .doOnNext(this::toRealmIfNeeded);
    }

    private void toRealmIfNeeded(List<Event> events) {
        if (mRealmProvider.getEventsCount() < 1){
            mRealmProvider.ListToRealm(events);
        }
        Log.d(TAG, "toRealmIfNeeded: already loaded");
    }

    //endregion

    //region Gigs

    public Observable<List<Event>> getAllEvents() {
        return mRealmProvider.getEvents(mFilter);
    }

    public void changeFilter(int id, boolean isApply) {
        mFilter.setApplyById(id, isApply);
    }

    //endregion
}
