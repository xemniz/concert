package ru.xmn.concert.model.api;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import ru.xmn.concert.Application;
import ru.xmn.concert.JobExecutor;
import ru.xmn.concert.model.data.BandLastfm;
import ru.xmn.concert.model.data.BandRealm;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.model.data.EventRockGig;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RealmApi {
    LastfmApi lastfmApi = new LastfmApi();
    static public final Realm myRealm = Realm.getDefaultInstance();

    public void GigsToRealm(List<EventRockGig> gigsRockGig) {
        Log.d(this.getClass().getSimpleName(), "Gigs to realm " + Thread.currentThread());
        Observable.from(gigsRockGig)
                .subscribeOn(Schedulers.io())
                .map(eventRockGig -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(new EventRealm(eventRockGig)));
                    return eventRockGig;
                })
                .toList().toBlocking().single();
    }

    public void setImages() {
        myRealm.where(BandRealm.class)
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .take(1)
                .flatMap(Observable::from)
                .flatMap(bandRealm -> Observable.just(bandRealm.getName()))
                .observeOn(Schedulers.from(JobExecutor.getInstance()))
                .subscribe(s -> {
                    BandLastfm bandInfo = lastfmApi.getBandInfo(s);
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.where(BandRealm.class).equalTo("name", s).findFirst().setBandImageUrl(bandInfo.getImageUrl());
                    realm.commitTransaction();
                });

    }
}
