package ru.xmn.concert.gigs;

import android.support.annotation.NonNull;

import java.util.List;

import ru.xmn.concert.mvp.BasePresenter;
import ru.xmn.concert.mvp.view.BaseView;
import ru.xmn.concert.gigs.filter.GigsFilter;
import ru.xmn.concert.mvp.model.data.Event;

/**
 * Created by xmn on 18.11.2016.
 */

public interface GigsContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showGigs(List<Event> gigs, boolean loadMore, boolean isScrollNeeded);

        void showNextGigs(boolean loadMore);

        void showGigDetails(String gigId);

        void onSubscribed();
    }

    interface Presenter extends BasePresenter {
        void loadGigs();

        void loadNextGigs();

        void openGigDetails(@NonNull Event requestedGig);

        void setFilter(GigsFilter filter);

        void changeFilter(int id, boolean isApply);
    }
}