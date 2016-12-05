package ru.xmn.concert.gigs;

import android.support.annotation.NonNull;
import android.util.Log;

import ru.xmn.concert.gigs.filter.GigsFilter;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.fernandocejas.frodo.core.checks.Preconditions.checkNotNull;

/**
 * Created by xmn on 18.11.2016.
 */

public class GigsPresenter implements GigsContract.Presenter {
    private static final String TAG = "GigsPresenter";
    private final GigsModel mModel;
    private final GigsContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public GigsPresenter(@NonNull GigsModel model,
                         @NonNull GigsContract.View view) {
        mModel = checkNotNull(model, "GigsRepository cannot be null");
        mView = checkNotNull(view, "GigsView cannot be null!");

        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }

    //region Contract
    @Override
    public void loadGigs() {
        mView.setLoadingIndicator(true);
        Subscription sub1 = mModel.loadGigs()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(events -> mView.showGigs(mModel.getEvents(), mModel.isLoadMore(), true));
        mSubscriptions.add(sub1);
    }

    @Override
    public void loadNextGigs() {
        Log.d(TAG, "loadNextGigs() called");
        if (mModel.isLoadMore()) {
            mModel.loadNextGigs();
            mView.showNextGigs(mModel.isLoadMore());
        } else {
            mView.showNextGigs(mModel.isLoadMore());
        }
    }

    @Override
    public void openGigDetails(@NonNull Event requestedGig) {
        mView.showGigDetails(requestedGig.getEventid());
    }

    @Override
    public void setFilter(GigsFilter filter) {

    }

    @Override
    public void changeFilter(int id, boolean isApply) {
        mModel.changeFilter(id, isApply);
    }

    @Override
    public void subscribe() {
        if (mModel.getEvents() == null) {
            mView.onSubscribed();
        }
        else mView.showGigs(mModel.getEvents(), mModel.isLoadMore(), false);
    }

    @Override
    public void unsubscribe() {

    }

    //endregion

}
