package ru.xmn.concert.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.IOException;
import java.util.List;

import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.model.data.BandLastfm;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.view.BandView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by xmn on 25.05.2016.
 */


@InjectViewState
public class BandPresenter extends MvpPresenter<BandView> {


//    private BandView mainView;

//    public BandPresenter(BandView mainView) {
//        this.mainView = mainView;
//    }

    ConcertsModel concertsModel = new ConcertsModel();
    private Subscription subscription = Subscriptions.empty();

    public void getBandInfo (String band) throws IOException {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        concertsModel.getArtistInfo(band).subscribe(new Observer<BandLastfm>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(BandLastfm data) {
                if (data != null) {
                    getViewState().showData(data);
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
                            getViewState().showEvents(data);
                        } else {
                        }
                    }
                });
    }
}
