package ru.xmn.concert.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.model.data.BandRockGig;
import ru.xmn.concert.model.data.EventRockGig;
import ru.xmn.concert.view.BandsView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmn on 19.05.2016.
 */

@InjectViewState
public class PresenterVkFragment extends MvpPresenter<BandsView> {
    ConcertsModel concertsModel = new ConcertsModel();
    final int PAGE_SIZE = 15;
    private Subscription subscription = Subscriptions.empty();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        eventList(0);

//        subscription = concertsModel
//                .getBandsGigsVk(true)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(bandRockGigs -> bandRockGigs.subList(0, PAGE_SIZE))
//                .subscribe(new Observer<List<BandRockGig>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        getViewState().showError(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<BandRockGig> data) {
//                        if (data != null && !data.isEmpty()) {
//                            getViewState().setBandRockGigs(data);
//                        }
//                    }
//                });

    }

    public void bandList(int page) {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        subscription = concertsModel
                .getBandsGigsVk(false)
                .map(bands -> {
                    if (page * PAGE_SIZE < bands.size())
                        if (bands.size() - page * PAGE_SIZE > PAGE_SIZE)
                            return bands.subList(page * PAGE_SIZE, page * PAGE_SIZE + PAGE_SIZE);
                        else
                            return bands.subList(page * PAGE_SIZE, bands.size());
                    else
                        return new ArrayList<BandRockGig>();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .map(bandRockGigs -> bandRockGigs.subList(10, 17))
                .subscribe(new Observer<List<BandRockGig>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getViewState().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BandRockGig> data) {
                        if (data != null && !data.isEmpty()) {
                            getViewState().addBands(data);
                        }
                    }
                });
    }

    public void eventList (int page) {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = concertsModel
                .getAllRockGigEvents(page+1, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EventRockGig>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getViewState().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<EventRockGig> eventRockGigs) {
                        getViewState().addGigs(eventRockGigs);
                    }
                });
    }

    public void closeError() {
        getViewState().hideError();
    }
}
