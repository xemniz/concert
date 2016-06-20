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
    VkApiBridge vkApiBridge = new VkApiBridge();
    static public final Realm myRealm = Realm.getDefaultInstance();
    final String TAG = getClass().getSimpleName();

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
                    Realm realm = Realm.getDefaultInstance();
                    BandRealm bRealm = realm.where(BandRealm.class).equalTo("name", s).findFirst();
                    String imageUrl = bRealm.getBandImageUrl();
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
                });
    }

    public void soutGigs() {
        myRealm.where(EventRealm.class).findAllAsync().asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(eventRealms -> Observable.from(eventRealms))
                .take(20)
                .subscribe(eventRealm -> System.out.println(eventRealm.toString()));
    }
}
