package ru.xmn.concert.presenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.view.SearchActivity;
import ru.xmn.concert.view.SearchView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by xmn on 24.05.2016.
 */

public class SearchPresenter {
    SearchView view;
    ConcertsModel concertsModel = new ConcertsModel();
    private Subscription subscription = Subscriptions.empty();

    public SearchPresenter(SearchView view) {
        this.view = view;
    }

    public void bandList(String band){
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = concertsModel.bandList(band)
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<String> data) {
                        if (data != null && !data.isEmpty()) {
                            view.showData(data);
                        } else {
                        }
                    }
                });
    }
}
