package ru.xmn.concert.viewimpl.adapters;

        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.TextView;

        import ru.xmn.concert.R;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView place;
    public TextView name;

    public EventViewHolder(View itemView) {
        super(itemView);
        date = (TextView) itemView.findViewById(R.id.adapterEventInfo);
        place = (TextView) itemView.findViewById(R.id.adapterEventName);
        name = (TextView) itemView.findViewById(R.id.adapterBandName);
    }
}