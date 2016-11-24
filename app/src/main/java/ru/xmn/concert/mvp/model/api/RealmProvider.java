package ru.xmn.concert.mvp.model.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import ru.xmn.concert.JobExecutor;
import ru.xmn.concert.mvp.model.data.BandLastfm;
import ru.xmn.concert.mvp.model.data.BandRealm;
import ru.xmn.concert.mvp.model.data.Event;
import ru.xmn.concert.mvp.model.data.EventRockGig;
import rx.Observable;
import rx.schedulers.Schedulers;

public class RealmProvider {
    private Realm mRealm = Realm.getDefaultInstance();
    private static final String TAG = "RealmProvider";

    ArrayList<Event> list = new ArrayList<Event>(){{
        for (int i = 0; i < 110; i++) {
            Event e = new Event();
            e.setName(i+"");
            add(e);
        }
    }};

    public <T extends RealmObject> void toRealm(T t) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(t);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public <T extends RealmObject> void ListToRealm(List<T> t) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(t);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public Observable<List<Event>> getEvents() {
//        mRealm = Realm.getDefaultInstance();
//        return mRealm.where(Event.class).findAllAsync().asObservable()
//                .filter(RealmResults::isLoaded)
//                .first()
//                .map(mRealm::copyFromRealm)
//                .doOnNext(events -> Log.d(TAG, "getEvents() called with size " + events.size()))
//                .doOnNext(events -> mRealm.close());
        return Observable.just(list);
    }

    public long getEventsCount() {
        mRealm = Realm.getDefaultInstance();
        long count = mRealm.where(Event.class).count();
        mRealm.close();
        return count;
    }

    //region deprecated
    LastfmApi lastfmApi = new LastfmApi();

    VkApiBridge vkApiBridge = new VkApiBridge();

    public void GigsToRealm(List<EventRockGig> gigsRockGig) {
        Observable.from(gigsRockGig)
                .subscribeOn(Schedulers.io())
                .map(eventRockGig -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(new Event(eventRockGig)));
                    return eventRockGig;
                })
                .toList()
                .single();
    }

    public void setImages() {
        mRealm.where(BandRealm.class)
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .take(1)
                .flatMap(Observable::from)
                .flatMap(bandRealm -> Observable.just(bandRealm.getName()))
                .observeOn(Schedulers.from(JobExecutor.getInstance()))
                .subscribe(s -> {
                    Realm realm = Realm.getDefaultInstance();
                    BandRealm bRealm = realm.where(BandRealm.class).equalTo("name", s).findFirst();
                    String imageUrl = bRealm.getBandImageUrl();
                    if (imageUrl == null || imageUrl.length() < 1) {
                        if (imageUrl == null || imageUrl.length() < 1 || imageUrl.equals(LastfmApi.DEFAULT_PIC)) {
                            realm.beginTransaction();
                            BandLastfm bandInfo = lastfmApi.getBandInfo(s);
                            bRealm.setBandImageUrl(bandInfo.getImageUrl());
                            realm.commitTransaction();
                        }

                        if (bRealm.getBandImageUrl().equals(LastfmApi.DEFAULT_PIC)) {
                            String bandname = new String(bRealm.getBandvk());
                            vkApiBridge.setImage(bandname)
                                    .subscribeOn(Schedulers.from(JobExecutor.getInstance()))
                                    .subscribe(s1 -> {
                                        Realm vkRealm = Realm.getDefaultInstance();
                                        BandRealm vkbRealm = vkRealm.where(BandRealm.class).equalTo("name", s).findFirst();
                                        vkRealm.beginTransaction();
                                        vkbRealm.setBandImageUrl(s1);
                                        vkRealm.commitTransaction();
                                    });
                        }
                    }
                });
    }

    public void soutGigs() {
        mRealm.where(Event.class).findAllAsync().asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(eventRealms -> Observable.from(eventRealms))
                .take(20);
    }
    //endregion
}
