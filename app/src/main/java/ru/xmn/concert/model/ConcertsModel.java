package ru.xmn.concert.model;

import android.util.Log;

import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import gk.android.investigator.Investigator;
import io.realm.Case;
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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public enum ConcertsModel {
    I;
    String TAG = getClass().getSimpleName();
    RockGigApi rockGigApi = new RockGigApi();
    LastfmApi lastfmApi = new LastfmApi();
    List<Band> gigsVkRockgig = new ArrayList<>();
    VkApiBridge vkApiBridge = new VkApiBridge();
    RealmApi mRealmApi = new RealmApi();
    private Realm mRealm = RealmApi.myRealm;
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
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getArtistInfo(final String band) throws IOException {
        return lastfmApi.getBandInfoObs(band)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //Получить все события постранично
    public Observable<List<String>> getAllEventsRealm(int PAGE, int IT_ON_PAGE) {
        return Observable.create(new Observable.OnSubscribe<List<EventRealm>>() {
            @Override
            public void call(Subscriber<? super List<EventRealm>> subscriber) {
                RealmResults<EventRealm> eventRealms = Realm.getDefaultInstance()
                        .where(EventRealm.class)
//                        .greaterThanOrEqualTo("date", new Date(System.currentTimeMillis()))
                        .findAllSorted("date");
                Log.d(TAG, "call: realm results count " + eventRealms.size());
                subscriber.onNext(eventRealms);
                subscriber.onCompleted();
            }
        })
                .flatMap(Observable::from)
                .skip(IT_ON_PAGE * PAGE)
                .take(IT_ON_PAGE)
                .flatMap(eventRealm -> Observable.just(new String(eventRealm.getName())))
                .toList()
                .single();
    }

    public Observable<List<EventRealm>> getAllEventsRealmAsync(int PAGE, int IT_ON_PAGE) {
        return Realm.getDefaultInstance()
                .where(EventRealm.class)
                .findAllSorted("date")
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::from)
                .skip(IT_ON_PAGE * PAGE)
                .take(IT_ON_PAGE)
                .toList()
                .single();
    }

    //Получить события исполнителей из списка вк постранично
    public Observable<List<String>> getEventsRealmVk(int PAGE, int IT_ON_PAGE){
        if (PAGE == 1){
            new Thread(() -> {}).run();
            vkApiBridge.bandList().toBlocking().subscribe(strings -> {
                vkBandList = strings;
            });
        }

        if (vkBandList==null){
            vkBandList = new ArrayList<>();
        }

        Investigator.log(this, "return vkbandlist", vkBandList.size());
        return getEventRealmFromList(PAGE, IT_ON_PAGE, vkBandList);
    }

    //Получить события из любого листа
    public Observable<List<String>> getEventRealmFromList(int PAGE, int IT_ON_PAGE, List<String> list) {
        return Observable.create(new Observable.OnSubscribe<List<EventRealm>>() {
            @Override
            public void call(Subscriber<? super List<EventRealm>> subscriber) {
                RealmQuery<EventRealm> query = Realm.getDefaultInstance().where(EventRealm.class);
                query.equalTo("bandRockGigs.name", "unreaaa", Case.INSENSITIVE);
                for (String bName :
                        list) {
                    query.or().equalTo("bandRockGigs.name", bName, Case.INSENSITIVE);
                }
                RealmResults<EventRealm> eventRealms = query
//                        .greaterThanOrEqualTo("date", new Date(System.currentTimeMillis()))
                        .findAllSorted("date");
                subscriber.onNext(eventRealms);
                subscriber.onCompleted();
            }
        })
                .flatMap(Observable::from)
                .skip(IT_ON_PAGE * (PAGE))
                .take(IT_ON_PAGE)
                .map(eventRealm -> {
                    Log.d(getClass().getSimpleName(), "COUNT IN GETBANDSREALM " + eventRealm.getBandRockGigs().size());
                    return eventRealm;
                })
                .flatMap(eventRealm -> Observable.just(eventRealm.getName()))
                .toList()
                .single();
    }

    //Загрузка фото для исполнителя
    private String loadImagesToRealm (String bandName ) {
        Realm realm = Realm.getDefaultInstance();
        BandRealm bandRealm = realm.where(BandRealm.class).equalTo("name", bandName).findFirst();
        if (bandRealm.getBandImageUrl() == null || bandRealm.getBandImageUrl().length() < 1) {
            realm.beginTransaction();
            BandLastfm bandInfo = lastfmApi.getBandInfo(bandName);
            bandRealm.setBandImageUrl(bandInfo.getImageUrl());
            realm.commitTransaction();
        }

        if (bandRealm.getBandImageUrl().equals(LastfmApi.DEFAULT_PIC)) {
            String bandname = bandRealm.getBandvk();
            vkApiBridge.setImage(bandname)
                    .subscribeOn(Schedulers.from(JobExecutor.getInstance()))
                    .subscribe(s1 -> {
                        Realm vkRealm = Realm.getDefaultInstance();
                        BandRealm vkbRealm = vkRealm.where(BandRealm.class).equalTo("name", bandName).findFirst();
                        vkRealm.beginTransaction();
                        vkbRealm.setBandImageUrl(s1);
                        vkRealm.commitTransaction();
                    });
        }
        return bandName;
    }

    //Загрузка событий в базу
    public Observable<Boolean> gigsToRealm () {
        return rockGigApi.eventRgToRealm();
    }
}
