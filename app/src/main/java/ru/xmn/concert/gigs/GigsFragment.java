package ru.xmn.concert.gigs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmResults;
import ru.xmn.concert.R;
import ru.xmn.concert.gigs.adapters.EventAdapter;
import ru.xmn.concert.gigs.filter.GigsFilter;
import ru.xmn.concert.mvp.model.data.Event;

import static com.fernandocejas.frodo.core.checks.Preconditions.checkNotNull;

/**
 * Created by xmn on 18.11.2016.
 */

public class GigsFragment extends Fragment implements GigsContract.View {
    private static final String TAG = "GigsFragment";
    private GigsContract.Presenter mPresenter;
    private EventAdapter mEventAdapter;
    //filter
    private GigsFilter mFilter;
    public static final int FILTER_VK = 1;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private RealmRecyclerView mRealmRecyclerView;

    public static GigsFragment newInstance() {
        Bundle args = new Bundle();
        GigsFragment fragment = new GigsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //region Lifecycle
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mRealmRecyclerView = (RealmRecyclerView) v.findViewById(R.id.recycler_view_main);
        mProgressBar = (ProgressBar) v.findViewById(R.id.gigs_progress);
        return v;
    }

    private void initRecyclerView(RealmResults<Event> events) {
        EventAdapter eventAdapter = new EventAdapter(getContext(), events, true, true);
        mRealmRecyclerView.setAdapter(eventAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        mPresenter.viewDestroyed();
        super.onDestroyView();
    }

    //endregion

    //region Contract
    @Override
    public void setLoadingIndicator(boolean active) {
        mRealmRecyclerView.setVisibility(active ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showGigs(RealmResults<Event> events) {
        initRecyclerView(events);
        setLoadingIndicator(false);
    }

    @Override
    public void showGigDetails(String gigId) {

    }

    @Override
    public void setPresenter(@NonNull GigsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
    //endregion

}
