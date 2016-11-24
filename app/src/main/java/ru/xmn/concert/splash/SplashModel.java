package ru.xmn.concert.splash;

import java.util.List;

import ru.xmn.concert.mvp.model.DataManager;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Observable;

/**
 * Created by xmn on 19.11.2016.
 */
public class SplashModel {
    DataManager mDataManager;
    public static final SplashModel INSTANCE = new SplashModel();

    public static SplashModel getInstance() {
        return INSTANCE;
    }

    private SplashModel() {
        mDataManager = DataManager.getInstance();
    }

    public Observable<List<Event>> loadGigsToRealm() {
        return mDataManager.loadGigsToRealm();
    }
}
