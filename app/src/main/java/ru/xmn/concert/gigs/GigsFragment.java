package ru.xmn.concert.gigs;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sa90.infiniterecyclerview.InfiniteRecyclerView;
import com.sa90.infiniterecyclerview.listener.OnLoadMoreListener;

import java.util.List;

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
    private InfiniteRecyclerView mEventRecyclerView;
    //filter
    private GigsFilter mFilter;
    public static final int FILTER_VK = 1;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar mProgressBar;

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
        initRecyclerView(v);
        mProgressBar = (ProgressBar) v.findViewById(R.id.gigs_progress);
        return v;
    }

    private void initRecyclerView(View v) {
        mEventAdapter = new EventAdapter(getActivity().getBaseContext());
        mEventRecyclerView = (InfiniteRecyclerView) v.findViewById(R.id.recycler_view_main);
        mEventRecyclerView.setHasFixedSize(true);
        mEventRecyclerView.setAdapter(mEventAdapter);
        mLayoutManager = new LinearLayoutManager(mEventRecyclerView.getContext());
        mEventRecyclerView.setLayoutManager(mLayoutManager);
        mEventRecyclerView.setOnLoadMoreListener(() -> new Handler().post(mPresenter::loadNextGigs));
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
    //endregion

    //region Contract
    @Override
    public void setLoadingIndicator(boolean active) {
            mEventRecyclerView.setVisibility(active?View.GONE:View.VISIBLE);
            mProgressBar.setVisibility(active?View.VISIBLE:View.GONE);
    }

    @Override
    public void showGigs(List<Event> gigs, boolean loadMore, boolean isScrollNeeded) {
        mEventAdapter.setEvents(gigs);
        mEventRecyclerView.setShouldLoadMore(loadMore);
        if (isScrollNeeded) {
            mLayoutManager.scrollToPositionWithOffset(0, 0);
        }
        setLoadingIndicator(false);
    }

    @Override
    public void showNextGigs(boolean loadMore) {
        int from = mEventAdapter.getCount();
        if (loadMore) {
            mEventRecyclerView.moreDataLoaded(from, 20);
        }
        mEventRecyclerView.setShouldLoadMore(loadMore);
        mEventRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showGigDetails(String gigId) {

    }

    @Override
    public void onSubscribed() {
        mPresenter.loadGigs();
    }

    @Override
    public void setPresenter(@NonNull GigsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
    //endregion

}
