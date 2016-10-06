package ru.xmn.concert.viewimpl.adapters;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.sa90.infiniterecyclerview.InfiniteAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.xmn.concert.R;
import ru.xmn.concert.model.data.EventRealm;


public class EventAdapter extends InfiniteAdapter<RecyclerView.ViewHolder> {
    private List<EventRealm> eventsList;
    private Context mContext;

    public EventAdapter(ArrayList<EventRealm> eventsList, Context mContext) {
        this.eventsList = eventsList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder getLoadingViewHolder(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View loadingView = inflater.inflate(R.layout.list_loading_view, parent, false);
        return new LoadingViewHolder(loadingView);
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public int getViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dummyView = inflater.inflate(R.layout.item_event, parent, false);
        EventViewHolder eventViewHolder = new EventViewHolder(dummyView);
        return eventViewHolder;
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
            EventRealm eventGig = eventsList.get(position);
            ((EventViewHolder) holder).name.setText(eventGig.getName());
            ((EventViewHolder) holder).date.setText(eventGig.getDate()+", "+eventGig.getTime()+", "+eventGig.getPrice());
            ((EventViewHolder) holder).place.setText(eventGig.getPlace().getName());
        }
    }
}
