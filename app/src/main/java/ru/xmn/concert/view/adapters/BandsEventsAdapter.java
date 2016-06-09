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
import java.util.Set;

import ru.xmn.concert.R;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.model.data.RockGigEvent;

/**
 * Created by xmn on 01.06.2016.
 */

public class BandsEventsAdapter extends RecyclerView.Adapter<BandsEventsAdapter.ViewHolder> {

    private List<Band> bands = new ArrayList<>();
    private Context context;

    public BandsEventsAdapter(Context context) {
        this.context = context;
    }

    public void setGigList(List<Band> repoList) {
        this.bands = repoList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_adapter_layout, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Band band = bands.get(i);
//        viewHolder.name.setText(eventGig.getName());
        viewHolder.name.setText(band.getBand());
        RockGigEvent event = band.getGigs().get(0);
        viewHolder.date.setText(event.getDate()+", "+event.getTime()+", "+event.getPrice());
        if (band.getGigs().size()>1)
            viewHolder.place.setText(event.getPlace().getName()+" - "+event.getName()+" +"+ (band.getGigs().size()-1));
        else
            viewHolder.place.setText(event.getPlace().getName()+" - "+event.getName());

        Picasso.with(context)
                .load(band.getBandImageUrl())
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return bands.size();
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
    public void add(Band gig, int position) {
        bands.add(position, gig);
        notifyItemInserted(position);
    }

    public void remove(EventGig gig) {
        int position = bands.indexOf(gig);
        bands.remove(position);
        notifyItemRemoved(position);
    }
}