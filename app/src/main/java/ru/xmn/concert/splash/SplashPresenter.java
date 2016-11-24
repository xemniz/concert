package ru.xmn.concert.splash;

import android.support.annotation.NonNull;

import ru.xmn.concert.gigs.GigsContract;
import ru.xmn.concert.gigs.GigsModel;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.fernandocejas.frodo.core.checks.Preconditions.checkNotNull;

/**
 * Created by xmn on 19.11.2016.
 */

public class SplashPresenter implements SplashContract.Presenter {


    private final SplashModel mModel;
    private final SplashContract.View mView;

    @NonNull
    private CompositeSubscription mSubscriptions;

    public SplashPresenter (@NonNull SplashModel model,
                            @NonNull SplashContract.View view) {
        mModel = checkNotNull(model);
        mView = checkNotNull(view);
        mSubscriptions = new CompositeSubscription();
    }

    //region Contract
    @Override
    public void subscribe() {
        Subscription subscription = mModel.loadGigsToRealm()
                .doOnError(mView::showLoadingError)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(events -> mView.showEventsActivity());
        mSubscriptions.add(subscription);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
    //endregion
}
