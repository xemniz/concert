package ru.xmn.concert.model;

import android.util.Log;

import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import gk.android.investigator.Investigator;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import ru.xmn.concert.JobExecutor;
import ru.xmn.concert.model.api.LastfmApi;
import ru.xmn.concert.model.api.RealmApi;
import ru.xmn.concert.model.api.RockGigApi;
import ru.xmn.concert.model.api.VkApiBridge;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.BandLastfm;
import ru.xmn.concert.model.data.BandRealm;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.model.data.EventRockGig;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConcertsModel {
    String TAG = getClass().getSimpleName();
    RockGigApi rockGigApi = new RockGigApi();
    LastfmApi lastfmApi = new LastfmApi();
    List<Band> gigsVkRockgig = new ArrayList<>();
    VkApiBridge vkApiBridge = new VkApiBridge();
    private List<String> vkBandList;

    public Observable eventList(final String band) {
        System.out.println(band + " in eventlist");

        return Observable
                .create(new Observable.OnSubscribe<List<EventGig>>() {
                    @Override
                    public void call(Subscriber<? super List<EventGig>> subscriber) {
                        try {
                            subscriber.onNext(rockGigApi.eventsByBand(band));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.from(JobExecutor.getInstance()));
//                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable bandList(final String band) {
        return Observable
                .create(new Observable.OnSubscribe<List<String>>() {
                    @Override
                    public void call(Subscriber<? super List<String>> subscriber) {
                        try {
                            subscriber.onNext(rockGigApi.findBands(band));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    public Observable getArtistInfo(final String band) throws IOException {
        return lastfmApi.getBandInfoObs(band)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<Band>> getBandsGigsVk(boolean isRefreshing) {
        System.out.println("INCONCERTSMODEL " + Thread.currentThread().getName() + " gigsVkRockgig " + gigsVkRockgig.size());
        List<Band> tmpGigsVkRockGig = new ArrayList<>();
        tmpGigsVkRockGig.addAll(gigsVkRockgig);
        if (isRefreshing) {
            gigsVkRockgig = new ArrayList<Band>() {
            };
            return Observable
                    .zip(vkApiBridge.bandList(), rockGigApi.getEventsRockGig(), (strings, rockGigEvents) -> {
                        System.out.println("CONCMODEL COMBLATEST " + rockGigEvents.size());
                        for (EventRockGig event : rockGigEvents) {
                            for (Band band : event.getBands()) {
                                if (strings.contains(band.getBand().trim().toLowerCase())) {

                                    boolean isBandInList = false;
                                    for (Band bandInList :
                                            gigsVkRockgig) {
                                        if (bandInList.equals(band)) {
                                            bandInList.getGigs().add(event);
                                            isBandInList = true;
                                        }
                                    }
                                    if (!isBandInList) {
                                        band.getGigs().add(event);
                                        gigsVkRockgig.add(band);
                                    }

                                    try {
                                        System.out.println("CONCERTMODEL THREAD IS " + Thread.currentThread().getName());
                                        lastfmApi.getBandInfoObs(band.getBand())
                                                .subscribe(bandLastfm -> band.setBandImageUrl(bandLastfm.getImageUrl()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        System.out.println("CONCERTMODEL BEFORERETURN " + gigsVkRockgig.size());
                        return gigsVkRockgig;
                    }).observeOn(Schedulers.io());
        } else {
            return Observable.just(tmpGigsVkRockGig);
        }

    }


    public Observable<List<String>> getAllEventsRealm(int PAGE, int IT_ON_PAGE) {
        return Observable.create(new Observable.OnSubscribe<List<EventRealm>>() {
            @Override
            public void call(Subscriber<? super List<EventRealm>> subscriber) {
                RealmResults<EventRealm> eventRealms = Realm.getDefaultInstance()
                        .where(EventRealm.class)
                        .greaterThanOrEqualTo("date", new Date(System.currentTimeMillis()))
                        .findAllSorted("date");
                Log.d(TAG, "call: realm results count " + eventRealms.size());
                subscriber.onNext(eventRealms);
                subscriber.onCompleted();
            }
        })
                .flatMap(Observable::from)
                .skip(IT_ON_PAGE * PAGE)
                .take(IT_ON_PAGE)
                .map(eventRealm -> {
                    Log.d(getClass().getSimpleName(), "COUNT IN GETBANDSREALM " + eventRealm.getBandRockGigs().size());
                    Observable.from(eventRealm.getBandRockGigs())
                            .flatMap(bandRealm -> Observable.just(bandRealm.getName()))
                            .map(this::loadImagesToRealm)
                            .toList().toBlocking().single();
                    return eventRealm;
                })
                .flatMap(eventRealm -> Observable.just(new String(eventRealm.getName())))
                .toList()
                .single();
    }

    public Observable<List<String>> getEventsRealmVk(int PAGE, int IT_ON_PAGE){
        if (PAGE == 1){
//            vkApiBridge.bandList().subscribe(strings -> vkBandList = strings);
//            vkApiBridge.bandList().subscribeOn(Schedulers.io()).single().toBlocking().forEach(strings -> vkBandList = strings);
//            vkBandList = vkApiBridge.bandList().subscribeOn(Schedulers.io()).toBlocking().subscribe(strings -> vkBandList = strings);
            vkApiBridge.bandList().subscribeOn(Schedulers.io()).toBlocking().subscribe(strings -> vkBandList = strings);
            Investigator.log(this, "vkbandlist.size", vkBandList.size());
        }

        if (vkBandList==null){
            vkBandList = new ArrayList<>();
            Investigator.log(this, "if vkbandlist null", vkBandList.size());
        }

        Investigator.log(this, "return vkbandlist", vkBandList.size());
        return getEventRealmFromList(PAGE, IT_ON_PAGE, vkBandList);
    }

    public Observable<List<String>> getEventRealmFromList(int PAGE, int IT_ON_PAGE, List<String> list) {
        return Observable.create(new Observable.OnSubscribe<List<EventRealm>>() {
            @Override
            public void call(Subscriber<? super List<EventRealm>> subscriber) {
                RealmQuery<EventRealm> query = Realm.getDefaultInstance().where(EventRealm.class);
                query.equalTo("name", "werynonrilnamee");
                for (String bName :
                        list) {
                    query.or().equalTo("name", bName);
                }
                RealmResults<EventRealm> eventRealms = query
                        .greaterThanOrEqualTo("date", new Date(System.currentTimeMillis()))
                        .findAllSorted("date");
                Log.d(TAG, "call: realm results count " + eventRealms.size());
                subscriber.onNext(eventRealms);
                subscriber.onCompleted();
            }
        })
                .flatMap(Observable::from)
                .skip(IT_ON_PAGE * PAGE)
                .take(IT_ON_PAGE)
                .map(eventRealm -> {
                    Log.d(getClass().getSimpleName(), "COUNT IN GETBANDSREALM " + eventRealm.getBandRockGigs().size());
                    Observable.from(eventRealm.getBandRockGigs())
                            .flatMap(bandRealm -> Observable.just(bandRealm.getName()))
                            .map(this::loadImagesToRealm)
                            .toList().toBlocking().single();
                    return eventRealm;
                })
                .flatMap(eventRealm -> Observable.just(new String(eventRealm.getName())))
                .toList()
                .single();
    }

    private String loadImagesToRealm (String s ) {
        Realm realm = Realm.getDefaultInstance();
        BandRealm bandRealm = realm.where(BandRealm.class).equalTo("name", s).findFirst();
        if (bandRealm.getBandImageUrl() == null || bandRealm.getBandImageUrl().length() < 1) {
            realm.beginTransaction();
            BandLastfm bandInfo = lastfmApi.getBandInfo(s);
            bandRealm.setBandImageUrl(bandInfo.getImageUrl());
            realm.commitTransaction();
        }

        if (bandRealm.getBandImageUrl().equals(LastfmApi.DEFAULT_PIC)) {
            String bandname = new String(bandRealm.getBandvk());
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
        return s;
    }
}
