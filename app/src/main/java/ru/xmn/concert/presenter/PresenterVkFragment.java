package ru.xmn.concert.presenter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import gk.android.investigator.Investigator;
import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.model.data.EventRockGig;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.util.ArrayList;
import java.util.List;

//@InjectViewState
//public class PresenterVkFragment extends MvpPresenter<BandsView> {
//    ConcertsModel concertsModel = new ConcertsModel();
//    final int PAGE_SIZE = 15;
//    private Subscription subscription = Subscriptions.empty();
//    private boolean mIsInLoading;
//
//
//    @Override
//    protected void onFirstViewAttach() {
//        super.onFirstViewAttach();
//        loadGigs(false);
//    }
//
//    private void loadGigs(boolean isRefreshing) {
//        eventList(0, isRefreshing);
//    }
//
//    public void eventList(int page, boolean isRefreshing) {
//        if (!subscription.isUnsubscribed()) {
//            subscription.unsubscribe();
//        }
//
//        subscription = concertsModel.getEventsRealmVk(page + 1, PAGE_SIZE)
//                .map(eventRealms -> {
//                    Log.d(getClass().getSimpleName(), "EVENTREALMS SIZE " + eventRealms.size());
//                    return eventRealms;
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<String>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        getViewState().showError(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<String> eventRealms) {
//                        onLoadingFinish(isRefreshing);
//                        onLoadingSuccess(eventRealms);
//                    }
//                });
//    }
//}
