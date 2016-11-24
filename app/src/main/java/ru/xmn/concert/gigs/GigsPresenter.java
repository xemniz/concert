package ru.xmn.concert.gigs;

import android.support.annotation.NonNull;

import ru.xmn.concert.gigs.filter.GigsFilter;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.fernandocejas.frodo.core.checks.Preconditions.checkNotNull;

/**
 * Created by xmn on 18.11.2016.
 */

public class GigsPresenter implements GigsContract.Presenter {

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
    public void loadGigs(GigsFilter filter) {
        Subscription sub1 = mModel.loadGigs(filter)
                .subscribe(events -> mView.showGigs(mModel.getEvents(), mModel.isLoadMore()));
        mSubscriptions.add(sub1);
    }

    @Override
    public void loadNextGigs() {
        mSubscriptions.unsubscribe();
        mModel.loadNextGigs();
        mView.showNextGigs(mModel.isLoadMore());
    }

    @Override
    public void openGigDetails(@NonNull Event requestedGig) {
        mView.showGigDetails(requestedGig.getEventid());
    }

    @Override
    public void setFilter(GigsFilter filter) {

    }

    @Override
    public void subscribe() {
        if (mModel.getEvents() == null) {
            mView.onSubscribed();
        }
        else mView.showGigs(mModel.getEvents(), mModel.isLoadMore());
    }

    @Override
    public void unsubscribe() {

    }

    //endregion

}
