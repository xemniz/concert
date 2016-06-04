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
import java.util.List;

import ru.xmn.concert.R;
import ru.xmn.concert.model.data.EventGig;

/**
 * Created by xmn on 01.06.2016.
 */

public class BandsEventsAdapter extends RecyclerView.Adapter<BandsEventsAdapter.ViewHolder> {

    private List<EventGig> eventGigs = new ArrayList<>();
    private Context context;

    public BandsEventsAdapter(Context context) {
        this.context = context;
    }

    public void setGigList(List<EventGig> repoList) {
        this.eventGigs = repoList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_adapter_layout, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        EventGig eventGig = eventGigs.get(i);
//        viewHolder.name.setText(eventGig.getName());
        viewHolder.name.setText(eventGig.getRequestBand());
        viewHolder.date.setText(eventGig.getDate()+", "+eventGig.getTime()+", "+eventGig.getPrice());
        if (eventGig.getCountOfSimilar()>1)
            viewHolder.place.setText(eventGig.getPlace()+" - "+eventGig.getName()+" +"+ (eventGig.getCountOfSimilar()-1));
        else
            viewHolder.place.setText(eventGig.getPlace()+" - "+eventGig.getName());

        Picasso.with(context)
                .load(eventGig.getBandImageUrl())
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return eventGigs.size();
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
    public void add(EventGig gig, int position) {
        eventGigs.add(position, gig);
        notifyItemInserted(position);
    }

    public void remove(EventGig gig) {
        int position = eventGigs.indexOf(gig);
        eventGigs.remove(position);
        notifyItemRemoved(position);
    }
}