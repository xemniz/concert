package ru.xmn.concert.gigs.adapters;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.sa90.infiniterecyclerview.InfiniteAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.xmn.concert.R;
import ru.xmn.concert.mvp.model.data.Event;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


public class EventAdapter extends InfiniteAdapter<RecyclerView.ViewHolder> {
    private static final String TAG = "EventAdapter";
    private Context mContext;
    private List<Event> mEvents;

    public EventAdapter(Context context) {
        mEvents = new ArrayList<>();
        mContext = context;
    }

    public void addEvents(List<Event> events){
        Log.d(TAG, "addEvents() called with: events = [" + Thread.currentThread().getName() + "]");
        Observable.just(events)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(events1 -> {
                    mEvents.addAll(events);
                    notifyDataSetChanged();
                });

    }

    public void setEvents(List<Event> events) {
        Log.d(TAG, "setEvents() called with: events = [" + events + "]");
        mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder getLoadingViewHolder(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View loadingView = inflater.inflate(R.layout.list_loading_view, parent, false);
        return new LoadingViewHolder(loadingView);
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public int getViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dummyView = inflater.inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(dummyView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            ObjectAnimator animator = ObjectAnimator.ofFloat(loadingViewHolder.loadingImage, "rotation", 0, 360);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(1000);
            animator.start();
            return;
        }
        else {
            Event eventGig = mEvents.get(position);
            ((EventViewHolder) holder).name.setText(eventGig.getName());
            ((EventViewHolder) holder).date.setText(eventGig.getDate()+", "+eventGig.getTime()+", "+eventGig.getPrice());
            ((EventViewHolder) holder).place.setText(eventGig.getPlace().getName());
        }
    }
}
