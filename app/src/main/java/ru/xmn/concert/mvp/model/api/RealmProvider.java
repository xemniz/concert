package ru.xmn.concert.mvp.model.api;

import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import ru.xmn.concert.JobExecutor;
import ru.xmn.concert.gigs.filter.GigsFilter;
import ru.xmn.concert.mvp.model.data.BandLastfm;
import ru.xmn.concert.mvp.model.data.Band;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RealmProvider {
    //    private volatile Realm mRealm = Realm.getDefaultInstance();
    private static final String TAG = "RealmProvider";

    //test mock
//    ArrayList<Event> list = new ArrayList<Event>() {{
//        for (int i = 0; i < 110; i++) {
//            Event e = new Event();
//            e.setName(i + "");
//            add(e);
//        }
//    }};

    public <T extends RealmObject> void toRealm(T t) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(t);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public <T extends RealmObject> void ListToRealm(List<T> t) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(t);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public synchronized Observable<List<Event>> getEvents(GigsFilter filter) {

        if (filter.isDisabled()) {
            return Observable.create(subscriber -> {
                try {
                    Realm localRealm1 = Realm.getDefaultInstance();
                    List<Event> events = localRealm1.copyFromRealm(localRealm1.where(Event.class).findAll());
                    localRealm1.close();
                    subscriber.onNext(events);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            });
        } else
            return filter.getFilterList()
                    .flatMap(list -> {
                        Realm localRealm2 = Realm.getDefaultInstance();
                        if (list.size() < 1)
                            return Observable.just(new ArrayList<>());

                        RealmQuery<Event> q = localRealm2.where(Event.class);
                        listFilterQuery(list, q, "bandRockGigs.name");

                        List<Event> events = localRealm2.copyFromRealm(q.findAll());
                        localRealm2.close();
                        return Observable.just(events);
                    });

//        return Observable.just(list);
    }

    public synchronized Observable<RealmResults<Event>> getQuery(GigsFilter filter, Realm realm) {

        if (filter.isDisabled()) {
            return Observable.just(realm.where(Event.class).findAll());
        } else
            return filter.getFilterList()
                    .map(list -> list.toArray(new String[list.size()]) )
                    .map(list -> {
                        Realm localRealm = Realm.getDefaultInstance();
                        RealmResults<Band> bands = localRealm.where(Band.class).in("band", list , Case.INSENSITIVE).findAll();
                        String[] strings = new String[bands.size()];
                        for (int i = 0; i < bands.size(); i++) {
                            strings[i] = bands.get(i).getBand();
                        }
                        return strings;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(list -> Observable.just(
                        realm.where(Event.class)
                                .in("bands.band", list , Case.INSENSITIVE)
                                .findAll()));
    }

    private <T extends RealmObject> RealmQuery<T> listFilterQuery(List<String> list, RealmQuery<T> q, String field) {
        Log.d(TAG, "listFilterQuery() called with: list = [" + list + "], q = [" + q + "], field = [" + field + "]");
        if (list.size() < 1)
            throw new RuntimeException("Empty list");

        q.equalTo(field, list.get(0), Case.INSENSITIVE);
        if (list.size() > 1)
            for (int i = 1; i < list.size(); i++)
                q.or().equalTo(field, list.get(i), Case.INSENSITIVE);

        return q;
    }

    public long getEventsCount() {
        Realm mRealm = Realm.getDefaultInstance();
        long count = mRealm.where(Event.class).count();
        mRealm.close();
        return count;
    }


    //region deprecated
    LastfmApi lastfmApi = new LastfmApi();

    VkApiBridge vkApiBridge = new VkApiBridge();

//    public void setImages() {
//        mRealm.where(Band.class)
//                .findAllAsync()
//                .asObservable()
//                .filter(RealmResults::isLoaded)
//                .take(1)
//                .flatMap(Observable::from)
//                .flatMap(bandRealm -> Observable.just(bandRealm.getName()))
//                .observeOn(Schedulers.from(JobExecutor.getInstance()))
//                .subscribe(s -> {
//                    Realm realm = Realm.getDefaultInstance();
//                    Band bRealm = realm.where(Band.class).equalTo("name", s).findFirst();
//                    String imageUrl = bRealm.getBandImageUrl();
//                    if (imageUrl == null || imageUrl.length() < 1) {
//                        if (imageUrl == null || imageUrl.length() < 1 || imageUrl.equals(LastfmApi.DEFAULT_PIC)) {
//                            realm.beginTransaction();
//                            BandLastfm bandInfo = lastfmApi.getBandInfo(s);
//                            bRealm.setBandImageUrl(bandInfo.getImageUrl());
//                            realm.commitTransaction();
//                        }
//
//                        if (bRealm.getBandImageUrl().equals(LastfmApi.DEFAULT_PIC)) {
//                            String bandname = new String(bRealm.getBandvk());
//                            vkApiBridge.setImage(bandname)
//                                    .subscribeOn(Schedulers.from(JobExecutor.getInstance()))
//                                    .subscribe(s1 -> {
//                                        Realm vkRealm = Realm.getDefaultInstance();
//                                        Band vkbRealm = vkRealm.where(Band.class).equalTo("name", s).findFirst();
//                                        vkRealm.beginTransaction();
//                                        vkbRealm.setBandImageUrl(s1);
//                                        vkRealm.commitTransaction();
//                                    });
//                        }
//                    }
//                });
//    }

//    public void soutGigs() {
//        mRealm.where(Event.class).findAllAsync().asObservable()
//                .filter(RealmResults::isLoaded)
//                .flatMap(eventRealms -> Observable.from(eventRealms))
//                .take(20);
//    }
    //endregion
}
