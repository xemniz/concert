package ru.xmn.concert.gigs;

import android.util.Log;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import ru.xmn.concert.JobExecutor;
import ru.xmn.concert.gigs.filter.GigsFilter;
import ru.xmn.concert.mvp.model.DataManager;
import ru.xmn.concert.mvp.model.api.LastfmApi;
import ru.xmn.concert.mvp.model.api.RealmProvider;
import ru.xmn.concert.mvp.model.api.RockGigProvider;
import ru.xmn.concert.mvp.model.api.VkApiBridge;
import ru.xmn.concert.mvp.model.data.Band;
import ru.xmn.concert.mvp.model.data.BandLastfm;
import ru.xmn.concert.mvp.model.data.BandRealm;
import ru.xmn.concert.mvp.model.data.Event;
import ru.xmn.concert.mvp.model.data.EventGig;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GigsModel {
    private static final String TAG = "GigsModel";
    private final DataManager mDataManager;
    private List<Event> mEvents;

    //paginator
    private int ONPAGE = 20;
    private volatile int CURPAGE = 0;
    private int COUNT = 0;

    private GigsFilter mFilter;
    private Observable<List<Event>> mCachedObservableEvents;
    private boolean mIsLoadMore = true;
    private List<Event> mCachedEvents;

    //region singleton
    private GigsModel() {
        mDataManager = DataManager.getInstance();
    }

    final static GigsModel INSTANCE = new GigsModel();

    public static GigsModel getInstance() {
        return INSTANCE;
    }
    //endregion

    //region getters, setters
    private void setEvents(List<Event> events) {
        mEvents = new ArrayList<>(events);
    }

    List<Event> getEvents() {
        return mEvents;
    }
    //endregion

    //region loadEvents
    Observable<List<Event>> loadGigs(GigsFilter filter) {
        COUNT = 0;
        CURPAGE = 0;
        if (mFilter == null || !filter.equals(mFilter)) {
            mFilter = filter;
            mCachedObservableEvents = mDataManager.getAllEvents(mFilter);
        }

        return mCachedObservableEvents
                .doOnNext(events -> COUNT = events.size())
                .doOnNext(this::setCachedEvents)
                .map(events -> events.subList(0, getNextPageSize()))
                .doOnNext(this::setEvents)
                .doOnNext(e -> CURPAGE++);
    }

    private void setCachedEvents(List<Event> events) {
        mCachedEvents = new ArrayList<>(events);
    }

    void loadNextGigs() {
        mEvents.addAll(new ArrayList<>(mCachedEvents.subList(ONPAGE * CURPAGE, ONPAGE * CURPAGE + getNextPageSize())));
        CURPAGE++;
    }

    private int getNextPageSize() {
        if (ONPAGE < COUNT - ONPAGE * CURPAGE) {
            Log.d(TAG, "getNextPageSize() returned: " + ONPAGE);
            return ONPAGE;
        } else {
            mIsLoadMore = false;
            Log.d(TAG, "getNextPageSize() returned: " + (COUNT - ONPAGE * CURPAGE));
            return COUNT - ONPAGE * CURPAGE;
        }
    }

    boolean isLoadMore() {
        return mIsLoadMore;
    }
    //endregion

    //region Deprecated
    private List<String> vkBandList;
    RockGigProvider mRockGigProvider = new RockGigProvider();
    LastfmApi lastfmApi = new LastfmApi();

    List<Band> gigsVkRockgig = new ArrayList<>();

    VkApiBridge vkApiBridge = new VkApiBridge();

    RealmProvider mRealmProvider = new RealmProvider();

    public Observable eventList(final String band) {
        return Observable
                .create(new Observable.OnSubscribe<List<EventGig>>() {
                    @Override
                    public void call(Subscriber<? super List<EventGig>> subscriber) {
                        try {
                            subscriber.onNext(mRockGigProvider.eventsByBand(band));
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
                            subscriber.onNext(mRockGigProvider.findBands(band));
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
        return Observable.create(new Observable.OnSubscribe<List<Event>>() {
            @Override
            public void call(Subscriber<? super List<Event>> subscriber) {
                RealmResults<Event> events = Realm.getDefaultInstance()
                        .where(Event.class)
//                        .greaterThanOrEqualTo("date", new Date(System.currentTimeMillis()))
                        .findAllSorted("date");
                Log.d(TAG, "call: realm results count " + events.size());
                subscriber.onNext(events);
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

    public Observable<List<Event>> getAllEventsRealmAsync(int PAGE, int IT_ON_PAGE) {
        return Realm.getDefaultInstance()
                .where(Event.class)
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
    public Observable<List<String>> getEventsRealmVk(int PAGE, int IT_ON_PAGE) {
        if (PAGE == 1) {
            new Thread(() -> {
            }).run();
            vkApiBridge.bandList().toBlocking().subscribe(strings -> {
                vkBandList = strings;
            });
        }

        if (vkBandList == null) {
            vkBandList = new ArrayList<>();
        }

        return getEventRealmFromList(PAGE, IT_ON_PAGE, vkBandList);
    }

    //Получить события из любого листа
    public Observable<List<String>> getEventRealmFromList(int PAGE, int IT_ON_PAGE, List<String> list) {
        return Observable.create(new Observable.OnSubscribe<List<Event>>() {
            @Override
            public void call(Subscriber<? super List<Event>> subscriber) {
                RealmQuery<Event> query = Realm.getDefaultInstance().where(Event.class);
                query.equalTo("bandRockGigs.name", "unreaaa", Case.INSENSITIVE);
                for (String bName :
                        list) {
                    query.or().equalTo("bandRockGigs.name", bName, Case.INSENSITIVE);
                }
                RealmResults<Event> events = query
//                        .greaterThanOrEqualTo("date", new Date(System.currentTimeMillis()))
                        .findAllSorted("date");
                subscriber.onNext(events);
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
    private String loadImagesToRealm(String bandName) {
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

    //    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public Observable<List<String>> vkBandList() {
        return vkApiBridge.bandList();
    }

    //Загрузка событий в базу
//    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public Observable<Boolean> gigsToRealm() {
        BehaviorSubject<Boolean> gigsInRealmSubj = BehaviorSubject.create();
        gigsInRealmSubj.onNext(true);
//        gigsInRealmSubj.onNext(mRockGigProvider.eventRgToRealm().subscribeOn(Schedulers.io()).toBlocking().single());
        return gigsInRealmSubj;
    }
    //endregion
}
