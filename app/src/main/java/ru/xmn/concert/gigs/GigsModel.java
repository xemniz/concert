package ru.xmn.concert.gigs;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.xmn.concert.mvp.model.DataManager;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class GigsModel {
    private static final String TAG = "GigsModel";
    private final DataManager mDataManager;

    //region singleton
    private GigsModel() {
        mDataManager = DataManager.getInstance();
    }

    final static GigsModel INSTANCE = new GigsModel();

    public static GigsModel getInstance() {
        return INSTANCE;
    }
    //endregion

    //region loadEventResults
    Observable<RealmResults<Event>> loadGigsResults(Realm realm) {
        return mDataManager.getAllEventsRealm(realm);
    }
    //endregion

    void changeFilter(int id, boolean isApply) {
        mDataManager.changeFilter(id, isApply);
    }

}
