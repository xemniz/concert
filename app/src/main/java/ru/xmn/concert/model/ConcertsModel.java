package ru.xmn.concert.model;

import android.util.Log;

import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import io.realm.Realm;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConcertsModel {
    String TAG = getClass().getSimpleName();
    RockGigApi rockGigApi = new RockGigApi();
    LastfmApi lastfmApi = new LastfmApi();
    VKList<VKApiAudio> list;
    List<Band> gigsVkRockgig = new ArrayList<>();
    VkApiBridge vkApiBridge = new VkApiBridge();
    RealmApi realmApi = new RealmApi();

    public VKList<VKApiAudio> getList() {
        return list;
    }

    public void setList(VKList<VKApiAudio> list) {
        this.list = list;
    }

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

    public Observable<List<EventRockGig>> getAllRockGigEvents(int PAGE, int IT_ON_PAGE) {
        return rockGigApi.getEventsRockGig()
                .flatMap(Observable::from)
                .skip(IT_ON_PAGE * PAGE)
                .take(IT_ON_PAGE)
                .map(rockGigEvent -> {
                    for (Band band : rockGigEvent.getBands()) {
                        try {
                            lastfmApi.getBandInfoObs(band.getBand())
                                    .subscribe(bandLastfm -> {
                                        band.setBandImageUrl(bandLastfm.getImageUrl());
                                        System.out.println("BAND URL ." + bandLastfm.getImageUrl() + ".");
                                        if (band.getBandImageUrl().length() < 3 && band.getBandImageUrl().equals("")) {
                                            band.setBandImageUrl("http://blog.songcastmusic.com/wp-content/uploads/2013/08/iStock_000006170746XSmall.jpg");
                                        }
                                        System.out.println("BAND URL " + bandLastfm.getImageUrl());
                                    });
                        } catch (IOException e) {
                            e.printStackTrace();
                            band.setBandImageUrl("http://p4cdn4static.sharpschool.com/UserFiles/Servers/Server_91869/Image/Band4.jpg");
                        }
                    }
                    return rockGigEvent;
                })
                .toList();
    }

    public Observable<List<EventRealm>> getAllEventsRealm(int PAGE, int IT_ON_PAGE) {
        return Observable.create(new Observable.OnSubscribe<List<EventRealm>>() {
            @Override
            public void call(Subscriber<? super List<EventRealm>> subscriber) {
                RealmResults<EventRealm> eventRealms = Realm.getDefaultInstance()
                        .where(EventRealm.class)
                        .greaterThanOrEqualTo("date", new Date(System.currentTimeMillis()))
                        .findAllSorted("date");
                Log.d(TAG, "call: realm results count "+eventRealms.size());
                subscriber.onNext(eventRealms);
                subscriber.onCompleted();
            }
        })
                .flatMap(Observable::from)
                .skip(IT_ON_PAGE * PAGE)
                .take(IT_ON_PAGE)
                .map(eventRealm -> {
                    Log.d(getClass().getSimpleName(), "COUNT IN GETBANDSREALM "+eventRealm.getBandRockGigs().size());
                    Observable.from(eventRealm.getBandRockGigs())
                            .flatMap(bandRealm -> Observable.just(bandRealm.getName()))
                            .map(s -> {
                                Log.d(getClass().getSimpleName(), "Thread of lastfm image getting "+ Thread.currentThread().toString());
                                BandLastfm bandInfo = lastfmApi.getBandInfo(s);
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                BandRealm bandRealm = realm.where(BandRealm.class).equalTo("name", s).findFirst();
                                if (bandRealm.getBandImageUrl() == null || bandRealm.getBandImageUrl().length()<1)
                                    bandRealm.setBandImageUrl(bandInfo.getImageUrl());
                                realm.commitTransaction();
                                return s;
                            })
                    .toList().toBlocking().single();
                    return eventRealm;
                })
                .toList()
                .single();
    }
}
