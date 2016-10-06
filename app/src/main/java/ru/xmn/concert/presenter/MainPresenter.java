package ru.xmn.concert.presenter;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.view.EventListView;
import ru.xmn.concert.viewimpl.MainFragment;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter {
    private ConcertsModel mModel;
    private EventListView mView;
    private Subscription mSubscription;
    private Observable<RealmResults<EventRealm>> mEventRealmList;
    Realm realm = Realm.getDefaultInstance();

    public MainPresenter(EventListView view) {
        mModel = ConcertsModel.I;
        mView = view;
    }

    public void onViewCreated(int i, int onPage) {

        mSubscription = mModel.gigsToRealm().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (mEventRealmList == null) {
                    mEventRealmList = realm
                            .where(EventRealm.class)
                            .findAllSortedAsync("date")
                            .asObservable()
                            .filter(RealmResults::isLoaded)
                            .first()
                            .map(eventRealms -> {
                                mView.setListSize(eventRealms.size());
                                return eventRealms;
                            })
                            .cache();
                }
                loadEventList(i, onPage);
            }
        });
    }

    private void loadEventList(int i, int onPage) {
        if (!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
        mSubscription = mEventRealmList
                .map(eventRealms -> eventRealms.subList(i * onPage, i * onPage + onPage))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<EventRealm>>() {
                    @Override
                    public void onCompleted() {
//                        if (!realm.isClosed())
//                            realm.close();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<EventRealm> eventRealms) {
                        mView.loadEventList(eventRealms);
                    }
                });

    }

    public void addMoreData(int i, int onPage) {
        loadEventList(i, onPage);
    }
}