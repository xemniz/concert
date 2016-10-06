package ru.xmn.concert.viewimpl.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import ru.xmn.concert.R;


/**
 * Created by Saurabh on 6/2/16.
 */
public class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ImageView loadingImage;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        loadingImage = (ImageView) itemView.findViewById(R.id.loadingImage);
    }
}
