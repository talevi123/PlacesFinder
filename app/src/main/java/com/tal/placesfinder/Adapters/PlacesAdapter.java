package com.tal.placesfinder.Adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tal.placesfinder.Moduls.Place;
import com.tal.placesfinder.R;

import java.util.List;

/**
 * Created by tal on 04/12/16.
 */
public abstract class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    public Context context;
    public List<Place> placesList;

    public PlacesAdapter(Context context, List<Place> placesList) {
        this.context = context;
        this.placesList = placesList;
    }

    public Place getItem(int position) {
        return placesList != null ? placesList.get(position) : null;
    }

    public void swap (List<Place> newplacesList) {
        placesList.clear();
        placesList.addAll(newplacesList);
        notifyDataSetChanged();
    }

    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayout(parent);
        return new ViewHolder(view);
    }

    protected abstract View getLayout(ViewGroup parent);

    @Override
    public void onBindViewHolder(PlacesAdapter.ViewHolder holder, int position) {
        String kmOrMile = PreferenceManager.getDefaultSharedPreferences(context).
                getString(context.getString(R.string.distance_key),context.getString(R.string.km));

        Place place = placesList.get(position);
        holder.name.setText(place.getName());
        holder.adress.setText(place.getVicinity());

        if (place.getPhotoUrl() != null) {
            Picasso.with(context).load(place.getPhotoUrl()).fit().into(holder.photo);
        } else {

        }

        if (kmOrMile.equals(context.getString(R.string.km))) {
            holder.distance.setText(place.getDistance());
        } else  {
            holder.distance.setText(String.valueOf(Double.valueOf(place.getDistance()) / 1.6));
            holder.km_mile.setText(R.string.mile_distance);
        }
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView adress;
        public TextView distance;
        public TextView km_mile;
        public ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.place_name);
            adress = (TextView) itemView.findViewById(R.id.adress);
            distance = (TextView) itemView.findViewById(R.id.distance);
            km_mile = (TextView) itemView.findViewById(R.id.km_mil);
            photo = (ImageView) itemView.findViewById(R.id.photo);
        }
    }
}
