package ru.xmn.concert.presenter;

import java.util.List;

import de.umass.lastfm.Artist;
import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.view.BandView;
import ru.xmn.concert.view.MainView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by xmn on 25.05.2016.
 */



public class BandPresenter {


    private BandView mainView;

    public BandPresenter(BandView mainView) {
        this.mainView = mainView;
    }

    ConcertsModel concertsModel = new ConcertsModel();
    private Subscription subscription = Subscriptions.empty();

    public void getBandInfo (String band){
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        concertsModel.getArtistInfo(band).subscribe(new Observer<Band>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Band data) {
                if (data != null) {
                    mainView.showData(data);
                } else {
                }
            }
        });
    }

    public void getBandEvents(String band) {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        subscription = concertsModel.eventList(band)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EventGig>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<EventGig> data) {
                        if (data != null && !data.isEmpty()) {
                            mainView.showEvents(data);
                        } else {
                        }
                    }
                });
    }
}
