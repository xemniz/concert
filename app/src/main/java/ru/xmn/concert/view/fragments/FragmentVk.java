package ru.xmn.concert.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import ru.xmn.concert.R;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.model.data.EventRockGig;
import ru.xmn.concert.presenter.PresenterVkFragment;
import ru.xmn.concert.view.BandsView;
import ru.xmn.concert.view.adapters.BandsEventsAdapter;
import ru.xmn.concert.view.adapters.EndlessRecyclerViewScrollListener;
import ru.xmn.concert.view.adapters.EventsBandsAdapter;
import ru.xmn.concert.view.adapters.EventsRealmAdapter;
import ru.xmn.concert.view.common.MvpAppCompatFragment;

public class FragmentVk extends MvpAppCompatFragment implements BandsView {
    @InjectPresenter
    PresenterVkFragment presenter;
    private BandsEventsAdapter bandsEventsAdapter;
    private EventsBandsAdapter eventsBandsAdapter;
    private EventsRealmAdapter eventsRealmAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog mErrorDialog;

    public FragmentVk() {
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null)
        {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vk, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_swipe_refresh_layout);
        recyclerView.setLayoutManager(linearLayoutManager);
        eventsRealmAdapter = new EventsRealmAdapter(this.getContext());
        presenter.setAdapter(eventsRealmAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
//                presenter.bandList(page);
                presenter.eventList(page, false);
            }
        });

        mErrorDialog = new AlertDialog.Builder(this.getContext())
                .setTitle(R.string.app_name)
                .setOnCancelListener(dialog -> presenter.closeError())
                .create();

        return rootView;
    }

    @Override
    public void showData(List<Band> list) {

    }

    @Override
    public void showError(String message) {
        mErrorDialog.setMessage(message);
        mErrorDialog.show();
    }

    @Override
    public void hideError() {
        mErrorDialog.hide();
    }

    @Override
    public void onStartLoading() {
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onFinishLoading() {
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void showRefreshing() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideRefreshing() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void showListProgress() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideListProgress() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void setGigsRealm(List<String> bands) {
        eventsRealmAdapter.setGigs(bands);
    }

    @Override
    public void addGigsRealm(List<String> bands) {
        eventsRealmAdapter.addGigs(bands);
    }
}
