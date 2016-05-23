package ru.xmn.concert.presenter;

import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.view.MainView;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import java.util.List;

/**
 * Created by xmn on 19.05.2016.
 */
public class Presenter {
    ConcertsModel concertsModel = new ConcertsModel();

    private MainView mainView;

    public Presenter(MainView mainView) {
        this.mainView = mainView;
    }


    private Subscription subscription = Subscriptions.empty();

    public void onstart(String band) {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        subscription = concertsModel.eventList(band)
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
                            mainView.showData(data);
                        } else {
//                            mainView.showEmptyList();
                        }
                    }
                });
    }

}
