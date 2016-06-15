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
import ru.xmn.concert.model.data.BandRockGig;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.model.data.EventRockGig;

/**
 * Created by xmn on 01.06.2016.
 */

public class BandsEventsAdapter extends RecyclerView.Adapter<BandsEventsAdapter.ViewHolder> {

    private List<BandRockGig> bandRockGigs = new ArrayList<>();
    private Context context;

    public BandsEventsAdapter(Context context) {
        this.context = context;

    }

    public void setBandRockGigs(List<BandRockGig> repoList) {
        bandRockGigs.addAll(repoList);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_adapter_layout, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        BandRockGig bandRockGig = bandRockGigs.get(i);
        viewHolder.name.setText(bandRockGig.getBand());
        EventRockGig event = bandRockGig.getGigs().get(0);
        viewHolder.date.setText(event.getDate()+", "+event.getTime()+", "+event.getPrice());
        if (bandRockGig.getGigs().size()>1)
            viewHolder.place.setText(event.getPlace().getName()+" - "+event.getName()+" +"+ (bandRockGig.getGigs().size()-1));
        else
            viewHolder.place.setText(event.getPlace().getName()+" - "+event.getName());

        Picasso.with(context)
                .load(bandRockGig.getBandImageUrl())
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return bandRockGigs.size();
    }

    public void addBands(List<BandRockGig> bandRockGigs) {

        this.bandRockGigs.addAll(bandRockGigs);
        notifyDataSetChanged();
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
    public void add(BandRockGig gig, int position) {
        bandRockGigs.add(position, gig);
        notifyItemInserted(position);
    }

    public void remove(EventGig gig) {
        int position = bandRockGigs.indexOf(gig);
        bandRockGigs.remove(position);
        notifyItemRemoved(position);
    }
}