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
import ru.xmn.concert.view.BandsView;
import ru.xmn.concert.view.adapters.BandsEventsAdapter;
import ru.xmn.concert.view.adapters.EventsBandsAdapter;
import ru.xmn.concert.view.adapters.EventsRealmAdapter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class PresenterVkFragment extends MvpPresenter<BandsView> {
    ConcertsModel concertsModel = new ConcertsModel();
    final int PAGE_SIZE = 15;
    private Subscription subscription = Subscriptions.empty();
    private boolean mIsInLoading;


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadGigs(false);
    }

    private void loadGigs(boolean isRefreshing) {
        eventList(0, isRefreshing);
    }

    public void eventList(int page, boolean isRefreshing) {
        if (mIsInLoading)
        {
            return;
        }
        mIsInLoading = true;

        getViewState().hideError();
        getViewState().onStartLoading();

        showProgress();
        
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        subscription = concertsModel.getAllEventsRealm(page + 1, PAGE_SIZE)
                .map(eventRealms -> {
                    Log.d(getClass().getSimpleName(), "EVENTREALMS SIZE " + eventRealms.size());
                    return eventRealms;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getViewState().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<String> eventRealms) {
                        onLoadingFinish(isRefreshing);
                        onLoadingSuccess(eventRealms);
                    }
                });
    }

    private void onLoadingFinish(boolean isRefreshing) {
        Investigator.log(this);

        mIsInLoading = false;

        getViewState().onFinishLoading();

        hideProgress();
    }

    private void onLoadingSuccess(List<String> eventRealms) {
        getViewState().addGigsRealm(eventRealms);
    }

    private void hideProgress() {
        getViewState().hideRefreshing();
    }

    private void showProgress() {
            getViewState().showRefreshing();
    }

    public void setAdapter (RecyclerView.Adapter adapter) {
        getViewState().setAdapter(adapter);
    }

    public void closeError() {
        getViewState().hideError();
    }
}
