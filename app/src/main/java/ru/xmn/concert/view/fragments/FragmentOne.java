package ru.xmn.concert.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ru.xmn.concert.R;
import ru.xmn.concert.view.adapters.EventsAdapter;

public class FragmentOne extends Fragment {

    private EventsAdapter adapter;

    public FragmentOne() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventsAdapter();
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public EventsAdapter getAdapter() {
        return adapter;
    }
}
