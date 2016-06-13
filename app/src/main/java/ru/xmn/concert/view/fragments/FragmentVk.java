package ru.xmn.concert.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import ru.xmn.concert.R;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.presenter.PresenterVkFragment;
import ru.xmn.concert.view.BandsView;
import ru.xmn.concert.view.adapters.BandsEventsAdapter;
import ru.xmn.concert.view.adapters.EndlessRecyclerViewScrollListener;
import ru.xmn.concert.view.common.MvpAppCompatFragment;

public class FragmentVk extends MvpAppCompatFragment implements BandsView {
    @InjectPresenter
    PresenterVkFragment presenter;
    private BandsEventsAdapter adapter;

    public FragmentVk() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vk, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BandsEventsAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                presenter.bandList(page);
            }
        });

        return rootView;
    }

    @Override
    public void showData(List<Band> list) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void hideError() {

    }

    @Override
    public void onStartLoading() {

    }

    @Override
    public void onFinishLoading() {

    }

    @Override
    public void showRefreshing() {

    }

    @Override
    public void hideRefreshing() {

    }

    @Override
    public void showListProgress() {

    }

    @Override
    public void hideListProgress() {

    }

    @Override
    public void setBands(List<Band> bands) {
        adapter.setBands(bands);
    }

    @Override
    public void addBands(List<Band> bands) {
        adapter.addBands(bands);
    }
}
