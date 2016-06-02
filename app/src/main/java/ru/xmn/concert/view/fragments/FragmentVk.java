package ru.xmn.concert.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.xmn.concert.R;
import ru.xmn.concert.view.adapters.BandsEventsAdapter;

public class FragmentVk extends Fragment {

    private BandsEventsAdapter adapter;

    public FragmentVk() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vk, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BandsEventsAdapter(this.getContext());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public BandsEventsAdapter getAdapter() {
        return adapter;
    }
}
