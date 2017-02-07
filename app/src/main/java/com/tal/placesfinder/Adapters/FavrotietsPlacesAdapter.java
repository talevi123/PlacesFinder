package com.tal.placesfinder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tal.placesfinder.Moduls.Place;
import com.tal.placesfinder.R;

import java.util.List;

/**
 * Created by tal on 22/12/16.
 */
public class FavrotietsPlacesAdapter extends PlacesAdapter {
    public FavrotietsPlacesAdapter(Context context, List<Place> placesList) {
        super(context, placesList);
    }

    @Override
    protected View getLayout(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card, parent, false);
    }
}
