package ru.xmn.concert.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.xmn.concert.R;
import ru.xmn.concert.model.data.EventGig;

/**
 * Created by xmn on 22.05.2016.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<EventGig> eventGigs = new ArrayList<>();

    public void setGigList(List<EventGig> repoList) {
        this.eventGigs = repoList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_layout, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        EventGig eventGig = eventGigs.get(i);
        viewHolder.name.setText(eventGig.getBand()+" "+eventGig.getDateTime());
    }

    @Override
    public int getItemCount() {
        return eventGigs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}