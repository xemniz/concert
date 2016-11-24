package ru.xmn.concert.gigs;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sa90.infiniterecyclerview.InfiniteRecyclerView;
import com.sa90.infiniterecyclerview.listener.OnLoadMoreListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import ru.xmn.concert.R;
import ru.xmn.concert.mvp.model.data.Event;
import ru.xmn.concert.mvp.view.EventListView;
import ru.xmn.concert.gigs.adapters.EventAdapter;


public class MainFragment extends Fragment implements EventListView {
    private InfiniteRecyclerView mEventRecyclerView;
    private EventAdapter mEventAdapter;
    private ArrayList<Event> mEvents;
    private MainPresenter mMainPresenter;
    private EventListView mView = this;
    private int mEventListSize;
    private int currentPage;
    final private int onPage = 50;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        initRecyclerView(v);
        mMainPresenter = new MainPresenter(mView);
        currentPage = 0;
        mMainPresenter.onViewCreated(currentPage++, onPage);

        return v;
    }

    private void initRecyclerView(View v) {
        mEvents = new ArrayList<>();

        mEventAdapter = new EventAdapter( getActivity().getBaseContext());
        mEventRecyclerView = (InfiniteRecyclerView) v.findViewById(R.id.recycler_view_main);
        mEventRecyclerView.setHasFixedSize(true);
        mEventRecyclerView.setAdapter(mEventAdapter);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(mEventRecyclerView.getContext()));
        mEventRecyclerView.setOnLoadMoreListener(mLoadMoreListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private OnLoadMoreListener mLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            Log.v("Main", "Load more fired");
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Log.d(getClass().getSimpleName(), "1: " + new Time(System.currentTimeMillis()));
                    if (currentPage < mEventListSize / onPage) {
                        mMainPresenter.addMoreData(currentPage++, onPage);
                        mEventRecyclerView.moreDataLoaded((currentPage) * onPage, onPage);
                        mEventRecyclerView.setShouldLoadMore(true);
                    } else {
                        int retainPages = mEventListSize % MainFragment.this.onPage;
                        if (retainPages>0) {
                            mMainPresenter.addMoreData(currentPage++, retainPages);
                            mEventRecyclerView.moreDataLoaded((currentPage) * MainFragment.this.onPage, retainPages);
                        }
                        mEventRecyclerView.setShouldLoadMore(false);
                    }
                }
            });
        }
    };

    @Override
    public void loadEventList(List<Event> events) {
        mEvents.addAll(events);
        mEventRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setListSize(Integer listSize) {
        mEventListSize = listSize;
    }

    @Override
    public void refresh() {
        mEvents.clear();
        mEventRecyclerView.getAdapter().notifyDataSetChanged();
        currentPage = 0;
    }
}
