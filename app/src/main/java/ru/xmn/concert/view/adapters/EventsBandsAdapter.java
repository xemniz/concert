package ru.xmn.concert.view.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.xmn.concert.R;
import ru.xmn.concert.model.data.BandRockGig;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.model.data.EventRockGig;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xmn on 01.06.2016.
 */

public class EventsBandsAdapter extends RecyclerView.Adapter<EventsBandsAdapter.ViewHolder> {

    private List<EventRockGig> gigs = new ArrayList<>();
    private Context context;

    public EventsBandsAdapter(Context context) {
        this.context = context;

    }

    public void setGigs(List<EventRockGig> repoList) {
        gigs.addAll(repoList);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_adapter_layout, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.image.setImageDrawable(null);
        EventRockGig event = gigs.get(i);
        viewHolder.place.setText(event.getPlace().getName() + " - " + event.getName());
        viewHolder.date.setText(event.getDate() + ", " + event.getTime() + ", " + event.getPrice());
        if (event.getBandRockGigs().size()>0) {
            BandRockGig defaultBandRockGig = event.getBandRockGigs().get(0);
            Picasso.with(context)
                    .load(defaultBandRockGig.getBandImageUrl())
                    .into(viewHolder.image);
            viewHolder.name.setText(defaultBandRockGig.getBand());
        } else
        {
            Picasso.with(context)
                    .load("http://blog.songcastmusic.com/wp-content/uploads/2013/08/iStock_000006170746XSmall.jpg")
                    .into(viewHolder.image);
            viewHolder.name.setText("");
        }

        Observable<BandRockGig> changeView = Observable
                .just(event.getBandRockGigs())
                .flatMap(bands -> {
                    Collections.shuffle(bands);
                    return Observable.from(bands);
                });


        Observable.zip(
                changeView,
                Observable.interval(4, TimeUnit.SECONDS),
                (band, aLong) -> band)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(band -> {
                    Picasso.with(context)
                            .load(band.getBandImageUrl())
                            .into(viewHolder.image);
                    viewHolder.name.setText(band.getBand());
                });
    }

    @Override
    public int getItemCount() {
        return gigs.size();
    }

    public void addGigs(List<EventRockGig> gigs) {

        this.gigs.addAll(gigs);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView place;
        private TextView date;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.adapterBandName);
            place = (TextView) itemView.findViewById(R.id.adapterEventName);
            date = (TextView) itemView.findViewById(R.id.adapterEventInfo);
            image = (ImageView) itemView.findViewById(R.id.adapterBandImage);
        }

    }

    public void add(EventRockGig gig, int position) {
        gigs.add(position, gig);
        notifyItemInserted(position);
    }

    public void remove(EventGig gig) {
        int position = gigs.indexOf(gig);
        gigs.remove(position);
        notifyItemRemoved(position);
    }
}