package ru.xmn.concert.gigs.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import ru.xmn.concert.R;
import ru.xmn.concert.mvp.model.data.Event;


public class EventAdapter extends RealmBasedRecyclerViewAdapter<Event, EventAdapter.ViewHolder> {
    private static final String TAG = "EventAdapter";

    public EventAdapter(
            Context context,
            RealmResults<Event> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }


    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.item_event, viewGroup, false);
        return new ViewHolder((FrameLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final Event event = realmResults.get(i);
        viewHolder.name.setText(event.getName());
        viewHolder.date.setText(event.getDate());
        viewHolder.place.setText(event.getPlace().getName());
        viewHolder.image.setImageResource(android.R.color.transparent);
        if (event.getPoster()!=null&&event.getPoster().length()>0) {
            Picasso.with(getContext()).load(event.getPoster()).into(viewHolder.image);
        }
    }


    class ViewHolder extends RealmViewHolder {
        TextView date;
        TextView place;
        TextView name;
        ImageView image;

        ViewHolder(FrameLayout container) {
            super(container);
            date = (TextView) itemView.findViewById(R.id.adapterEventInfo);
            place = (TextView) itemView.findViewById(R.id.adapterEventName);
            name = (TextView) itemView.findViewById(R.id.adapterBandName);
            image = (ImageView) itemView.findViewById(R.id.adapterBandImage);
        }
    }
}
