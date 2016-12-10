package ru.xmn.concert.gigs;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.RealmResults;
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

        void showGigs(RealmResults<Event> events);

        void showGigDetails(String gigId);
    }

    interface Presenter extends BasePresenter {
        void loadGigs();

        void openGigDetails(@NonNull Event requestedGig);

        void setFilter(GigsFilter filter);

        void changeFilter(int id, boolean isApply);

        void viewDestroyed();
    }
}
