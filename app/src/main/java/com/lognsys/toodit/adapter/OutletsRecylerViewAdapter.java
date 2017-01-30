package com.lognsys.toodit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lognsys.toodit.R;

/**
 * Created by pdoshi on 25/01/17.
 */

public class OutletsRecylerViewAdapter extends RecyclerView.Adapter<OutletRecylerViewHolders>  {

    private Integer[] restaurantThumbImages;
    private Context context;

    public OutletsRecylerViewAdapter(Context context, Integer[] restaurantThumbImages) {
        this.restaurantThumbImages = restaurantThumbImages;
        this.context = context;
    }

    @Override
    public OutletRecylerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_card_list_view, null);
        OutletRecylerViewHolders orcv = new OutletRecylerViewHolders(layoutView);
        return orcv;
    }

    @Override
    public void onBindViewHolder(OutletRecylerViewHolders holder, int position) {
              holder.outletPhoto.setImageResource(restaurantThumbImages[position]);
    }

    @Override
    public int getItemCount() {
        return this.restaurantThumbImages.length;
    }
}
