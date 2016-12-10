package ru.xmn.concert.gigs;

import android.support.annotation.NonNull;
import android.util.Log;

import io.realm.Realm;
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

class GigsPresenter implements GigsContract.Presenter {
    private static final String TAG = "GigsPresenter";
    private final GigsModel mModel;
    private final GigsContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private Realm mRealmMain;

    GigsPresenter(@NonNull GigsModel model,
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
        createRealm();
        Subscription loadGigsSubscription = mModel.loadGigsResults(mRealmMain)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::showGigs);
        mSubscriptions.add(loadGigsSubscription);
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
    public void viewDestroyed() {
        if (mRealmMain!=null&&!mRealmMain.isClosed()) {
            mRealmMain.close();
        }
    }

    @Override
    public void subscribe() {
        Log.d(TAG, "subscribe() called");
        createRealm();
        loadGigs();
    }

    @Override
    public void unsubscribe() {
        Log.d(TAG, "unsubscribe() called");
    }

    //endregion

    private void createRealm() {
        if (mRealmMain==null||mRealmMain.isClosed()) {
            mRealmMain = Realm.getDefaultInstance();
        }
    }

}
