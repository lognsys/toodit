package com.lognsys.toodit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lognsys.toodit.R;

/**
 * Created by pdoshi on 25/01/17.
 */

public class OutletRecylerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView outletPhoto;

    public OutletRecylerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        outletPhoto = (ImageView) itemView.findViewById(R.id.outlet_photo);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}


